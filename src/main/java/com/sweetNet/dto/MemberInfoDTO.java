package com.sweetNet.dto;

import java.io.Serializable;

import javax.persistence.Column;

import com.alibaba.fastjson.JSONObject;

public class MemberInfoDTO implements Serializable {

	private String memUuid;
	private String memMail;

	private String memPwd;

	private String memNickname;

	private String memDep;

	private Integer memSex;

	private String memName;

	private String memPhone;

	private Integer phoneStates;

	private String memBirthday;

	private Integer memAge;

	private String memAddress;

	private String memArea;

	private Integer memHeight;

	private Integer memWeight;

	@Column(name = "memEdu")
	private Integer memEdu;

	private Integer memMarry;

	private Integer memAlcohol;

	private Integer memSmoke;

	private Integer memIncome;

	private Integer memAssets;

	private Integer memIsvip;

	private String memRdate;

	private Integer memLgd;

	private Integer memSta;

	public MemberInfoDTO() {
		super();
	}

	public MemberInfoDTO(String memUuid, String memMail, String memPwd, String memNickname, String memDep, Integer memSex,
			String memName, String memPhone, Integer phoneStates, String memBirthday, String memAddress, String memArea,
			Integer memAge, Integer memHeight, Integer memWeight, Integer memEdu, Integer memMarry, Integer memAlcohol,
			Integer memSmoke, Integer memIncome, Integer memAssets, Integer memIsvip, String memRdate, Integer memLgd,
			Integer memSta) {
		super();
		this.memUuid = memUuid;
		this.memMail = memMail;
		this.memPwd = memPwd;
		this.memNickname = memNickname;
		this.memDep = memDep;
		this.memSex = memSex;
		this.memName = memName;
		this.memPhone = memPhone;
		this.phoneStates = phoneStates;
		this.memBirthday = memBirthday;

		this.memAddress = memAddress;
		this.memArea = memArea;
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

	public Integer getPhoneStates() {
		return phoneStates;
	}

	public void setPhoneStates(Integer phoneStates) {
		this.phoneStates = phoneStates;
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

	public String getMemArea() {
		return memArea;
	}

	public void setMemArea(String memArea) {
		this.memArea = memArea;
	}

	public JSONObject toJson() {
		JSONObject json = new JSONObject();

		json.put("memUuid", memUuid);
		json.put("memMail", memMail);
		json.put("memNickname", memNickname);
		json.put("memDep", memDep);
		json.put("memSex", memSex);
		json.put("memName", memName);
		json.put("memPhone", memPhone);
		json.put("memBirthday", memBirthday);
		json.put("memAge", memAge);
		json.put("memAddress", memAddress);
		json.put("memArea", memArea);
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