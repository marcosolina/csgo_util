var PlayersManager = ((function(PlayersManager){
	"use strict";

	if(PlayersManager === undefined){
		PlayersManager = {};
	}

    let timeOut;
    let teamTerrorists;

    PlayersManager.init = function(){
        PlayersManager.getUsersList();
        PlayersManager.initMinPercSelect();
        $("#selectRoundToConsider").change(PlayersManager.createTeams);
        $("#selectScoreType").change(PlayersManager.createTeams);
        $('input[name=partitionType]').change(PlayersManager.createTeams);
        $('#penaltyWeigth').change(PlayersManager.createTeams);
        $("#selectMinPercPlayed").change(PlayersManager.createTeams);
        $("#partitionTypeIxigo").prop("checked", true);
        $("#setPlayerOnServer").hide();
        $("#setPlayerOnServer").click(PlayersManager.setPlayersOnServer);
        PlayersManager.getAvailableGames();
        PlayersManager.getScoreTypes();
    }

    PlayersManager.setPlayersOnServer = function(){
        if(teamTerrorists == undefined){
            return;
        }

        let steamIds = "";
        teamTerrorists.members.forEach(e => {
            steamIds += "\"" + e.steamID + "\" ";
            });

        let cmd = "sm_move_players " + steamIds + "dummy";
        $("#rconCmd").val(cmd);
        $("#sendRcon").click();
    }

    PlayersManager.getAvailableGames = function(){
        MarcoUtils.executeAjax({
            type: "GET",
            url: __URLS.DEM_MANAGER.GET_ALL_FILES,
        }).then(PlayersManager.availableGamesRetrieved);
    }

    PlayersManager.availableGamesRetrieved = function(resp){

        if(resp && resp.status){
            let strTmpl = '<option value="%count%">%count%</option>';
            let jSelect = $("#selectRoundToConsider");
            jSelect.empty();
            let gamesList = [];
            for (const day in resp.files) {
              let gamesOfTheDay = resp.files[day];
              gamesOfTheDay.forEach(element => gamesList.push(element.name));
            }
            
            let numberOfGames = gameList.length;
            for(let i = 1; i <= numberOfGames; i++){
                jSelect.append(MarcoUtils.template(strTmpl, {count: i}));
            }

            if(numberOfGames > 49){
                jSelect.val(50);
            }else{
                jSelect.val(numberOfGames);
            }
        }
        
    }

    PlayersManager.initMinPercSelect = function(){
        let strTmpl = '<option value="%key%">%val%</option>';
        let jSelect = $("#selectMinPercPlayed");
        for(let i = 0; i < 101; i++){
            jSelect.append(MarcoUtils.template(strTmpl, {key: i/100, val: i + " %"}));
        }
		jSelect.val(0.9);
    }
    

    PlayersManager.getScoreTypes = function(){
        MarcoUtils.executeAjax({
            type: "GET",
            url: __URLS.DEM_MANAGER.GET_SCORES_TYPES,
        }).then(PlayersManager.scoreTypesRetrieved);
    }

    PlayersManager.scoreTypesRetrieved = function(resp){

        if(resp && resp.status){
            let strTmpl = '<option value="%key%">%val%</option>';
            let jSelect = $("#selectScoreType");
            jSelect.empty();

            let values = [];
            for(let prop in resp.types){
                values.push({key: prop, val: resp.types[prop]});
            }

            values.sort((o1, o2) => {
                return o1.val.toLowerCase() < o2.val.toLowerCase() ? -1 : 1;
            });

            values.forEach(el => jSelect.append(MarcoUtils.template(strTmpl, el)));
            jSelect.val("HLTV");
        }
        
    }

    PlayersManager.getUsersList = function(){
        MarcoUtils.executeAjax({
            type: "GET",
            url: __URLS.DEM_MANAGER.GET_PLAYERS,
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
            $("#setPlayerOnServer").fadeOut();
            return;
        }
        
        let queryParam = "";
        steamIds.forEach(v => {queryParam += "," + v;});
        let gamesToConsider = $("#selectRoundToConsider").val()
        
        let url = __URLS.PLAYERS_MANAGER.GET_TEAMS;
        url += "?";
        url += "&teamsCounter=2";
        url += "&gamesCounter=" + gamesToConsider;
        url += "&usersIDs=" + queryParam.substring(1);
        url += "&partitionType=" + $('input[name=partitionType]:checked').val();
        url += "&penaltyWeigth=" + $('#penaltyWeigth').val();
        url += "&partitionScore=" + $("#selectScoreType").val();
        url += "&minPercPlayed=" + $("#selectMinPercPlayed").val();

        MarcoUtils.executeAjax({
            type: "GET",
            url: url,
            showLoading: true,
        }).then(PlayersManager.teamsCreated);
    }

    PlayersManager.teamsCreated = function(resp){

        if(resp && resp.status){
            var strTmpl =   '<li class="list-group-item d-flex justify-content-between align-items-center">' +
                                '%userName%' +
                                '<span class="badge badge-pill %badgType%">%teamSplitScore%</span>' +
                            '</li>';

            teamTerrorists = resp.teams[0];
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
                t.badgType = t.teamSplitScore == t.originalTeamSplitScore ? "badge-primary" : "badge-danger";
                t.teamSplitScore = t.teamSplitScore.toFixed(2);
                t.teamSplitScore = t.teamSplitScore.length < 5 ? "0" + t.teamSplitScore : t.teamSplitScore;
                jTerroristList.append(MarcoUtils.template(strTmpl, t));
            });
            arrCt.forEach(ct => {
                ct.badgType = ct.teamSplitScore == ct.originalTeamSplitScore ? "badge-primary" : "badge-danger";
                ct.teamSplitScore = ct.teamSplitScore.toFixed(2);
                ct.teamSplitScore = ct.teamSplitScore.length < 5 ? "0" + ct.teamSplitScore : ct.teamSplitScore;
                jCtList.append(MarcoUtils.template(strTmpl, ct));
            });
            $("#setPlayerOnServer").fadeIn();
        }
    }

    function sortUserByScore(u1, u2) {
        return u1.teamSplitScore < u2.teamSplitScore ? 1 : -1;
    }

	return PlayersManager;
})(PlayersManager));
