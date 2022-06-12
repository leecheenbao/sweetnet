package com.sweetNet.service;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.sweetNet.model.City;
import com.sweetNet.repository.CityRepository;
import com.sweetNet.until.GetLocalJSON;

@Service
public class CityServiceImpl implements CityService {
	@Autowired
	private CityRepository cityRepository;

	@Override
	public Iterable<City> findAll() {
		return cityRepository.findAll();
	}

	@Override
	public void save(City contact) {
		cityRepository.save(contact);
	}

	@Override
	public List<City> findByCityId(String cityId) {

		return cityRepository.findByCityId(cityId);
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