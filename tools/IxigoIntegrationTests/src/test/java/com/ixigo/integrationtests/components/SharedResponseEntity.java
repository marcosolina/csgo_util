package com.ixigo.integrationtests.components;

import org.springframework.http.ResponseEntity;

public class SharedResponseEntity {
	private ResponseEntity<?> sharedResp;

	public void setSharedResp(ResponseEntity<?> sharedResp) {
		this.sharedResp = sharedResp;
	}
	
	@SuppressWarnings("unchecked")
	public <T> ResponseEntity<T> getSharedResp(Class<T> clazz) {
		return (ResponseEntity<T>)sharedResp;
	}

}
