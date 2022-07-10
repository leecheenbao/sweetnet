package com.sweetNet.service;

import java.util.List;

import com.sweetNet.dto.MemberDTO;
import com.sweetNet.dto.SearchConditionDTO;
import com.sweetNet.model.Member;

public interface MemberService {

	List<MemberDTO> findAll();

	List<MemberDTO> findByMemSex(Integer memSex);

	List<MemberDTO> findByMemSexAndMemArea(Integer memSex, String memArea);

	MemberDTO findOneByEmail(String memMail);

	MemberDTO findOneByEmailAndPwd(String memMail, String memPwd);

	MemberDTO findOneByUuid(String memUuid);

	void save(Member member);

	List<MemberDTO> findByCondition(String action, SearchConditionDTO searchConditionDTO);
}
