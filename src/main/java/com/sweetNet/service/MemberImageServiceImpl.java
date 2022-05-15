package com.sweetNet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sweetNet.model.MemberImage;
import com.sweetNet.repository.MemberImageRepository;

@Service
public class MemberImageServiceImpl implements MemberImageService {
	@Autowired
	private MemberImageRepository memberImageRepository;

	@Override
	public Iterable<MemberImage> findAll() {
		return memberImageRepository.findAll();
	}

	@Override
	public void save(MemberImage contact) {
		memberImageRepository.save(contact);
	}

}