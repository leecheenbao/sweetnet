package com.sweetNet.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

import io.swagger.annotations.ApiModelProperty;

public class SignUpDTO {

	@ApiModelProperty("電子信箱")
	@Email
	private String memMail;

	@ApiModelProperty("密碼")
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[\\w]{6,16}$", message = "密碼必須為長度6~16位碼大小寫英文加數字")
	private String memPwd;

	@ApiModelProperty("暱稱")
	private String memNickname;

	@ApiModelProperty("一句話描述自己")
	private String memDep;

	@ApiModelProperty("性別")
	private Integer memSex;

	public SignUpDTO() {
		super();
	}

	public SignUpDTO(String memMail, String memPwd, String memNickname, String memDep, Integer memSex) {
		super();
		this.memMail = memMail;
		this.memPwd = memPwd;
		this.memNickname = memNickname;
		this.memDep = memDep;
		this.memSex = memSex;

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

	public String getMemNickname() {
		return memNickname;
	}

	public void setMemNickname(String memNickname) {
		this.memNickname = memNickname;
	}

	public String getMemDep() {
		return memDep;
	}

	public void setMemDep(String memDep) {
		this.memDep = memDep;
	}

	public Integer getMemSex() {
		return memSex;
	}

	public void setMemSex(Integer memSex) {
		this.memSex = memSex;
	}

}