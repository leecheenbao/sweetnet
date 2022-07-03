package com.sweetNet.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sweetNet.model.MemberImage;
import com.sweetNet.repository.MemberImageRepository;
import com.sweetNet.service.MemberImageService;

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

	@Override
	public Iterable<MemberImage> findByUuid(String uuid) {
		Iterable<MemberImage> members = memberImageRepository.findByMemUuid(uuid);
		return members;
	}

	@Override
	public void saveSeq(MemberImage contact, int id) {
		
		if (contact.getId() == id) {
			contact.setSeq(1);
		} else {
			contact.setSeq(2);
		}

		memberImageRepository.save(contact);
	}

}