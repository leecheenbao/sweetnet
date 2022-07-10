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

	Member findByMemMailAndMemPwd(String memEmail,String memPwd);
//	@Query(value = "select * from Member where "
//			+ "if(?1 !='',memArea like concat('%',?1,'%'),1=1) and "
//			+ "if(?2 !='',memCity=?2,1=1) and "
//			+ "if(IFNULL(?3,'') !='',age=?3,1=1) and "
//			+ "if(IFNULL(?4,'') !='',num=?4,1=1) and "
//			+ "if(IFNULL(?5,'') !='',weightMin?5,1=1) ",nativeQuery = true)
	
	@Query(value = "select * from MEMBER where "
		+ "if(?1 !='',				memCountry	=  ?1,	1=1) and "
		+ "if(?2 !='',				memArea 	=  ?2,	1=1) and "
		+ "if(IFNULL(?3,0) !='', 	memHeight 	>= ?3, 	1=1) and "
		+ "if(IFNULL(?4,0) !='', 	memHeight 	<  ?4, 	1=1) and "
		+ "if(IFNULL(?5,0) !='', 	memWeight 	>= ?5, 	1=1) and "
		+ "if(IFNULL(?6,0) !='', 	memWeight 	<  ?6, 	1=1) and "
		+ "if(IFNULL(?5,0) !='', 	memPattern 	=  ?7, 	1=1) ",nativeQuery = true)
	List<Member> findCondiction(String memCountry, String memArea,  Integer heightMin,Integer heightMax, Integer weightMin, Integer weightMax, Integer memPattern);
	
	@Query(value = "select * from MEMBER where "
			+ "if(?1 !='',				memCountry	=  ?1,	1=1) and "
			+ "if(?2 !='',				memArea 	=  ?2,	1=1) and "
			+ "if(IFNULL(?3,0) !='', 	memHeight 	>= ?3, 	1=1) and "
			+ "if(IFNULL(?4,0) !='', 	memHeight 	<  ?4, 	1=1) and "
			+ "if(IFNULL(?5,0) !='', 	memWeight 	>= ?5, 	1=1) and "
			+ "if(IFNULL(?6,0) !='', 	memWeight 	<  ?6, 	1=1) and "
			+ "if(IFNULL(?5,0) !='', 	memPattern 	=  ?7, 	1=1)",nativeQuery = true)
	List<Member> findCondiction(String memCountry, String memArea,  Integer heightMin,Integer heightMax, Integer weightMin, Integer weightMax, Integer memPattern, Pageable p);

}