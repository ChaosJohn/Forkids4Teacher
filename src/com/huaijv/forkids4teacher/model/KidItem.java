package com.huaijv.forkids4teacher.model;

import java.io.Serializable;

public class KidItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String avatarUrlString = null;
	private String nameString = null;
	private String phoneString = null;

	public KidItem() {
	}

	public KidItem(String avatarUrlString, String nameString, String phoneString) {
		this.avatarUrlString = avatarUrlString;
		this.nameString = nameString;
		this.phoneString = phoneString;
	}

	public String getAvatarUrlString() {
		return avatarUrlString;
	}

	public void setAvatarUrlString(String avatarUrlString) {
		this.avatarUrlString = avatarUrlString;
	}

	public String getNameString() {
		return nameString;
	}

	public void setNameString(String nameString) {
		this.nameString = nameString;
	}

	public String getPhoneString() {
		return phoneString;
	}

	public void setPhoneString(String phoneString) {
		this.phoneString = phoneString;
	}

}
