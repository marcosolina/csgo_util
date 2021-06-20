var DiscordBot = ((function(DiscordBot){
	"use strict";

    let discordUsersWrapperId = "discordUsers";
    let mappedSteamUsersWrapperId = "mappedSteamUsers";
    let dpRoundToConsiderId = "botRoundToConsider";

    let steamUsers = [];

    let tplDiscord = '<div class="col-12" data-discord-id="%id%" data-discord-name=%name%>' +
                        '<input class="form-control form-control-sm" type="text" value="%name%" readonly></div>';
    let tplSteam = '<div class="col-12"><select class="form-control form-control-sm" id="steam-discord-select-%discordId%"><option value=""></option></select></div>';
	
	if(DiscordBot === undefined){
		DiscordBot = {};
    }
    
    DiscordBot.init = function(){
        DiscordBot.getSteamUsers();
        $("#saveMapDiscordUsers").click(DiscordBot.saveMapping);
        $("#startDiscordBot").click(function(){DiscordBot.startStopDiscordBot(true);});
        $("#stopDiscordBot").click(function(){DiscordBot.startStopDiscordBot(false);});

        let jSelectRoundsToConsider = $("#" + dpRoundToConsiderId);
        for(let i = 1; i < 100; i++){
            jSelectRoundsToConsider.append(MarcoUtils.template("<option value='%number%'>%number%</option>", {number: i}));
        }
        DiscordBot.getRoundsToConsider();
        jSelectRoundsToConsider.change(DiscordBot.changeRoundsToConsider);
    }

    DiscordBot.changeRoundsToConsider = function(){
        let url = __URLS.API_BASE + "/ixigo-discord-bot/discordbot/config";
        MarcoUtils.executeAjax({
                type: "PUT",
                url: url,
                showLoading: true,
                body: {
                    configKey: "ROUNDS_TO_CONSIDER_FOR_TEAM_CREATION",
                    configVal: $(this).val()
                }
            }).then(function(resp){
                if(resp.status){
                    MarcoUtils.showNotification({type: "success", title: "Ok", message: "Configuration saved"});
                }
            });
    }

    DiscordBot.getRoundsToConsider = function(){
        let url = __URLS.API_BASE + "/ixigo-discord-bot/discordbot/config?config=ROUNDS_TO_CONSIDER_FOR_TEAM_CREATION";
        MarcoUtils.executeAjax({
                type: "GET",
                url: url
            }).then(function(resp){
                if(resp.status){
                    $("#" + dpRoundToConsiderId).val(resp.config.configVal);
                }
            });
    }

    DiscordBot.startStopDiscordBot = function(boolStart){
        if(boolStart){
            let url = __URLS.API_BASE + "/ixigo-discord-bot/discordbot/start";
            MarcoUtils.executeAjax({type: "POST", url: url, showLoading: true}).then(function(resp){
                DiscordBot.receivedResponse(resp);
                if(resp.status){
                    DiscordBot.getSteamUsers();
                }
            });
        }else{
            let url = __URLS.API_BASE + "/ixigo-discord-bot/discordbot/stop";
            MarcoUtils.executeAjax({type: "DELETE", url: url, showLoading: true}).then(DiscordBot.receivedResponse);
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

        let url = __URLS.DISCORD_BOT.POST_PLAYERS_MAPPING;
        MarcoUtils.executeAjax({type: "POST", url: url, showLoading: true, body: {players: players}}).then(DiscordBot.receivedResponse);
    }

    DiscordBot.getSteamUsers = function(){
        let url = __URLS.DEM_MANAGER.GET_PLAYERS;
        MarcoUtils.executeAjax({type: "GET", url: url, showLoading: true})
            .then(DiscordBot.steamUsersRetrieved)
            .fail(function(){
                MarcoUtils.showNotification({type: "error", title: "Sorry", message: "The bot service is down"});
            });
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
        let url = __URLS.DISCORD_BOT.GET_MAPPED_PLAYERS;
        MarcoUtils.executeAjax({type: "GET", url: url, showLoading: true}).then(DiscordBot.discordUsersWithMappingRetrieved);
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