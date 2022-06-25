package com.sweetNet.service;

import java.util.List;

import com.sweetNet.dto.CityDTO;
import com.sweetNet.model.City;

public interface CityService {

	List<CityDTO> findAll();

	void save(City contact);

	CityDTO findByCityId(String cityId);

}
