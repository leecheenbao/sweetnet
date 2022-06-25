package com.sweetNet.dto;

public class CityDTO {

	private String city;
	private String cityId;

	public CityDTO() {
		super();
	}

	public CityDTO(String city, String cityId) {
		super();
		this.city = city;
		this.cityId = cityId;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
}
