package com.ixigo.demmanager.models.database;

import com.ixigo.library.dto.IxigoDto;

import lombok.experimental.FieldNameConstants;

@FieldNameConstants
public class UsersDto implements IxigoDto{

	private static final long serialVersionUID = 1L;
	private String user_name = "";
	private String steam_id = "";

	public String getUser_name(){
		return this.user_name;
	}

	public void setUser_name(String user_name){
		this.user_name = user_name;
	}


	public String getSteam_id(){
		return this.steam_id;
	}

	public UsersDto setSteam_id(String steam_id){
		this.steam_id = steam_id;
		return this;
	}


}
