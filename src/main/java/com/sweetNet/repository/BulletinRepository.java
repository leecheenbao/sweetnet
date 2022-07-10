package com.sweetNet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sweetNet.model.Bulletin;

public interface BulletinRepository extends JpaRepository<Bulletin, String> {

	@Query(value = " SELECT * FROM BULLETIN WHERE POSTTIME < NOW()  AND STATES = 0 ORDER BY POSTTIME DESC ",nativeQuery = true)
	public List<Bulletin> findByStates();

	public Bulletin findById(Integer id);
	
	@Query(value = "SELECT * FROM BULLETIN ORDER BY POSTTIME DESC",nativeQuery = true)
	public List<Bulletin> findAllOrderByPostTime();

}