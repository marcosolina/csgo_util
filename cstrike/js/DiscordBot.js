var DiscordBot = ((function(DiscordBot){
	"use strict";

    let discordUsersWrapperId = "discordUsers";
    let mappedSteamUsersWrapperId = "mappedSteamUsers";
    let nonMappedSteamUsersWrapperId = "nonMappedSteamUsers";

    let steamUsers = [];

    let tplDiscord = '<div class="col-12" data-discord-id="%id%" data-discord-name=%name%>%name%</div>';
    let tplSteam = '<div class="col-12"><select class="form-control form-control-sm" id="steam-discord-select-%discordId%"><option value=""></option></select></div>';
	
	if(DiscordBot === undefined){
		DiscordBot = {};
    }
    
    DiscordBot.init = function(){
        DiscordBot.getSteamUsers();
        $("#saveMapDiscordUsers").click(DiscordBot.saveMapping);
        $("#startDiscordBot").click(function(){DiscordBot.startStopDiscordBot(true);});
        $("#stopDiscordBot").click(function(){DiscordBot.startStopDiscordBot(false);});
    }

    DiscordBot.startStopDiscordBot = function(boolStart){
        if(boolStart){
            let url = __URLS.API_BASE + "/ixigo-discord-bot/discordbot/start";
            MarcoUtils.executeAjax({type: "POST", url: url}).then(DiscordBot.receivedResponse);
        }else{
            let url = __URLS.API_BASE + "/ixigo-discord-bot/discordbot/stop";
            MarcoUtils.executeAjax({type: "DELETE", url: url}).then(DiscordBot.receivedResponse);
        }
    }

    DiscordBot.receivedResponse = function(resp){
        if(resp.status){
            MarcoUtils.showNotification({type: "success", title: "Ok", message: "Done"});
        }
    }

    DiscordBot.saveMapping = function(){
        let players = [];
        $.each($("[data-discord-id]"), function(i, e){
            let jElement = $(e);
            let player = {
                discordDetails: {},
                steamDetails : {}
            };

            let steamSelectId = "#steam-discord-select-" + jElement.data("discord-id");
            let jSteamSelect = $(steamSelectId);

            player.discordDetails.id = jElement.data("discord-id");
            player.discordDetails.name = jElement.data("discord-name");
            player.steamDetails.steamId = jSteamSelect.val();
            player.steamDetails.userName = $( steamSelectId + " option:selected" ).text();
            players.push(player);
        });

        let url = __URLS.API_BASE + "/ixigo-discord-bot/discordbot/players/mapping";
        MarcoUtils.executeAjax({type: "POST", url: url, body: {players: players}}).then(DiscordBot.receivedResponse);
    }

    DiscordBot.getSteamUsers = function(){
        let url = __URLS.API_BASE + "/csgo-round-parser-api/demparser/users";
        MarcoUtils.executeAjax({type: "GET", url: url}).then(DiscordBot.steamUsersRetrieved);
    }

    DiscordBot.steamUsersRetrieved = function(resp){
        if(resp.status){
            steamUsers = resp.users;
            steamUsers.sort((a, b) => {
                return a.userName.toUpperCase() < b.userName.toUpperCase() ? -1 : 1;
            });
            DiscordBot.getDiscordUsersWithMapping();
        }
    }

    DiscordBot.getDiscordUsersWithMapping = function(){
        let url = __URLS.API_BASE + "/ixigo-discord-bot/discordbot/mapped/players";
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
                element.steamDetails.discordId = element.discordDetails.id;
                let jSteamSelectWrapper = $(MarcoUtils.template(tplSteam, element.steamDetails));
                steamUsers.forEach(el => {
                    jSteamSelectWrapper.find("select").append(MarcoUtils.template("<option value='%steamId%'>%userName%</option>", el));
                });
                jSteamSelectWrapper.find("select").val(element.steamDetails.steamId);
                jWrapperS.append(jSteamSelectWrapper);
            });
        }
    }

	return DiscordBot;
})(DiscordBot));