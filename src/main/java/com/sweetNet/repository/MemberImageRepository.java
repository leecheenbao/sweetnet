package com.sweetNet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sweetNet.model.MemberImage;

public interface MemberImageRepository extends JpaRepository<MemberImage, String> {
	List<MemberImage> findByMemUuid(String memUuid);
}