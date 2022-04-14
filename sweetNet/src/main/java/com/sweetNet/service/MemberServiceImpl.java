package com.sweetNet.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sweetNet.model.Member;
import com.sweetNet.repository.MemberRepository;

@Service
public class MemberServiceImpl implements MemberService {
	@Autowired
	private MemberRepository memberRepository;

	@Override
	public Iterable<Member> findAll() {
		return memberRepository.findAll();
	}

	@Override
	public void save(Member contact) {
		memberRepository.save(contact);
	}

	@Override
	public Optional<Member> findOne(String mem_mail) {
		return memberRepository.findById(mem_mail);
	}

}