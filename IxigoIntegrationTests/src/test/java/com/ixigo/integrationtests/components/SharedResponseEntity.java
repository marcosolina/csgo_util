package com.ixigo.integrationtests.components;

import org.springframework.http.ResponseEntity;

public class SharedResponseEntity {
	private ResponseEntity<?> sharedResp;

	public ResponseEntity<?> getSharedResp() {
		return sharedResp;
	}

	public void setSharedResp(ResponseEntity<?> sharedResp) {
		this.sharedResp = sharedResp;
	}

}
