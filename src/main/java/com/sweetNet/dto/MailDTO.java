package com.sweetNet.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;

public class MailDTO {

	@ApiModelProperty("電子信箱")
	@Email
	@NotNull
	private String mail;

	@ApiModelProperty("稱呼")
	@NotNull
	private String name;

	@ApiModelProperty("信件內容")
	@NotNull
	private String content;

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}