package com.ixigo.demmanager.commands.charts;

import java.util.Map;

import com.ixigo.demmanagercontract.models.rest.charts.RestViewData;
import com.ixigo.library.mediators.web.interfaces.WebCommandRequest;

public class CmdGetViewData implements WebCommandRequest<RestViewData> {
	private String viewName;
	private Map<String, String> allRequestParams;

	public CmdGetViewData(String viewName, Map<String, String> allRequestParams) {
		super();
		this.viewName = viewName;
		this.allRequestParams = allRequestParams;
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public Map<String, String> getAllRequestParams() {
		return allRequestParams;
	}

	public void setAllRequestParams(Map<String, String> allRequestParams) {
		this.allRequestParams = allRequestParams;
	}

}
