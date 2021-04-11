/**
	This class will fetch the data at national level and draw the Chart
 */
class PlayerScoreChart {

	constructor(canvasId, checkBoxesContainerId, colorPalette, countGames) {
		this.canvasId = canvasId;
		this.checkBoxesContainerId = checkBoxesContainerId;
		this.colorPalette = colorPalette;
		
		this.darkModeOn = true;
		this.lastResponse = {};
		this.idSelctRoundToConsider = "chartRoundToConsider";
		this.idSelectSCoreType = "chartSelectScoreType";

		this.chart = new CsgoChart(document.getElementById(canvasId));

		this.countGames = countGames;
		this.countGamesSelected = 0;

		this.mapDataType = {
			"FF": "teamKillFriendlyFire",
			"ASSISTS": "assists",
			"BD": "bombDefused",
			"SCORE": "score",
			"APR": "assistsPerRound",
			"MVP": "mostValuablePlayer",
			"DPR": "deathPerRound",
			"DEATHS": "deaths",
			"HS": "headShots",
			"KPR": "killPerRound",
			"BP": "bombPLanted",
			"ADR": "averageDamagePerRound",
			"TDA": "totalDamageArmor",
			"_5K": "fiveKills",
			"_3K": "threeKills",
			"FIRE": "fireThrownCount",
			"KDR": "killDeathRation",
			"_1K": "oneKill",
			"TDH": "totalDamageHealth",
			"SMOKES": "smokesThrownCount",
			"FLASHES": "flashesThrownCount",
			"HSP": "headShotsPercentage",
			"_1V3": "oneVersusThree",
			"_1V2": "oneVersusTwo",
			"EK": "entryKill",
			"_1V1": "oneVersusOne",
			"HED": "highExplosiveDamage",
			"HLTV": "halfLifeTelevisionRating",
			"RWS": "roundWinShare",
			"TD": "tradeDeath",
			"KILLS": "kills",
			"_4K": "fourKills",
			"TK": "tradeKill",
			"_2K": "twoKills",
			"_1V5": "oneVersusFive",
			"_1V4": "oneVersusFour",
			"FD": "fireDamage",
			"GRENADES": "grenadesThrownCount"
		};
	}
	
	setDarkMode(darkModeOn){
		this.darkModeOn = darkModeOn;
	}

	/**
		Adds the checkboxes used to select/de-select the data to display
	 */
	addCheckboxes(usersArr, scoreType) {

		/*
		* Craeting the list of the users
		*/
		let template = '<div class="col-12 col-sm-6 col-md-4 col-lg-3 col-xl-3">' +
							'<div class="custom-control custom-switch">' +
								'<input type="checkbox" class="custom-control-input" id="check-user-%steamId%" data-steamid="%steamId%">' +
								'<label class="custom-control-label switch-label" style="color: %color%" for="check-user-%steamId%"" id="label-check-user-%steamId%""><b>%userName%</b></label>' +
							'</div>' +
						'</div>';
		let jContainer = $("#" + this.checkBoxesContainerId);
		jContainer.empty();

		usersArr.forEach(function(el, index){
			el.color = this.colorPalette[index];
			jContainer.append(MarcoUtils.template(template, el));
		}.bind(this));

		jContainer.find("input").change(this.fetchData.bind(this));

		/*
		* Craeting the dropdown used to select the number
		* or rounds to consider
		*/
		let jSelect = $("#" + this.idSelctRoundToConsider);
		jSelect.empty();
		let strTmpl = '<option value="%count%">%count%</option>';
		for(let i = 1; i <= this.countGames; i++){
			jSelect.append(MarcoUtils.template(strTmpl, {count: i}));
		}

		if(this.countGames > 20){
			jSelect.val(20);
		}else{
			jSelect.val(this.countGames);
		}

		this.countGamesSelected = jSelect.val();

		jSelect.change(this.fetchData.bind(this));

		/*
		* Craeting the dropdown used to select the data
		* type to display in the chart
		*/
		jSelect = $("#" + this.idSelectSCoreType);
		strTmpl = '<option value="%key%">%val%</option>';
		let values = [];
		for(let prop in scoreType){
			values.push({key: prop, val: scoreType[prop]});
		}

		values.sort((o1, o2) => {
			return o1.val.toLowerCase() < o2.val.toLowerCase() ? -1 : 1;
		});

		values.forEach(el => jSelect.append(MarcoUtils.template(strTmpl, el)));
		jSelect.val("RWS");
		jSelect.change(this.drawChart.bind(this));
	}

	fetchData() {
		let activeCheckboses = $("#chartRowPlayers input:checked");
		if(activeCheckboses.length == 0){
			this.drawChart();
			return;
		}
		let usersId = "";
		$.each(activeCheckboses, function(index, el){
			usersId += "," + $(el).data("steamid");
		});

		this.countGamesSelected = $("#" + this.idSelctRoundToConsider).val();

		let url = __URLS.API_BASE + "/demparser/users/last/" + this.countGamesSelected + "/games/scores?usersIDs=" + usersId.substr(1);
		MarcoUtils.executeAjax({type: "GET", url: url}).then(this.dataRetrieved.bind(this));
	}

	dataRetrieved(response) {
		if (response.status) {
			this.lastResponse = response;
			this.drawChart();
		}
	}

	drawChart() {
		this.chart.clearDataSets();
		let arrLabels = [];
		for(let i = 1; i <= this.countGamesSelected; i++){
			arrLabels.push(i);
		}
		this.chart.setLabels(arrLabels);
		
		for (let steamId in this.lastResponse.usersScores) {
			if($("#check-user-" + steamId).prop("checked")){
				var userDataList = this.lastResponse.usersScores[steamId];
				userDataList.sort((a, b) => a.playedOn < b.playedOn ? -1 : 1);
				var arrData = [];
				userDataList.forEach(el => {
					arrData.push(el.usersStats[0][this.mapDataType[$("#" + this.idSelectSCoreType).val()]]);
				});

				const dataset = new CsgoChartDataset(userDataList[0].usersStats[0].userName);
				dataset.setData(arrData);
				dataset.setColor($("#label-check-user-" + steamId).css("color"));
				this.chart.addCsgoChartDataset(dataset);
			}
		}
		
		this.chart.drawChart(this.darkModeOn);
	}
}
