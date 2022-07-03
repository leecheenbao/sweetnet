package com.sweetNet.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sweetNet.dto.MemberDTO;
import com.sweetNet.model.Member;
import com.sweetNet.repository.MemberRepository;
import com.sweetNet.service.MemberService;

@Service
public class MemberServiceImpl implements MemberService {
	@Autowired
	private MemberRepository memberRepository;

	@Override
	public List<MemberDTO> findAll() {
		List<Member> members = memberRepository.findAll();
		List<MemberDTO> memberDTOs = new ArrayList<MemberDTO>();
		for (Member member : members) {
			MemberDTO memberDTO = this.getMemberDTOFromMember(member);
			memberDTOs.add(memberDTO);
		}
		return memberDTOs;
	}

	@Override
	public List<MemberDTO> findByMemSex(Integer memSex) {
		List<Member> members = memberRepository.findByMemSex(memSex);
		List<MemberDTO> memberDTOs = new ArrayList<MemberDTO>();
		for (Member member : members) {
			MemberDTO memberDTO = this.getMemberDTOFromMember(member);
			memberDTOs.add(memberDTO);
		}
		return memberDTOs;
	}

	@Override
	public List<MemberDTO> findByMemSexAndMemArea(Integer memSex, String memArea) {
		List<Member> members = memberRepository.findByMemSexAndMemArea(memSex, memArea);
		List<MemberDTO> memberDTOs = new ArrayList<MemberDTO>();
		for (Member member : members) {
			MemberDTO memberDTO = this.getMemberDTOFromMember(member);
			memberDTOs.add(memberDTO);
		}
		return memberDTOs;
	}

	@Override
	public void save(Member member) {
		memberRepository.save(member);
	}

	@Override
	public MemberDTO findOneByUuid(String memUuid) {
		MemberDTO memberDTO = new MemberDTO();
		Member member = new Member();

		Optional<Member> mOptional = memberRepository.findById(memUuid);

		if (mOptional.isPresent())
			member = mOptional.orElse(null);
		memberDTO = this.getMemberDTOFromMember(member);
		return memberDTO;
	}

	@Override
	public MemberDTO findOneByEmail(String memEamil) {
		MemberDTO memberDTO = new MemberDTO();
		Member member = memberRepository.findByMemMail(memEamil);
		if (member != null) {

			memberDTO = this.getMemberDTOFromMember(member);
		}
		return memberDTO;
	}

	public Member getMemberFromMemberDTO(MemberDTO memberDTO) {
		Member member = new Member();
		member.setMemCountry(memberDTO.getMemCountry());
		member.setMemAge(memberDTO.getMemAge());
		member.setMemAlcohol(memberDTO.getMemAlcohol());
		member.setMemArea(memberDTO.getMemArea());
		member.setMemAssets(memberDTO.getMemAssets());
		member.setMemBirthday(memberDTO.getMemBirthday());
		member.setMemDep(memberDTO.getMemDep());
		member.setMemEdu(memberDTO.getMemEdu());
		member.setMemHeight(memberDTO.getMemHeight());
		member.setMemIncome(memberDTO.getMemIncome());
		member.setMemIsvip(memberDTO.getMemIsvip());
		member.setMemLgd(memberDTO.getMemLgd());
		member.setMemMail(memberDTO.getMemMail());
		member.setMemMarry(memberDTO.getMemMarry());
		member.setMemName(memberDTO.getMemName());
		member.setMemNickname(memberDTO.getMemNickname());
		member.setMemPhone(memberDTO.getMemPhone());
		member.setMemPwd(memberDTO.getMemPwd());
		member.setMemRdate(memberDTO.getMemRdate());
		member.setMemSex(memberDTO.getMemSex());
		member.setMemSmoke(memberDTO.getMemSmoke());
		member.setMemSta(memberDTO.getMemSta());
		member.setMemUuid(memberDTO.getMemUuid());
		member.setMemWeight(memberDTO.getMemWeight());
		member.setMemAbout(memberDTO.getMemAbout());
		member.setMemBody(memberDTO.getMemBody());
		member.setMemPattern(memberDTO.getMemPattern());
		return member;
	}

	public MemberDTO getMemberDTOFromMember(Member member) {
		MemberDTO memberDTO = new MemberDTO();
		memberDTO.setMemCountry(member.getMemCountry());
		memberDTO.setMemAge(member.getMemAge());
		memberDTO.setMemAlcohol(member.getMemAlcohol());
		memberDTO.setMemArea(member.getMemArea());
		memberDTO.setMemAssets(member.getMemAssets());
		memberDTO.setMemBirthday(member.getMemBirthday());
		memberDTO.setMemDep(member.getMemDep());
		memberDTO.setMemEdu(member.getMemEdu());
		memberDTO.setMemHeight(member.getMemHeight());
		memberDTO.setMemIncome(member.getMemIncome());
		memberDTO.setMemIsvip(member.getMemIsvip());
		memberDTO.setMemLgd(member.getMemLgd());
		memberDTO.setMemMail(member.getMemMail());
		memberDTO.setMemMarry(member.getMemMarry());
		memberDTO.setMemName(member.getMemName());
		memberDTO.setMemNickname(member.getMemNickname());
		memberDTO.setMemPhone(member.getMemPhone());
		memberDTO.setMemRdate(member.getMemRdate());
		memberDTO.setMemSex(member.getMemSex());
		memberDTO.setMemSmoke(member.getMemSmoke());
		memberDTO.setMemSta(member.getMemSta());
		memberDTO.setMemUuid(member.getMemUuid());
		memberDTO.setMemWeight(member.getMemWeight());
		memberDTO.setMemAbout(member.getMemAbout());
		memberDTO.setMemBody(member.getMemBody());
		memberDTO.setMemPattern(member.getMemPattern());

		memberDTO.setPhoneStates(member.getPhoneStates());
		memberDTO.setMemPwd(member.getMemPwd());
		return memberDTO;
	}

}