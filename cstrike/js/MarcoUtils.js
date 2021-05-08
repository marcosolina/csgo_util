/*######################################################
		GLOBAL things
######################################################*/
var __NOTIFY_TYPE = {
	ERROR	:	"error",
	INFO	:	"info",
	WARNING	:	"warning",
	SUCCESS	:	"success"
};

var MarcoUtils = ((function(MarcoUtils){
	"use strict";

	if(MarcoUtils === undefined){
		MarcoUtils = {};
	}

	var idBackGroungLoading = "_bgLoading";


	/*######################################################
		NOTIFICATIONS
	######################################################*/

	/**
	* Show a Pnotify notifications
	* params is a Javascript Object
	* 
	* Basic barameters:
	* title		-> The title of the notification
	* message	-> The message of the notification
	* type		-> The type of the message (use __NOTIFY_TYPE global object)
	* 
	* @param params
	* @returns
	*/
	MarcoUtils.showNotification = function(params){
		if(!params)
			params = {};
		if(params.close === undefined)
			params.close = true;
		
		var pNotiffyParams = {
				title: params.title || "Hey!!!",
				text: params.message || "...",
				type: params.type || "error",
				delay: params.timer || 5000,
				styling: "fontawesome",
				addclass: params.classs || "",
				hide: params.close,
				/*hide:false*/
				buttons: {
					closer: true,
					closer_hover: false,
					sticker: false
				},
				animate: {
					animate: true,
					in_class: "fadeInRight",
					out_class: "fadeOutRight"
				},
				mobile: {
					swipe_dismiss: true,// - Let the user swipe the notice away.
					styling: true,// - Styles the notice to look good on mobile.
				},
			};
		
		if(params.icon)
			pNotiffyParams.icon = params.icon;
		if(params.closeAll){
			pNotiffyParams.confirm = {
				confirm: true,
				buttons: [{
					text: 'Close All',
					//addClass: 'btn-danger',
					click: PNotify.removeAll
				},null]
			};
		}
		
		var notification = new PNotify(pNotiffyParams);
		notification.get().click(function() {
			notification.remove();
		});
		return notification;
	}

	MarcoUtils.maxZIndex = function (){
		var maxZ = Math.max.apply(null,$.map($('body > *'), function(e,n){
			//if($(e).css('position')=='absolute')
				var jPnotify = $(e).closest(".ui-pnotify");
				if(jPnotify.leght != 0){
					return 101
				}else{
					return parseInt($(e).css('z-index'))||1 ;
				}
		})
		);
		return maxZ;
	}

	MarcoUtils.preventClick = function(preventUserFromClick){
		if(preventUserFromClick && $("#" + idBackGroungLoading).length < 1){
			var zIndex = MarcoUtils.maxZIndex() + 1;
			
			var backGroundLoading = "<div id='" + idBackGroungLoading + "' style='" +
						"position: fixed; " +
						"top: 0; " +
						"width: 100%; " +
						"height: 100%; " +
						"background-color: rgba(0,0,0,0.5); " +
						"z-index: " + zIndex + ";'>" +
						"" +
							"<div style='" +
								"height: 100%; " +
								"display: flex;" +
								"justify-content: center; " +
								"align-items: center; '>" +
									"<span class='fa fa-spinner fa-spin' style='" +
									"font-size: 120px; " +
									"color: #00b4c7;' >" +
									"</span>" +
							"</div>" +
						"</div>";
			$("body").append(backGroundLoading);
		}else{
			$("#" + idBackGroungLoading).remove();
		}
	}

	/*######################################################
	Ajax
	######################################################*/

	MarcoUtils.executeAjax = function(parameters){
		var deferred = jQuery.Deferred();
		
		var notificationLoading;
		if(parameters.showLoading){
			MarcoUtils.preventClick(true);
			notificationLoading = MarcoUtils.showNotification({title:"Loading", message:"Please wait", type:__NOTIFY_TYPE.INFO, close:false});
		}
		
		if(parameters.showErrors === undefined)
			parameters.showErrors = true;
		
		var ajaxConfig = {
			type: parameters.type == undefined ? "POST" : parameters.type,
			url: parameters.url,
			cache: false,
			async: parameters.async == undefined ? true : parameters.async, 
			success: function(resp){
					if(notificationLoading){
						notificationLoading.remove();
						MarcoUtils.preventClick(false);
					}
					if(resp.status){
						deferred.resolve(resp);
					}else{
						if(parameters.showErrors){
							$.each(resp.errors, function(index, error){
								MarcoUtils.showNotification({
									title: error.title, 
									message: error.message, 
									close: error.close, 
									type: __NOTIFY_TYPE[error.type] || error.type
								});
							});
						}
						deferred.reject(resp);
					}
				},
			error: function(resp){
				if(notificationLoading){
					notificationLoading.remove();
					MarcoUtils.preventClick(false);
				}
				if(parameters.hideErrors != true){
					if(resp.responseJSON && resp.responseJSON.errors){
						$.each(resp.responseJSON.errors, function(index, error){
							MarcoUtils.showNotification({
								title: error.title, 
								message: error.message, 
								close: error.close, 
								type: __NOTIFY_TYPE[error.type] || error.type
							});
						});
					}else{
						MarcoUtils.showNotification({
							message: "Something went wrong",
							type: __NOTIFY_TYPE.ERROR,
							title: "Oops",
						});
					}
				}
				deferred.reject(resp);
			},
		};
		
		if(parameters.body){
			ajaxConfig.data = JSON.stringify(parameters.body);
			ajaxConfig.contentType= "application/json";
			ajaxConfig.dataType= "json";
			ajaxConfig.processData= parameters.processData == undefined ? true : parameters.processData;
		}
		
		
		$.ajax(ajaxConfig);
		
		return deferred.promise();
	}


	MarcoUtils.initializeTooltips = function(){
		$('[data-toggle="tooltip"]').tooltip("dispose");
		$(".tooltip").remove();
		
		$('[data-toggle="tooltip"]').tooltip({
			delay: {show: 700, hide:0},
			placement: "top"
		});
	}

	MarcoUtils.formDataToJson = function(formSelector, data){
		if(typeof data == "undefined"){
			data = {};
		}
		$(formSelector).serializeArray().map(function(x){data[x.name] = x.value;});
		return data;
	}

	MarcoUtils.log = function(param){
		console.log(param);
	}

	MarcoUtils.getMessage = function(key, data){
		if(typeof __MESSAGES == "undefined"){
			MarcoUtils.log("__MESSAGES not defined");
			return key;
		}
		
		var message = MarcoUtils.template(__MESSAGES[key] || key, data || {});
		return message;
	}


	MarcoUtils.animate = function(jQelement, animation, timer){
		var deferred = jQuery.Deferred();
		var complete = false;
		var timer = timer || 800;
		
		if(animation === undefined)
			return;
		animation += " animated";
		
		jQelement.removeClass(animation).addClass(animation).one('webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend', function(){
			/*
			complete = true;
			$(this).removeClass(animation);
			deferred.resolve(jQelement);
			*/
		});
		
		/*
		* Firefox doesn't support the "one" method...
		*/
		setTimeout(function() {
			if(complete)
				return;
			jQelement.removeClass(animation);
			//console.log("Animation complete 2");
			deferred.resolve(jQelement);
		}, timer);
		
		return deferred.promise();
	}

	MarcoUtils.formatBytes = function(a,b){if(0==a)return"0 Bytes";var c=1024,d=b||2,e=["Bytes","KB","MB","GB","TB","PB","EB","ZB","YB"],f=Math.floor(Math.log(a)/Math.log(c));return parseFloat((a/Math.pow(c,f)).toFixed(d))+" "+e[f]}

	MarcoUtils.showModal = function(jObj){
		var deferred = jQuery.Deferred();
		jObj.modal("show");
		MarcoUtils.animate(jObj.children("div").show(), "zoomInUp").then(function(){
			deferred.resolve();
			MarcoUtils.initializeTooltips();
		});
		return deferred.promise();
	}

	MarcoUtils.hideModal = function(jObj){
		var deferred = jQuery.Deferred();
		MarcoUtils.animate(jObj.children("div"), "zoomOutDown").then(function(){
			jObj.modal("hide");
			deferred.resolve();
		});
		return deferred.promise();
	}

	/*
	* Apply the data (javascript object) to the template
	* string
	*/
	MarcoUtils.template = function(tplString, data){
		return tplString.replace(/%(\w*)%/g,function(m,key){return data.hasOwnProperty(key)?data[key]:"";});
	}

	MarcoUtils.randomString = function(size) {
	var text = "";
	var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

	for (var i = 0; i < size; i++)
		text += possible.charAt(Math.floor(Math.random() * possible.length));

	return text;
	}


	MarcoUtils.pad = function(num) {
		var s = "000000" + num;
		return s.substr(s.length - 6);
	}


	MarcoUtils.scrollToTop = function(selector, speed) {
		var jElement = $(selector);
		jElement.animate({
			scrollTop: 0,
		}, speed || 1000); 
	};

	MarcoUtils.scrollToBottom = function(selector, speed) {
		var jElement = $(selector);
		var height = jElement[0].scrollHeight;
		jElement.animate({
			scrollTop: height,
		}, speed || 1000); 
	};

	MarcoUtils.scrollTo = function(containerElement, elem, speed, offset) {
		try{
			offset = offset || 0;//to compensate the node padding
			
			var currentScrollTop = $(containerElement).scrollTop();
			var newScrollTop = $(containerElement).scrollTop() - $(containerElement).offset().top + $(elem).offset().top - offset;
			if(currentScrollTop == newScrollTop || !$(elem).is(":visible")){
				return;
			}
			
			$(containerElement).animate({
				scrollTop:  newScrollTop,
			}, speed == undefined ? 400 : speed); 
		}catch(error){
			
		}
		
	};

	MarcoUtils.goToUrl = function(parameters){
		MarcoUtils.preventClick(true);
		window.open(parameters.url, parameters.target || "_self");
	}

	return MarcoUtils;
})(MarcoUtils));
