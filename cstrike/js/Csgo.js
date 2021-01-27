var Csgo = ((function(Csgo){
	"use strict";
	
	if(Csgo === undefined){
		Csgo = {};
    }
    
    Csgo.init = function(){
        $(".rcon-map").click(Csgo.changeMap);
    }

    Csgo.changeMap = function(){
        var request = $(this).data();
        request.rconHost = $("#rconHost").val();
        request.rconPort = $("#rconPort").val();
        request.rconPass = $("#rconPassw").val();
        MarcoUtils.executeAjax(
            {
                dataToPost: request,
                url: "https://marco.selfip.net/rcon/cmd",
                showLoading: true
            });
    }
	return Csgo;
})(Csgo));