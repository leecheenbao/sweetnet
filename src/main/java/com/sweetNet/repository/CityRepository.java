package com.sweetNet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sweetNet.model.City;

public interface CityRepository extends JpaRepository<City, String> {
	List<City> findByCityId(String cityId);
}