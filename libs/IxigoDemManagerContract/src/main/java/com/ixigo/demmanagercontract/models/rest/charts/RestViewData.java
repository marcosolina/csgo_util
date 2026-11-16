package com.ixigo.demmanagercontract.models.rest.charts;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response model returned when retrieving the list of maps that we played
 * 
 * @author Marco
 *
 */
public class RestViewData implements Serializable {
	private static final long serialVersionUID = 1L;
	@JsonProperty("view_name")
	private String viewName;
	@JsonProperty("view_data")
	private List<?> data;

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public List<?> getData() {
		return data;
	}

	public void setData(List<?> data) {
		this.data = data;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
