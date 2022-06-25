package com.sweetNet.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.sweetNet.dto.CityDTO;
import com.sweetNet.model.City;
import com.sweetNet.repository.CityRepository;
import com.sweetNet.until.GetLocalJSON;

@Service
public class CityServiceImpl implements CityService {
	@Autowired
	private CityRepository cityRepository;

	@Override
	public List<CityDTO> findAll() {
		List<City> citys = cityRepository.findAll();
		List<CityDTO> cityDTOs = new ArrayList<CityDTO>();
		for (City city : citys) {
			CityDTO cityDTO = new CityDTO();
			cityDTO.setCity(city.getCity());
			cityDTO.setCityId(city.getCityId());
			cityDTOs.add(cityDTO);
		}
		return cityDTOs;
	}

	@Override
	public void save(City contact) {
		cityRepository.save(contact);
	}

	@Override
	public CityDTO findByCityId(String cityId) {
		City city = cityRepository.findByCityId(cityId);
		CityDTO cityDTO = new CityDTO();
		cityDTO.setCity(city.getCity());
		cityDTO.setCityId(city.getCityId());
		return null;
	}

	@Autowired
	private static CityService cs = new CityServiceImpl();

	public static void main(String args[]) {
		JSONArray jsonArray = new JSONArray();
		try {
			File file = new File("country.json");
			jsonArray = GetLocalJSON.getCityJSON();
			Gson gson = new Gson();
			City c = new City();
			String list = gson.toJson(jsonArray, List.class);
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject json = (JSONObject) jsonArray.get(i);
				String city = json.getString("city");
				String cityId = json.getString("cityId");

				c.setCity(city);
				c.setCityId(cityId);
				cs.save(c);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}