package com.sweetNet.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sweetNet.model.Member;

public interface MemberRepository extends JpaRepository<Member, String> {
	List<Member> findByMemSex(Integer memSex);

	List<Member> findByMemSexAndMemArea(Integer memSex, String memArea);

	Member findByMemMail(String memEmail);

	Member findByMemMailAndMemPwd(String memEmail, String memPwd);

	@Query(value = "select * from MEMBER where " + "if(?1 !='',				memCountry	=  ?1,	1=1) and "
			+ "if(?2 !='',				memArea 	=  ?2,	1=1) and "
			+ "if(IFNULL(?3,0) !='', 	memHeight 	>= ?3, 	1=1) and "
			+ "if(IFNULL(?4,0) !='', 	memHeight 	<  ?4, 	1=1) and "
			+ "if(IFNULL(?5,0) !='', 	memWeight 	>= ?5, 	1=1) and "
			+ "if(IFNULL(?6,0) !='', 	memWeight 	<  ?6, 	1=1) and "
			+ "if(IFNULL(?5,0) !='', 	memPattern 	=  ?7, 	1=1) ", nativeQuery = true)
	List<Member> findCondiction(String memCountry, String memArea, Integer heightMin, Integer heightMax,
			Integer weightMin, Integer weightMax, Integer memPattern);

//	if(IFNULL(?9,0) !='', 	memPattern 	like ?9, 	1=1)

	@Query(value = "select * from MEMBER where " 
			+ "if(?1 !='',				memCountry	=  ?1,	1=1) and "
			+ "if(?2 !='',				memArea 	=  ?2,	1=1) and "
			+ "if(IFNULL(?3,0) !='', 	memHeight 	>= ?3, 	1=1) and "
			+ "if(IFNULL(?4,0) !='', 	memHeight 	<  ?4, 	1=1) and "
			+ "if(IFNULL(?5,0) !='', 	memWeight 	>= ?5, 	1=1) and "
			+ "if(IFNULL(?6,0) !='', 	memWeight 	<  ?6, 	1=1) and "
			+ "if(IFNULL(?7,0) !='', 	memAge	 	>= ?7, 	1=1) and "
			+ "if(IFNULL(?8,0) !='', 	memAge	 	<  ?8, 	1=1) and "
			+ "if(IFNULL(?9,0) !='', 	memPattern 	=  ?9, 	memPattern >= 0) ", nativeQuery = true)
	List<Member> findCondiction(String memCountry, String memArea, Integer heightMin, Integer heightMax,
			Integer weightMin, Integer weightMax, Integer ageMin, Integer ageMax, Integer memPattern, Pageable p);

}