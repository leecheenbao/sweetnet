package com.sweetNet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sweetNet.model.Images;

public interface ImagesRepository extends JpaRepository<Images, String> {
	Images findByMemUuid(String memUuid);
}