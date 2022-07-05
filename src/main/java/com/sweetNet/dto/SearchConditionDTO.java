package com.sweetNet.dto;

public class SearchConditionDTO {

	private String memCountry;
	private String memArea;
	private Integer weightMin;
	private Integer weightMax;
	private Integer heightMin;
	private Integer heightMax;
	private Integer memPattern;
	private Integer memSex;

	public SearchConditionDTO() {
		super();
	}

	public SearchConditionDTO(String memCountry, String memArea, Integer weightMin, Integer weightMax,
			Integer heightMin, Integer heightMax, Integer memPattern, Integer memSex) {
		super();
		this.memCountry = memCountry;
		this.memArea = memArea;
		this.weightMin = weightMin;
		this.weightMax = weightMax;
		this.heightMin = heightMin;
		this.heightMax = heightMax;
		this.memPattern = memPattern;
		this.memSex = memSex;
	}

	public Integer getMemPattern() {
		return memPattern;
	}

	public void setMemPattern(Integer memPattern) {
		this.memPattern = memPattern;
	}

	public Integer getMemSex() {
		return memSex;
	}

	public void setMemSex(Integer memSex) {
		this.memSex = memSex;
	}

	public String getMemCountry() {
		return memCountry;
	}

	public void setMemCountry(String memCountry) {
		this.memCountry = memCountry;
	}

	public String getMemArea() {
		return memArea;
	}

	public void setMemArea(String memArea) {
		this.memArea = memArea;
	}

	public Integer getWeightMin() {
		return weightMin;
	}

	public void setWeightMin(Integer weightMin) {
		this.weightMin = weightMin;
	}

	public Integer getWeightMax() {
		return weightMax;
	}

	public void setWeightMax(Integer weightMax) {
		this.weightMax = weightMax;
	}

	public Integer getHeightMin() {
		return heightMin;
	}

	public void setHeightMin(Integer heightMin) {
		this.heightMin = heightMin;
	}

	public Integer getHeightMax() {
		return heightMax;
	}

	public void setHeightMax(Integer heightMax) {
		this.heightMax = heightMax;
	}

}