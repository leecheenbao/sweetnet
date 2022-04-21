package com.sweetNet.service;

import java.util.Optional;

import com.sweetNet.model.MemberInfo;

public interface MemberInfoService {

	Iterable<MemberInfo> findAll();

	void save(MemberInfo contact);

	Optional<MemberInfo> findOne(String id);

}