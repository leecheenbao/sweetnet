package com.sweetNet.dto;

public class SearchConditionDTO {

	private String city;
	private String area;
	private Integer weightMin;
	private Integer weightMax;
	private Integer heightMin;
	private Integer heightMax;
	private Integer pattern;

	public SearchConditionDTO() {
		super();
	}

	public SearchConditionDTO(String city, String area, Integer weightMin, Integer weightMax, Integer heightMin,
			Integer heightMax, Integer pattern) {
		super();
		this.city = city;
		this.area = area;
		this.weightMin = weightMin;
		this.weightMax = weightMax;
		this.heightMin = heightMin;
		this.heightMax = heightMax;
		this.pattern = pattern;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
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

	public Integer getpattern() {
		return pattern;
	}

	public void setpattern(Integer pattern) {
		this.pattern = pattern;
	}

	public SearchConditionDTO(String city) {
		super();
		this.city = city;
	}

}