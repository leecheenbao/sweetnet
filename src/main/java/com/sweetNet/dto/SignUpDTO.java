package com.sweetNet.dto;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;

public class SignUpDTO implements Serializable {

	private String memMail;

	private String memPwd;

	private String memNickname;

	private String memDep;

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

	public JSONObject toJson() {
		JSONObject json = new JSONObject();

		json.put("memMail", memMail);
		json.put("memNickname", memNickname);
		json.put("memDep", memDep);
		json.put("memSex", memSex);
		return json;
	}

}