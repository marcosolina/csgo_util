var PlayersManager = ((function(PlayersManager){
	"use strict";

	if(PlayersManager === undefined){
		PlayersManager = {};
	}

    let usersForGame = {};
    let timeOut;

    PlayersManager.init = function(){
        PlayersManager.getUsersList();
        $("#selectRoundToConsider").change(PlayersManager.createTeams);
        PlayersManager.getAvailableGames();
    }

    PlayersManager.getAvailableGames = function(){
        MarcoUtils.executeAjax({
            type: "GET",
            url: "https://marco.selfip.net/demparser/games/list",
        }).then(PlayersManager.availableGamesRetrieved);
    }

    PlayersManager.availableGamesRetrieved = function(resp){

        if(resp && resp.status){
            let strTmpl =   '<option value="%count%">%count%</option>';
            let jSelect = $("#selectRoundToConsider");
            jSelect.empty();
            for(let i = 1; i <= resp.availableGames.length; i++){
                jSelect.append(MarcoUtils.template(strTmpl, {count: i}));
            }
        }
        
    }

    PlayersManager.getUsersList = function(){
        MarcoUtils.executeAjax({
            type: "GET",
            url: "https://marco.selfip.net/demparser/users",
        }).then(PlayersManager.usersListRetrieved);
    }

    PlayersManager.usersListRetrieved = function(resp){
        let jWrapper = $("#usersListWrapper");
        let strTmpl =   '<div class="col-12 col-sm-6 col-md-12">' +
                            '<div class="custom-control custom-switch">' +
                                '<input type="checkbox" class="custom-control-input" id="steamId-%steamId%" data-steam-id="%steamId%">' +
                                '<label id="label-steamId-%steamId%" class="custom-control-label switch-label" for="steamId-%steamId%">%userName%</label>' +
                            '</div>' +
                        '</div>';
        if(resp && resp.status){
            jWrapper.empty();
            resp.users.sort( (a, b) => {
                let nameA = a.userName.toUpperCase();
                let nameB = b.userName.toUpperCase();
                if (nameA < nameB) {
                    return -1;
                  }
                  if (nameA > nameB) {
                    return 1;
                  }
                  return 0;
            });
            resp.users.forEach(element => {
                jWrapper.append(MarcoUtils.template(strTmpl, element));
            });
            jWrapper.find("input").change(PlayersManager.addUserForGame);
        }
    }

    PlayersManager.addUserForGame = function(){
        
        clearTimeout(timeOut);
        timeOut = setTimeout(PlayersManager.createTeams, 1000);

    }

    PlayersManager.createTeams = function(){
        let steamIds = [];
        
        $("#usersListWrapper input").each(function(index, checkBoxTag){
            let jCheckbox = $(checkBoxTag);
            if(jCheckbox.prop("checked")){
                steamIds.push(jCheckbox.data("steam-id"));
            }
        });
        
        if(steamIds.length < 3){
            $("#terroristPlayers ul").empty();
            $("#ctPlayers ul").empty();

            $("#terroristPlayers .badge").html("");
            $("#ctPlayers .badge").html("");
            return;
        }
        
        let queryParam = "";
        steamIds.forEach(v => {queryParam += "," + v;});
        let gamesToConsider = $("#selectRoundToConsider").val()
        let url = "https://marco.selfip.net/demparser/2/using/last/" + gamesToConsider + "/games/scores?usersIDs=" + queryParam.substring(1);

        MarcoUtils.executeAjax({
            type: "GET",
            url: url,
            showLoading: true,
        }).then(PlayersManager.teamsCreate);
    }

    PlayersManager.teamsCreate = function(resp){

        if(resp && resp.status){
            var strTmpl =   '<li class="list-group-item d-flex justify-content-between align-items-center">' +
                                '%userName%' +
                                '<span class="badge badge-primary badge-pill">%avgScore%</span>' +
                            '</li>';

            let teamTerrorists = resp.teams[0];
            let teamCt = resp.teams[1];
            let arrTerrorist = teamTerrorists.members.sort(sortUserByScore);
            let arrCt = teamCt.members.sort(sortUserByScore);

            let jTerroristList = $("#terroristPlayers ul");
            let jCtList = $("#ctPlayers ul");

            $("#terroristPlayers .badge").html(teamTerrorists.teamScore);
            $("#ctPlayers .badge").html(teamCt.teamScore);

            jTerroristList.empty();
            jCtList.empty();

            arrTerrorist.forEach(t => {
                jTerroristList.append(MarcoUtils.template(strTmpl, t));
            });
            arrCt.forEach(ct => {
                jCtList.append(MarcoUtils.template(strTmpl, ct));
            });
        }
    }

    function sortUserByScore(u1, u2) {
        return u1.avgScore < u2.avgScore ? 1 : -1;
    }

	return PlayersManager;
})(PlayersManager));