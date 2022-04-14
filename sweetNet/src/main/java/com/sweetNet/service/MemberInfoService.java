package com.sweetNet.service;

import java.util.List;
import java.util.Optional;

import com.sweetNet.model.MemberInfo;

public interface MemberInfoService {

	Iterable<MemberInfo> findAll();
	
//	List<MemberInfo> search(String q);
//
//	MemberInfo findOne(String id);
//
//	void save(MemberInfo contact);
//
//	void delete(String id);

}