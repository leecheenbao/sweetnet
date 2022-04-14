package com.sweetNet.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "MEMBER")
public class Member implements Serializable {
	/**
	 *
	 */
	private static long serialVersionUID = 1L;

	@Id
	@Column(name = "mem_uuid")
	private String mem_uuid;

	@Column(name = "mem_mail")
	private String mem_mail;

	@Column(name = "mem_pwd")
	private String mem_pwd;

	@Column(name = "mem_nickname")
	private String mem_nickname;

	@Column(name = "mem_dep")
	private String mem_dep;

	@Column(name = "mem_sex")
	private Integer mem_sex;

	public Member() {
		super();
	}

	public Member(String mem_uuid, String mem_mail, String mem_pwd, String mem_nickname, String mem_dep,
			Integer mem_sex) {
		super();
		this.mem_uuid = mem_uuid;
		this.mem_mail = mem_mail;
		this.mem_pwd = mem_pwd;
		this.mem_nickname = mem_nickname;
		this.mem_dep = mem_dep;
		this.mem_sex = mem_sex;

	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public static void setSerialVersionUID(long serialVersionUID) {
		Member.serialVersionUID = serialVersionUID;
	}

	public String getMem_uuid() {
		return mem_uuid;
	}

	public void setMem_uuid(String mem_uuid) {
		this.mem_uuid = mem_uuid;
	}

	public String getMem_mail() {
		return mem_mail;
	}

	public void setMem_mail(String mem_mail) {
		this.mem_mail = mem_mail;
	}

	public String getMem_pwd() {
		return mem_pwd;
	}

	public void setMem_pwd(String mem_pwd) {
		this.mem_pwd = mem_pwd;
	}

	public String getMem_nickname() {
		return mem_nickname;
	}

	public void setMem_nickname(String mem_nickname) {
		this.mem_nickname = mem_nickname;
	}

	public String getMem_dep() {
		return mem_dep;
	}

	public void setMem_dep(String mem_dep) {
		this.mem_dep = mem_dep;
	}

	public Integer getMem_sex() {
		return mem_sex;
	}

	public void setMem_sex(Integer mem_sex) {
		this.mem_sex = mem_sex;
	}

	public static Object from(String string, String string2, Object object) {
		// TODO Auto-generated method stub
		return null;
	}

}