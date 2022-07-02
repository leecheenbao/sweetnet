package com.sweetNet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sweetNet.model.Bulletin;

public interface BulletinRepository extends JpaRepository<Bulletin, String> {

	public List<Bulletin> findByStates(Integer states);

	public Bulletin findById(Integer id);

}