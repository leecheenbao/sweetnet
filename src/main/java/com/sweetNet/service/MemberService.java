package com.sweetNet.service;

import java.util.List;

import com.sweetNet.dto.MemberInfoDTO;
import com.sweetNet.model.Member;

public interface MemberService {

	List<MemberInfoDTO> findAll();

	List<MemberInfoDTO> findByMemSex(Integer memSex);

	List<MemberInfoDTO> findByMemSexAndMemArea(Integer memSex, String memArea);

	MemberInfoDTO findOneByEmail(String memMail);

	MemberInfoDTO findOneByUuid(String memberUuid);

	void save(Member member);
}
