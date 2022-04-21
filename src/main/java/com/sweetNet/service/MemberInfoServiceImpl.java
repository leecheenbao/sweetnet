package com.sweetNet.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sweetNet.model.MemberInfo;
import com.sweetNet.repository.MemberInfoRepository;

@Service
public class MemberInfoServiceImpl implements MemberInfoService {
	@Autowired
	private MemberInfoRepository memberinfoRepository;

	@Override
	public Iterable<MemberInfo> findAll() {
		return memberinfoRepository.findAll();
	}

	@Override
	public void save(MemberInfo contact) {
		memberinfoRepository.save(contact);
	}

	@Override
	public Optional<MemberInfo> findOne(String id) {
		return memberinfoRepository.findById(id);
	}

}