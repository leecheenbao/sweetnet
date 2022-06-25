package com.sweetNet.dto;

import java.io.Serializable;

public class LoginDTO implements Serializable {

	private String memMail;
	private String memPwd;

	public LoginDTO() {
		super();
	}

	public LoginDTO(String memMail, String memPwd) {
		super();

		this.memMail = memMail;
		this.memPwd = memPwd;
	}

	public String getMemMail() {
		return memMail;
	}

	public void setMemMail(String memMail) {
		this.memMail = memMail;
	}

	public String getMemPwd() {
		return memPwd;
	}

	public void setMemPwd(String memPwd) {
		this.memPwd = memPwd;
	}

}