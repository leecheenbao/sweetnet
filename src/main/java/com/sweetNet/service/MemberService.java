package com.sweetNet.service;

import java.util.Optional;

import com.sweetNet.model.Member;

public interface MemberService {

	Iterable<Member> findAll();

	void save(Member contact);

	Optional<Member> findOne(String id);
}
