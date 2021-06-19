var RconApi = ((function(RconApi){
	"use strict";
	
	if(RconApi === undefined){
		RconApi = {};
    }
    
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
                url: __URLS.API_BASE + "/csgo-rest-api/rcon/cmd",
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