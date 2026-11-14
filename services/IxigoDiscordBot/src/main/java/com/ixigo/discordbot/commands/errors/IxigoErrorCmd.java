package com.ixigo.discordbot.commands.errors;

import com.ixigo.library.mediators.web.interfaces.WebCommandRequest;

public class IxigoErrorCmd implements WebCommandRequest<Void> {
	private String errorMsg;

	public IxigoErrorCmd(String errorMsg) {
		super();
		this.errorMsg = errorMsg;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
}
