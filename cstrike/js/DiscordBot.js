var DiscordBot = ((function(DiscordBot){
	"use strict";

    let discordUsersWrapperId = "discordUsers";
    let mappedSteamUsersWrapperId = "mappedSteamUsers";
    let nonMappedSteamUsersWrapperId = "nonMappedSteamUsers";

    let tplDiscord = '<div class="col-12" data-discord-id="%id%">%name%</div>';
    let tplSteam = '<select class="form-control"><option value=""></option></select>';
	
	if(DiscordBot === undefined){
		DiscordBot = {};
    }
    
    DiscordBot.init = function(){
    }

    DiscordBot.getDiscordUsersWithMapping = function(){
        let url = __URLS.API_BASE + "/ixigo-discord-bot/discordbot/players";
        MarcoUtils.executeAjax({type: "GET", url: url}).then(DiscordBot.discordUsersWithMappingRetrieved);
    }

    DiscordBot.discordUsersWithMappingRetrieved = function(resp){
        if(resp.status){
            let jWrapperD = $("#" + discordUsersWrapperId);
            let jWrapperS = $("#" + mappedSteamUsersWrapperId);
            jWrapperD.empty();
            jWrapperS.empty();

            resp.players.forEach(element => {
                jWrapperD.append(MarcoUtils.template(tplDiscord, element.discordDetails));
                jWrapperS.append(MarcoUtils.template(tplSteam, element.steamDetails));
            });
        }
    }

	return DiscordBot;
})(DiscordBot));