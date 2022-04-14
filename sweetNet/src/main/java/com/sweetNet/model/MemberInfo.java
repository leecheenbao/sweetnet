package com.sweetNet.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "memberinfo")
public class MemberInfo implements Serializable {
	/**
	 *
	 */
	private static long serialVersionUID = 1L;

	@Id
	@Column(name = "meminfo_uuid")
	private String meminfo_uuid;

	@Column(name = "mem_uuid")
	private String mem_uuid;

	@Column(name = "mem_name")
	private String mem_name;

	@Column(name = "mem_phone")
	private String mem_phone;

	@Column(name = "mem_birthday")
	private Date mem_birthday;

	@Column(name = "mem_age")
	private Integer mem_age;

	@Column(name = "mem_address")
	private String mem_address;

	@Column(name = "mem_height")
	private Integer mem_height;

	@Column(name = "mem_weight")
	private Integer mem_weight;

	@Column(name = "mem_edu")
	private Integer mem_edu;

	@Column(name = "mem_marry")
	private Integer mem_marry;

	@Column(name = "mem_alcohol")
	private Integer mem_alcohol;

	@Column(name = "mem_smoke")
	private Integer mem_smoke;

	@Column(name = "mem_income")
	private Integer mem_income;

	@Column(name = "mem_assets")
	private Integer mem_assets;

	@Column(name = "mem_isvip")
	private Integer mem_isvip;

	@Column(name = "mem_rdate")
	private Date mem_rdate;

	@Column(name = "mem_lgd")
	private Integer mem_lgd;

	@Column(name = "mem_sta")
	private Integer mem_sta;

	public MemberInfo() {
		super();
	}

	public MemberInfo(String meminfo_uuid, String mem_uuid, String mem_name, String mem_phone, Date mem_birthday,
			String mem_address, Integer mem_age, Integer mem_height, Integer mem_weight, Integer mem_edu,
			Integer mem_marry, Integer mem_alcohol, Integer mem_smoke, Integer mem_income, Integer mem_assets,
			Integer mem_isvip, Date mem_rdate, Integer mem_lgd, Integer mem_sta) {
		super();
		this.meminfo_uuid = meminfo_uuid;
		this.mem_uuid = mem_uuid;
		this.mem_name = mem_name;
		this.mem_phone = mem_phone;
		this.mem_birthday = mem_birthday;

		this.mem_address = mem_address;
		this.mem_age = mem_age;
		this.mem_height = mem_height;
		this.mem_weight = mem_weight;
		this.mem_edu = mem_edu;

		this.mem_marry = mem_marry;
		this.mem_alcohol = mem_alcohol;
		this.mem_smoke = mem_smoke;
		this.mem_income = mem_income;
		this.mem_assets = mem_assets;

		this.mem_isvip = mem_isvip;
		this.mem_rdate = mem_rdate;
		this.mem_lgd = mem_lgd;
		this.mem_sta = mem_sta;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public static void setSerialVersionUID(long serialVersionUID) {
		MemberInfo.serialVersionUID = serialVersionUID;
	}

	public String getMeminfo_uuid() {
		return meminfo_uuid;
	}

	public void setMeminfo_uuid(String meminfo_uuid) {
		this.meminfo_uuid = meminfo_uuid;
	}

	public String getMem_uuid() {
		return mem_uuid;
	}

	public void setMem_uuid(String mem_uuid) {
		this.mem_uuid = mem_uuid;
	}

	public String getMem_name() {
		return mem_name;
	}

	public void setMem_name(String mem_name) {
		this.mem_name = mem_name;
	}

	public String getMem_phone() {
		return mem_phone;
	}

	public void setMem_phone(String mem_phone) {
		this.mem_phone = mem_phone;
	}

	public Date getMem_birthday() {
		return mem_birthday;
	}

	public void setMem_birthday(Date mem_birthday) {
		this.mem_birthday = mem_birthday;
	}

	public Integer getMem_age() {
		return mem_age;
	}

	public void setMem_age(Integer mem_age) {
		this.mem_age = mem_age;
	}

	public String getMem_address() {
		return mem_address;
	}

	public void setMem_address(String mem_address) {
		this.mem_address = mem_address;
	}

	public Integer getMem_height() {
		return mem_height;
	}

	public void setMem_height(Integer mem_height) {
		this.mem_height = mem_height;
	}

	public Integer getMem_weight() {
		return mem_weight;
	}

	public void setMem_weight(Integer mem_weight) {
		this.mem_weight = mem_weight;
	}

	public Integer getMem_edu() {
		return mem_edu;
	}

	public void setMem_edu(Integer mem_edu) {
		this.mem_edu = mem_edu;
	}

	public Integer getMem_marry() {
		return mem_marry;
	}

	public void setMem_marry(Integer mem_marry) {
		this.mem_marry = mem_marry;
	}

	public Integer getMem_alcohol() {
		return mem_alcohol;
	}

	public void setMem_alcohol(Integer mem_alcohol) {
		this.mem_alcohol = mem_alcohol;
	}

	public Integer getMem_smoke() {
		return mem_smoke;
	}

	public void setMem_smoke(Integer mem_smoke) {
		this.mem_smoke = mem_smoke;
	}

	public Integer getMem_income() {
		return mem_income;
	}

	public void setMem_income(Integer mem_income) {
		this.mem_income = mem_income;
	}

	public Integer getMem_assets() {
		return mem_assets;
	}

	public void setMem_assets(Integer mem_assets) {
		this.mem_assets = mem_assets;
	}

	public Integer getMem_isvip() {
		return mem_isvip;
	}

	public void setMem_isvip(Integer mem_isvip) {
		this.mem_isvip = mem_isvip;
	}

	public Date getMem_rdate() {
		return mem_rdate;
	}

	public void setMem_rdate(Date mem_rdate) {
		this.mem_rdate = mem_rdate;
	}

	public Integer getMem_lgd() {
		return mem_lgd;
	}

	public void setMem_lgd(Integer mem_lgd) {
		this.mem_lgd = mem_lgd;
	}

	public Integer getMem_sta() {
		return mem_sta;
	}

	public void setMem_sta(Integer mem_sta) {
		this.mem_sta = mem_sta;
	}

}