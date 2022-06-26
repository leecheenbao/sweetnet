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
	@Column(name = "memUuid")
	private String memUuid;
	@Column(name = "memMail")
	private String memMail;

	@Column(name = "memPwd")
	private String memPwd;

	@Column(name = "memNickname")
	private String memNickname;

	@Column(name = "memDep")
	private String memDep;

	@Column(name = "memSex")
	private Integer memSex;

	@Column(name = "memName")
	private String memName;

	@Column(name = "memPhone")
	private String memPhone;

	@Column(name = "phoneStates")
	private Integer phoneStates;

	@Column(name = "memBirthday")
	private String memBirthday;

	@Column(name = "memAge")
	private Integer memAge;

	@Column(name = "memCountry")
	private String memCountry;

	@Column(name = "memArea")
	private String memArea;

	@Column(name = "memHeight")
	private Integer memHeight;

	@Column(name = "memWeight")
	private Integer memWeight;

	@Column(name = "memEdu")
	private Integer memEdu;

	@Column(name = "memMarry")
	private Integer memMarry;

	@Column(name = "memAlcohol")
	private Integer memAlcohol;

	@Column(name = "memSmoke")
	private Integer memSmoke;

	@Column(name = "memIncome")
	private Integer memIncome;

	@Column(name = "memAssets")
	private Integer memAssets;

	@Column(name = "memIsvip")
	private Integer memIsvip;

	@Column(name = "memRdate")
	private String memRdate;

	@Column(name = "memLgd")
	private Integer memLgd;

	@Column(name = "memSta")
	private Integer memSta;

	@Column(name = "memAbout")
	private String memAbout;
	


	public Member() {
		super();
	}

	public Member(String memUuid, String memMail, String memPwd, String memNickname, String memDep, Integer memSex,
			String memName, String memPhone, Integer phoneStates, String memBirthday, String memCountry, String memArea,
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
		this.memRdate = memRdate;
		this.memLgd = memLgd;
		this.memSta = memSta;
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

	public String getMemAbout() {
		return memAbout;
	}

	public void setMemAbout(String memAbout) {
		this.memAbout = memAbout;
	}

}