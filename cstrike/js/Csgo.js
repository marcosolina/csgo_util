var Csgo = ((function(Csgo){
	"use strict";
	
	if(Csgo === undefined){
		Csgo = {};
    }
    
    Csgo.init = function(){
        $(".rcon-map").click(Csgo.changeMap);
        $("#sendRcon").click(function(){
            Csgo.sendRconCmd($("#rconCmd").val());
        });
    }

    Csgo.changeMap = function(){
        Csgo.sendRconCmd($(this).data("rcon-cmd"));
    }


    Csgo.sendRconCmd = function(rconCmd){
        var request = {
            rconCmd: rconCmd,
            rconHost: $("#rconHost").val(),
            rconPort: $("#rconPort").val(),
            rconPass: $("#rconPassw").val(),
        };

        MarcoUtils.executeAjax(
            {
                dataToPost: request,
                url: "https://marco.selfip.net/rcon/cmd",
                showLoading: true
            }).then(Csgo.rconCmdSent);
    }

    Csgo.rconCmdSent = function(resp){
        if(resp.status && resp.rconResponse && resp.rconResponse != ""){
            $("#rconResp").val(resp.rconResponse);
            $('#modalWindow').modal("show");
        }
    }

	return Csgo;
})(Csgo));