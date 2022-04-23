package com.sweetNet.service;

import com.sweetNet.model.Member;

public interface MemberService {

	Iterable<Member> findAll();

	void save(Member contact);

}
