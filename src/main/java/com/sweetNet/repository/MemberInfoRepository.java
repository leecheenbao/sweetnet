package com.sweetNet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sweetNet.model.MemberInfo;

public interface MemberInfoRepository extends JpaRepository<MemberInfo, String> {

	Optional<MemberInfo> findByMemUuidAndMemSta(String mem_uuid, Integer mem_sta);
}