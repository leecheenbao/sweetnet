package com.sweetNet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sweetNet.model.MemberInfo;

@Repository
public interface MemberInfoRepository extends JpaRepository<MemberInfo, String> {

}