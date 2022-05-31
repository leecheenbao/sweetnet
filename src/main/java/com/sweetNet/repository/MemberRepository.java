package com.sweetNet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sweetNet.model.Member;

public interface MemberRepository extends JpaRepository<Member, String> {
	List<Member> findByMemSex(Integer memSex);

	List<Member> findByMemSexAndMemArea(Integer memSex, String memArea);
}