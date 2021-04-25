var ChartsManager = ((function(ChartsManager){
	"use strict";
	
	if(ChartsManager === undefined){
		ChartsManager = {};
    }

	var players = [];
	var gamesList = [];
	var colorPalette = [];
	var scoreTypes = {};
	colorPalette.push("rgb( 0, 54, 255 , 1)");// Blu
	colorPalette.push("rgb( 255, 0, 223 , 1)");// Rosa
	colorPalette.push("rgb( 15, 212, 192, 1)");// Verde Acqua
	colorPalette.push("rgb( 255, 123, 0, 1)");// Arancione
	colorPalette.push("rgb( 174, 67, 67, 1)");// Marrone
	colorPalette.push("rgb( 67, 141, 133 , 1)");// Verde Scuro
	colorPalette.push("rgb( 186, 124, 66, 1)");// Marrone Chiaro
	colorPalette.push("rgb( 182, 176, 53, 1)");// Oliva
	colorPalette.push("rgb( 79, 255, 0, 1)");// Verde evidenziatore
	colorPalette.push("rgb( 255, 169, 90, 1)");// Arancio chiaro
	colorPalette.push("rgb( 222, 212, 22, 1)");// Senape
	colorPalette.push("rgb( 90, 163, 57, 1)"); // Verde
	colorPalette.push("rgb( 183, 0, 255 , 1)");// Viola
	colorPalette.push("rgb( 55, 98, 36, 1)");// Verde molto scuro
	colorPalette.push("rgb( 255, 104, 104, 1)");// Rossino
	colorPalette.push("rgb( 119, 148, 255 , 1)");// Azzurro Violetto
	colorPalette.push("rgb( 48, 68, 144 , 1)");// Blu scuro
	colorPalette.push("rgb( 255, 255, 255, 1)");// Bianco
	colorPalette.push("rgb( 101, 46, 122 , 1)");// Viola Scuro
	colorPalette.push("rgb( 255, 0, 0, 1)");// Rosso
	//colorPalette.push("rgb( 0, 0, 0, 1)");// Nero
	
	var charts = {}
    
    ChartsManager.init = function(){
		ChartsManager.retrievePlayerList()
			.then(ChartsManager.getAvailableGames)
			.then(ChartsManager.getScoreTypes)
			.then(ChartsManager.initChartsObjects);
    }

	ChartsManager.initChartsObjects = function(){
		charts.playeScoreChart = new PlayerScoreChart("playerScoresChart", "chartRowPlayers", colorPalette, gamesList.length);
		charts.playeScoreChart.addCheckboxes(players, scoreTypes);
		charts.playeScoreChart.fetchData();

		charts.mapsPlayed = new MapsPlayedChart("mapsPlayedChart", colorPalette);
		charts.mapsPlayed.fetchData();
	}

	ChartsManager.getScoreTypes = function(){
		let deferred = $.Deferred();
		MarcoUtils.executeAjax({
            type: "GET",
            url: __URLS.API_BASE + "/demparser/scorestype",
        }).then(function(resp){
			ChartsManager.getScoreTypesRetrieved(resp);
			deferred.resolve();
		});
		return deferred.promise();

    }

	ChartsManager.getScoreTypesRetrieved = function(resp){
		if(resp.status){
			scoreTypes = resp.types;
		}
	}

	ChartsManager.getAvailableGames = function(){
		let deferred = $.Deferred();
		MarcoUtils.executeAjax({
            type: "GET",
            url: __URLS.API_BASE + "/demparser/games/list",
        }).then(function(resp){
			ChartsManager.getAvailableGamesRetrieved(resp);
			deferred.resolve();
		});
		return deferred.promise();

    }

	ChartsManager.getAvailableGamesRetrieved = function(resp){
		if(resp.status){
			gamesList = resp.availableGames;
		}
	}

	ChartsManager.retrievePlayerList = function(){
		let deferred = $.Deferred();
		MarcoUtils.executeAjax({
            type: "GET",
            url: __URLS.API_BASE + "/demparser/users",
        }).then(function(resp){
			ChartsManager.playersRetrieved(resp);
			deferred.resolve();
		});
		return deferred.promise();
	}

	ChartsManager.playersRetrieved = function(resp){
		if(resp.status){
			players = resp.users;
			players.sort( (a, b) => {
                let nameA = a.userName.toUpperCase();
                let nameB = b.userName.toUpperCase();
                return nameA < nameB ? -1 : 1;
            });
		}
	}

    ChartsManager.loadData = function(){
		charts.playeScoreChart.fetchData();
	}

	return ChartsManager;
})(ChartsManager));