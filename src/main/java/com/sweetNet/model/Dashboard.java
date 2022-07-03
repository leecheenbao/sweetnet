package com.sweetNet.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "DASHBOARD")
public class Dashboard {

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	@Id
	@ApiModelProperty("前台顯示編號")
	@Column(name = "code")
	private String code;

	@ApiModelProperty("前台顯示KEY")
	@Column(name = "name")
	private String name;

	@ApiModelProperty("前台顯示內容")
	@Column(name = "content")
	private String content;

	public Dashboard() {
		super();
	}

	public Dashboard(String code, String name, String content) {
		super();

		this.code = code;
		this.name = name;
		this.content = content;
	}

}