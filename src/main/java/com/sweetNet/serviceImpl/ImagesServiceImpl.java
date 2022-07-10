package com.sweetNet.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sweetNet.model.Images;
import com.sweetNet.repository.ImagesRepository;
import com.sweetNet.service.ImagesService;

@Service
public class ImagesServiceImpl implements ImagesService {
	@Autowired
	private ImagesRepository imagesRepository;

	@Override
	public void save(Images images) {
		imagesRepository.save(images);
	}

	@Override
	public List<Images> findAll() {
		return imagesRepository.findAll();
	}

	@Override
	public Images findByMemUuid(String memUuid) {
		return imagesRepository.findByMemUuid(memUuid);
	}

}