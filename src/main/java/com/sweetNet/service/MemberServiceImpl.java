package com.sweetNet.service;

import java.util.List;

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
	public List<Member> findByMem_Sex(Integer mem_sex) {
		return memberRepository.findByMemSex(mem_sex);
	}


}