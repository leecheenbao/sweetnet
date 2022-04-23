package com.sweetNet.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.alibaba.fastjson.JSONObject;

@Entity
@Table(name = "MEMBER")
public class Member implements Serializable {
	/**
	 *
	 */
	private static long serialVersionUID = 1L;

	@Id
	@Column(name = "mem_Uuid")
	private String memUuid;
	@Column(name = "mem_Mail")
	private String memMail;

	@Column(name = "mem_Pwd")
	private String memPwd;

	@Column(name = "mem_Nickname")
	private String memNickname;

	@Column(name = "mem_Dep")
	private String memDep;

	@Column(name = "mem_Sex")
	private Integer memSex;

	public Member() {
		super();
	}

	public Member(String memUuid, String memMail, String memPwd, String memNickname, String memDep,
			Integer memSex) {
		super();
		this.memUuid = memUuid;
		this.memMail = memMail;
		this.memPwd = memPwd;
		this.memNickname = memNickname;
		this.memDep = memDep;
		this.memSex = memSex;

	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public static void setSerialVersionUID(long serialVersionUID) {
		Member.serialVersionUID = serialVersionUID;
	}

	public String getMemUuid() {
		return memUuid;
	}

	public void setMemUuid(String memUuid) {
		this.memUuid = memUuid;
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

		json.put("memUuid", memUuid);
		json.put("memMail", memMail);
		json.put("memNickname", memNickname);
		json.put("memDep", memDep);
		json.put("memSex", memSex);

		return json;
	}

}