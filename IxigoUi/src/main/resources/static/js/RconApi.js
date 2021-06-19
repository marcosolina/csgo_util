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
    
    RconApi.init = function(){
        $(".rcon-map").click(RconApi.changeMap);
        $("#sendRcon").click(function(){
            RconApi.sendRconCmd($("#rconCmd").val());
        });
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