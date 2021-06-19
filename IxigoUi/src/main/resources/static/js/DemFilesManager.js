var DemFilesManager = ((function(DemFilesManager){
	"use strict";

    if(DemFilesManager === undefined){
		DemFilesManager = {};
    }

    let tplCard = '<div class="card"></div>';
    let tplCardHeader = '<div class="card-header" id="card_%folderName%">' +
                            '<h2 class="mb-0">' +
                                '<button ' +
                                    'class="btn btn-link btn-card" ' +
                                    'type="button" ' +
                                    'data-toggle="collapse" ' +
                                    'data-target="#collapse_%folderName%" ' +
                                    'aria-expanded="false" ' +
                                    'aria-controls="collapse_%folderName%">' +
                                    '%folderName%' +
                                '</button>' +
                            '</h2>' +
                        '</div>';
    let tplCardBody = '<div ' +
                            'id="collapse_%folderName%" ' +
                            'class="collapse" ' +
                            'aria-labelledby="card_%folderName%" ' +
                            'data-parent="#accordionExample">' +
                            '<div class="card-body">' +
                                '<ul class="list-group">' +
                                '</ul>' +
                            '</div>' + 
                        '</div>';
    let tplMapItem = '<li class="list-group-item">'  + 
                        '<div class="row">' + 
                            '<div class="col-12 col-sm-8">' + 
                                '%mapName%' + 
                            '</div>' + 
                            '<div class="col-4 col-sm-3 col-md-3">' + 
                                '%size%' + 
                            '</div>' + 
                            '<div class="col-1">' + 
                                '<a href="%url%"' + 
                                    'target="_blank">' + 
                                    '<i ' + 
                                        'class="fa fa-cloud-download"' + 
                                        'aria-hidden="true">' + 
                                    '</i>' + 
                                '</a>' + 
                            '</div>' + 
                        '</div>' + 
                       '</li>';


    DemFilesManager.init = function(){
        DemFilesManager.getDemFiles();
    }

    DemFilesManager.getDemFiles = function(){
        let url = __URLS.DEM_MANAGER.GET_ALL_FILES;
        MarcoUtils.executeAjax({
                type: "GET",
                url: url
            }).then(DemFilesManager.demFilesRetrieved);
    }

    DemFilesManager.demFilesRetrieved = function(resp){
        if(resp.status){
            $("#accordionExample").empty()
            for(let prop in resp.files){
                let folderObj = {folderName: prop};
                let jCard = $(MarcoUtils.template(tplCard, folderObj));
                jCard.append($(MarcoUtils.template(tplCardHeader, folderObj)));
                jCard.append($(MarcoUtils.template(tplCardBody, folderObj)));
                let maps = resp.files[prop];

                let ul = jCard.find("ul");
                maps.forEach(el => {
                    el.url = __URLS.DEM_MANAGER.DOWNLOAD_FILE + "/" + el.name;
                    ul.append(MarcoUtils.template(tplMapItem, el))
                });
                $("#accordionExample").append(jCard);
            }
        }
    }

	return DemFilesManager;
})(DemFilesManager));