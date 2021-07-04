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
        RconApi.addRconCmds();
        $(".rcon-map").click(RconApi.changeMap);
        $("#sendRcon").click(function(){
            RconApi.sendRconCmd($("#rconCmd").val());
        });
        $("#rconHost").val(__RCON_PROPS.host);
    }
    
    RconApi.addRconCmds = function(){
        let jRconDiv = $("#rconDiv");
        let jCardContainer;
        
        // Bot Section
        jRconDiv.append(MarcoUtils.template(tplRconSectionTitle, {title: "Bots"}));
        jCardContainer = $(MarcoUtils.template(tplRconCmdsContainer, {}));
        
        jCardContainer.append(MarcoUtils.template(tplRconPictureCmd, {rconCmd: "bot_add_t",  imgSrc: "./pictures/rcon/terrorist.jpg",           cardDesc: "Add a Terrorist Bot"}));
        jCardContainer.append(MarcoUtils.template(tplRconPictureCmd, {rconCmd: "bot_add_ct", imgSrc: "./pictures/rcon/counterterrorist.jpg",    cardDesc: "Add a C.T. Bot"}));
        jCardContainer.append(MarcoUtils.template(tplRconPictureCmd, {rconCmd: "bot_kick",   imgSrc: "./pictures/rcon/kickbots.jpg",            cardDesc: "Kick All the bots"}));
        
        jRconDiv.append(jCardContainer);
        
        
        // Game Section
        jRconDiv.append(MarcoUtils.template(tplRconSectionTitle, {title: "Game"}));
        jCardContainer = $(MarcoUtils.template(tplRconCmdsContainer, {}));
        
        jCardContainer.append(MarcoUtils.template(tplRconIconCmd, {rconCmd: "mp_restartgame 5", fontAwesome: "fa fa-refresh",           cardDesc: "Restart Game"}));
        jCardContainer.append(MarcoUtils.template(tplRconIconCmd, {rconCmd: "pause",            fontAwesome: "fa fa-pause-circle-o",    cardDesc: "Pause Game"}));
        jCardContainer.append(MarcoUtils.template(tplRconIconCmd, {rconCmd: "unpause",          fontAwesome: "fa fa-play-circle-o",     cardDesc: "Resume Game"}));
        jCardContainer.append(MarcoUtils.template(tplRconIconCmd, {rconCmd: "exit",             fontAwesome: "fa fa-stop-circle-o",     cardDesc: "Stop the Server"}));
        
        jRconDiv.append(jCardContainer);
        
        // Game Section
        jRconDiv.append(MarcoUtils.template(tplRconSectionTitle, {title: "Maps"}));
        jCardContainer = $(MarcoUtils.template(tplRconCmdsContainer, {}));
        
        __MAPS.forEach(m => {
            jCardContainer.append(MarcoUtils.template(tplRconPictureCmd, m));
        });
        
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