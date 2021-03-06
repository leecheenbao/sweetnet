package com.sweetNet.dto;

public class MemberInfoDTO {

	private String memNickname;

	private String memDep;

	private String memName;

	private String memPhone;

	private Integer phoneStates;

	private String memBirthday;

	private Integer memAge;

	private String memCountry;

	private String memArea;

	private Integer memHeight;

	private Integer memWeight;

	private Integer memEdu;

	private Integer memMarry;

	private Integer memAlcohol;

	private Integer memSmoke;

	private Integer memIncome = 0;

	private Integer memAssets = 0;

	private Integer memIsvip;

	private String memAbout;

	private Integer memBody;

	private Integer memPattern;

	public MemberInfoDTO() {
		super();
	}

	public MemberInfoDTO(String memMail, String memPwd, String memNickname, String memDep, Integer memSex,
			String memName, String memPhone, Integer phoneStates, String memBirthday, String memCountry, String memArea,
			Integer memAge, Integer memHeight, Integer memWeight, Integer memEdu, Integer memMarry, Integer memAlcohol,
			Integer memSmoke, Integer memIncome, Integer memAssets, Integer memIsvip, String memRdate, Integer memLgd,
			Integer memSta, Integer memBody, Integer memPattern, String memAbout) {
		super();
		this.memNickname = memNickname;
		this.memDep = memDep;
		this.memName = memName;
		this.memPhone = memPhone;
		this.phoneStates = phoneStates;
		this.memBirthday = memBirthday;

		this.memCountry = memCountry;
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
		this.memAbout = memAbout;
		this.memBody = memBody;
		this.memPattern = memPattern;
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

	public String getMemName() {
		return memName;
	}

	public void setMemName(String memName) {
		this.memName = memName;
	}

	public Integer getMemBody() {
		return memBody;
	}

	public void setMemBody(Integer memBody) {
		this.memBody = memBody;
	}

	public Integer getMemPattern() {
		return memPattern;
	}

	public void setMemPattern(Integer memPattern) {
		this.memPattern = memPattern;
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

	public String getMemCountry() {
		return memCountry;
	}

	public void setMemCountry(String memCountry) {
		this.memCountry = memCountry;
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

	public String getMemArea() {
		return memArea;
	}

	public void setMemArea(String memArea) {
		this.memArea = memArea;
	}

	public String getMemAbout() {
		return memAbout;
	}

	public void setMemAbout(String memAbout) {
		this.memAbout = memAbout;
	}
}