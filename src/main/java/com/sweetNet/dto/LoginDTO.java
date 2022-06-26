package com.sweetNet.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

public class LoginDTO implements Serializable {

//	@Email(message = "帳號必須是Email 格式")
	@NotBlank(message = "帳號不可為空")
	private String memMail;

//	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[\\w]{6,16}$", message = "密碼必須為長度6~16位碼大小寫英文加數字")
	@NotBlank(message = "密碼不可為空")
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