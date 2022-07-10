package com.sweetNet.service;

import java.util.List;

import com.sweetNet.model.Bulletin;

public interface BulletinService {

	List<Bulletin> findAll();

	void save(Bulletin bulletin);

	Bulletin findOne(Integer id);

	List<Bulletin> findBySates();

	void delete(Integer id);
}
