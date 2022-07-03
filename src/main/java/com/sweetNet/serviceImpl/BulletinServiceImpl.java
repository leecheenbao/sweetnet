package com.sweetNet.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sweetNet.model.Bulletin;
import com.sweetNet.repository.BulletinRepository;
import com.sweetNet.service.BulletinService;

@Service
public class BulletinServiceImpl implements BulletinService {
	@Autowired
	private BulletinRepository bulletinRepository;

	@Override
	public List<Bulletin> findAll() {
		return bulletinRepository.findAll();
	}

	@Override
	public void save(Bulletin bulletin) {
		bulletinRepository.save(bulletin);
	}

	@Override
	public Bulletin findOne(Integer id) {
		return bulletinRepository.findById(id);
	}

	@Override
	public List<Bulletin> findBySates(Integer states) {
		return bulletinRepository.findByStates(states);
	}

}