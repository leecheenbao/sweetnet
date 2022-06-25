package com.sweetNet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sweetNet.model.City;

public interface CityRepository extends JpaRepository<City, String> {
	City findByCityId(String cityId);
}