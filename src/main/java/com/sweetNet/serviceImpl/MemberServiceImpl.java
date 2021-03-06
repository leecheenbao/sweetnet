package com.sweetNet.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.sweetNet.dto.MemberDTO;
import com.sweetNet.dto.SearchConditionDTO;
import com.sweetNet.model.Member;
import com.sweetNet.repository.MemberRepository;
import com.sweetNet.service.MemberService;
import com.sweetNet.until.Until;

@Service
public class MemberServiceImpl implements MemberService {
	@Autowired
	private MemberRepository memberRepository;

	@Override
	public List<MemberDTO> findByCondition(String action, SearchConditionDTO searchConditionDTO) {
		String memArea = searchConditionDTO.getMemArea();
		String memCountry = searchConditionDTO.getMemCountry();
		Integer heightMin = searchConditionDTO.getHeightMin();
		Integer heightMax = searchConditionDTO.getHeightMax();
		Integer weightMin = searchConditionDTO.getWeightMin();
		Integer weightMax = searchConditionDTO.getWeightMax();
		Integer memPattern = searchConditionDTO.getMemPattern();
		Integer memSex = searchConditionDTO.getMemSex();
		Integer ageMax = searchConditionDTO.getAgeMax();
		Integer ageMin = searchConditionDTO.getAgeMin();

		/* 計算會員年齡區間 */
		String dateMax = String.valueOf(Until.getDateFromAge(ageMax));
		String dateMin = String.valueOf(Until.getDateFromAge(ageMin));

		/* 僅能瀏覽異性資料 */
		if (memSex > 0) {
			memSex = (memSex == 1) ? 2 : 1;
		}

		Integer total = memberRepository.findAll().size();
		Pageable pageable = null;
		if (action.equals("hot")) {
			pageable = PageRequest.of(0, total, Sort.Direction.DESC, "memSeq");
		} else if (action.equals("new")) {
			pageable = PageRequest.of(0, total, Sort.Direction.DESC, "memRdate");
		}

		List<Member> members = memberRepository.findCondiction(memCountry, memArea, heightMin, heightMax, weightMin,
				weightMax, ageMin, ageMax, memPattern, pageable);
		List<MemberDTO> memberDTOs = new ArrayList<MemberDTO>();
		for (Member member : members) {
			if (member.getMemSex() != memSex && member.getMemSex() > 0) {
				MemberDTO memberDTO = this.getMemberDTOFromMember(member);
				memberDTOs.add(memberDTO);
			}
		}
		return memberDTOs;
	}

	@Override
	public MemberDTO findOneByEmailAndPwd(String memEmail, String memPwd) {
		Member member = memberRepository.findByMemMailAndMemPwd(memEmail, memPwd);
		MemberDTO memberDTO = this.getMemberDTOFromMember(member);
		return memberDTO;
	}

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
		member.setMemSeq(memberDTO.getMemSeq());
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
		memberDTO.setMemSeq(member.getMemSeq());
		memberDTO.setPhoneStates(member.getPhoneStates());
		memberDTO.setMemPwd(member.getMemPwd());
		return memberDTO;
	}

}