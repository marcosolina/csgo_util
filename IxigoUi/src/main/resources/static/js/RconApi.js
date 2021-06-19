var RconApi = ((function(RconApi){
	"use strict";
	
	if(RconApi === undefined){
		RconApi = {};
    }
    
    let tplRconSectionTitle = '<div class="rcon-container-title">' +
                                 '<h1>%title%</h1>' + 
                              '</div>';
    let tplRconCmdsContainer = '<div class="rcon-container"></div>';
    let tplRconPictureCmd = '<div class="card rcon-map" data-rcon-cmd="%rconCmd%">' +
                                '<img class="card-img-top" src="%imgSrc%" alt="Send RCON cmd">' +
                                '<div class="rcon-card-body">%cardDesc%</div>' +
                            '</div>';
    let tplRconIconCmd = '<div class="card rcon-map" data-rcon-cmd="%rconCmd%">' +
                            '<div class="rcon-icon-div">' +
                                '<i class="%fontAwesome%" arai-hidden="true"></i>' +
                            '</div>' +
                            '<div class="rcon-card-body">%cardDesc%</div>' +
                         '</div>';
                         
    let rconCardType = {
        picture: "picture",
        fontAws: "fontAwesome",
    };
    
    RconApi.init = function(){
        $(".rcon-map").click(RconApi.changeMap);
        $("#sendRcon").click(function(){
            RconApi.sendRconCmd($("#rconCmd").val());
        });
    }
    
    RconApi.addRconCmds = function(){
        let jRconDiv = $(".rconDiv");
        
        // Bot Section
        jRconDiv.append(MarcoUtils.template(tplRconSectionTitle, {title: "Bots"}));
        let jCardContainer = $(MarcoUtils.template(tplRconCmdsContainer, {}));
        
        jCardContainer.append(MarcoUtils.template(tplRconPictureCmd, {rconCmd: "bot_add_t",  imgSrc: "./pictures/rcon/terrorist.jpg",           cardDesc: "Add a Terrorist Bot"}));
        jCardContainer.append(MarcoUtils.template(tplRconPictureCmd, {rconCmd: "bot_add_ct", imgSrc: "./pictures/rcon/counterterrorist.jpg",    cardDesc: "Add a C.T. Bot"}));
        jCardContainer.append(MarcoUtils.template(tplRconPictureCmd, {rconCmd: "bot_kick",   imgSrc: "./pictures/rcon/kickbots.jpg",            cardDesc: "Kick All the bots"}));
        
        jRconDiv.append(jCardContainer);
        
    }

    RconApi.changeMap = function(){
        RconApi.sendRconCmd($(this).data("rcon-cmd"));
    }


    RconApi.sendRconCmd = function(rconCmd){
        var request = {
            rconCmd: rconCmd,
            rconHost: $("#rconHost").val(),
            rconPort: $("#rconPort").val(),
            rconPass: $("#rconPassw").val(),
        };

        MarcoUtils.executeAjax(
            {
                body: request,
                url: __URLS.RCON_API.SEND_CMD,
                showLoading: true
            }).then(RconApi.rconCmdSent);
    }

    RconApi.rconCmdSent = function(resp){
        if(resp.status && resp.rconResponse && resp.rconResponse != ""){
            $("#rconResp").val(resp.rconResponse);
            $('#modalWindow').modal("show");
        }
    }

	return RconApi;
})(RconApi));