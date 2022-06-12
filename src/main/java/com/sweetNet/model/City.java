package com.sweetNet.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CITY")
public class City {

	@Id
	@Column(name = "city")
	private String city;
	@Column(name = "cityId")
	private String cityId;

	public City() {
		super();
	}

	public City(String city, String cityId) {
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
