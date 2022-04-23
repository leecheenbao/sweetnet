package com.sweetNet.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.alibaba.fastjson.JSONObject;

@Entity
@Table(name = "memberinfo")
public class MemberInfo implements Serializable {
	/**
	 *
	 */
	private static long serialVersionUID = 1L;
	@Id
	@Column(name = "mem_Uuid")
	private String memUuid;

	@Column(name = "mem_Name")
	private String memName;

	@Column(name = "mem_Phone")
	private String memPhone;

	@Column(name = "mem_Birthday")
	private String memBirthday;

	@Column(name = "mem_Age")
	private Integer memAge;

	@Column(name = "mem_Address")
	private String memAddress;

	@Column(name = "mem_Height")
	private Integer memHeight;

	@Column(name = "mem_Weight")
	private Integer memWeight;

	@Column(name = "mem_Edu")
	private Integer memEdu;

	@Column(name = "mem_Marry")
	private Integer memMarry;

	@Column(name = "mem_Alcohol")
	private Integer memAlcohol;

	@Column(name = "mem_Smoke")
	private Integer memSmoke;

	@Column(name = "mem_Income")
	private Integer memIncome;

	@Column(name = "mem_Assets")
	private Integer memAssets;

	@Column(name = "mem_Isvip")
	private Integer memIsvip;

	@Column(name = "mem_Rdate")
	private String memRdate;

	@Column(name = "mem_Lgd")
	private Integer memLgd;

	@Column(name = "mem_Sta")
	private Integer memSta;

	public MemberInfo() {
		super();
	}

	public MemberInfo(String memUuid, String memName, String memPhone, String memBirthday, String memAddress,
			Integer memAge, Integer memHeight, Integer memWeight, Integer memEdu, Integer memMarry,
			Integer memAlcohol, Integer memSmoke, Integer memIncome, Integer memAssets, Integer memIsvip,
			String memRdate, Integer memLgd, Integer memSta) {
		super();
		this.memUuid = memUuid;
		this.memName = memName;
		this.memPhone = memPhone;
		this.memBirthday = memBirthday;

		this.memAddress = memAddress;
		this.memAge = memAge;
		this.memHeight = memHeight;
		this.memWeight = memWeight;
		this.memEdu = memEdu;

		this.memMarry = memMarry;
		this.memAlcohol = memAlcohol;
		this.memSmoke = memSmoke;
		this.memIncome = memIncome;
		this.memAssets = memAssets;

		this.memIsvip = memIsvip;
		this.memRdate = memRdate;
		this.memLgd = memLgd;
		this.memSta = memSta;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public static void setSerialVersionUID(long serialVersionUID) {
		MemberInfo.serialVersionUID = serialVersionUID;
	}

	public String getMemUuid() {
		return memUuid;
	}

	public void setMemUuid(String memUuid) {
		this.memUuid = memUuid;
	}

	public String getMemName() {
		return memName;
	}

	public void setMemName(String memName) {
		this.memName = memName;
	}

	public String getMemPhone() {
		return memPhone;
	}

	public void setMemPhone(String memPhone) {
		this.memPhone = memPhone;
	}

	public String getMemBirthday() {
		return memBirthday;
	}

	public void setMemBirthday(String memBirthday) {
		this.memBirthday = memBirthday;
	}

	public Integer getMemAge() {
		return memAge;
	}

	public void setMemAge(Integer memAge) {
		this.memAge = memAge;
	}

	public String getMemAddress() {
		return memAddress;
	}

	public void setMemAddress(String memAddress) {
		this.memAddress = memAddress;
	}

	public Integer getMemHeight() {
		return memHeight;
	}

	public void setMemHeight(Integer memHeight) {
		this.memHeight = memHeight;
	}

	public Integer getMemWeight() {
		return memWeight;
	}

	public void setMemWeight(Integer memWeight) {
		this.memWeight = memWeight;
	}

	public Integer getMemEdu() {
		return memEdu;
	}

	public void setMemEdu(Integer memEdu) {
		this.memEdu = memEdu;
	}

	public Integer getMemMarry() {
		return memMarry;
	}

	public void setMemMarry(Integer memMarry) {
		this.memMarry = memMarry;
	}

	public Integer getMemAlcohol() {
		return memAlcohol;
	}

	public void setMemAlcohol(Integer memAlcohol) {
		this.memAlcohol = memAlcohol;
	}

	public Integer getMemSmoke() {
		return memSmoke;
	}

	public void setMemSmoke(Integer memSmoke) {
		this.memSmoke = memSmoke;
	}

	public Integer getMemIncome() {
		return memIncome;
	}

	public void setMemIncome(Integer memIncome) {
		this.memIncome = memIncome;
	}

	public Integer getMemAssets() {
		return memAssets;
	}

	public void setMemAssets(Integer memAssets) {
		this.memAssets = memAssets;
	}

	public Integer getMemIsvip() {
		return memIsvip;
	}

	public void setMemIsvip(Integer memIsvip) {
		this.memIsvip = memIsvip;
	}

	public String getMemRdate() {
		return memRdate;
	}

	public void setMemRdate(String memRdate) {
		this.memRdate = memRdate;
	}

	public Integer getMemLgd() {
		return memLgd;
	}

	public void setMemLgd(Integer memLgd) {
		this.memLgd = memLgd;
	}

	public Integer getMemSta() {
		return memSta;
	}

	public void setMemSta(Integer memSta) {
		this.memSta = memSta;
	}

	public JSONObject toJson() {
		JSONObject json = new JSONObject();

		json.put("memUuid", memUuid);
		json.put("memName", memName);
		json.put("memPhone", memPhone);
		json.put("memBirthday", memBirthday);
		json.put("memAge", memAge);
		json.put("memAddress", memAddress);
		json.put("memHeight", memHeight);
		json.put("memWeight", memWeight);
		json.put("memEdu", memEdu);
		json.put("memMarry", memMarry);
		json.put("memAlcohol", memAlcohol);
		json.put("memSmoke", memSmoke);
		json.put("memIncome", memIncome);
		json.put("memAssets", memAssets);
		json.put("memIsvip", memIsvip);
		json.put("memRdate", memRdate);
		json.put("memLgd", memLgd);
		json.put("memSta", memSta);

		return json;
	}

}