package com.sweetNet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sweetNet.model.MemberInfo;
import com.sweetNet.repository.MemberInfoRepository;

@Service
public class MemberInfoServiceImpl implements MemberInfoService {
	@Autowired
	private MemberInfoRepository memberRepository;

	@Override
	public Iterable<MemberInfo> findAll() {
		return memberRepository.findAll();
	}

}