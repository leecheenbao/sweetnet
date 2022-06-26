package com.sweetNet.service;

import java.util.List;

import com.sweetNet.model.Images;

public interface ImagesService {

	void save(Images contact);

	List<Images> findAll();

	Images findByMemUuid(String memUuid);

}
