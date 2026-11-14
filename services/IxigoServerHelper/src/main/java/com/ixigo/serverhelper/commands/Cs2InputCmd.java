package com.ixigo.serverhelper.commands;

import com.ixigo.library.mediators.web.interfaces.WebCommandRequest;

public class Cs2InputCmd implements WebCommandRequest<Void> {
	private String cs2Input;

	public Cs2InputCmd(String cs2Input) {
		super();
		this.cs2Input = cs2Input;
	}

	public String getCs2Input() {
		return cs2Input;
	}

	public void setCs2Input(String cs2Input) {
		this.cs2Input = cs2Input;
	}

}
