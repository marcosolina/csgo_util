/**
	This class will fetch the data at national level and draw the Chart
 */
class MapsPlayedChart {

	constructor(canvasId, colorPalette) {
		this.canvasId = canvasId;
		this.colorPalette = colorPalette;
		
		this.darkModeOn = true;
		this.lastResponse = {};
		
		this.chart = new CsgoChart(document.getElementById(canvasId));
	}
	
	setDarkMode(darkModeOn){
		this.darkModeOn = darkModeOn;
	}

	fetchData() {
		let url = __URLS.API_BASE + "/demparser/map/played/list";
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
		let arrData = [];
		let arrColor = [];
		let i = 0;
		this.lastResponse.maps.forEach(element => {
			arrLabels.push(element.mapName);
			arrData.push(element.count);
			i = i < this.colorPalette.length ? i : 0;
			arrColor.push(this.colorPalette[i]);
			i++;
		});
		const dataset = new CsgoChartDataset("");
		dataset.setColor(arrColor);
		dataset.setData(arrData);

		this.chart.addCsgoChartDataset(dataset);
		this.chart.setLabels(arrLabels)
		this.chart.drawChart(this.darkModeOn, "bar");
	}
}
