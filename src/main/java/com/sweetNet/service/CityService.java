package com.sweetNet.service;

import com.sweetNet.model.City;

public interface CityService {

	Iterable<City> findAll();

	void save(City contact);

	Iterable<City> findByCityId(String cityId);

}
