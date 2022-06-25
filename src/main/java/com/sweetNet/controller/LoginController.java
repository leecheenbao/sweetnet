package com.sweetNet.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.sweetNet.dto.LoginDTO;
import com.sweetNet.dto.MemberDTO;
import com.sweetNet.model.Member;
import com.sweetNet.repository.MemberRepository;
import com.sweetNet.service.MemberService;
import com.sweetNet.until.AesHelper;
import com.sweetNet.until.JwtTokenUtils;
import com.sweetNet.until.SystemInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "LoginController")
@RestController
public class LoginController {

	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private MemberService memberService;

	/**
	 * 會員登入
	 * 
	 * @param request
	 * @return JSONObject
	 */
	@ApiOperation("登入")
	@PostMapping(value = "/login")
	protected void doPost(LoginDTO loginDTO, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/plain;charset=UTF-8");
		JSONObject jsonParam = new JSONObject();
		Member member = new Member();
		String jsonStr = "";

		try (PrintWriter out = response.getWriter()) {

			// 驗證信箱、密碼
			String memMail = loginDTO.getMemMail();
			String memPwd = AesHelper.encrypt(loginDTO.getMemPwd());

			member.setMemMail(memMail);
			member.setMemPwd(memPwd);
			Example<Member> example = Example.of(member);
			Optional<Member> eresult = memberRepository.findOne(example);
			if (eresult.isPresent()) {

				Example<Member> memberExample = Example.of(member);
				member = memberRepository.findOne(memberExample).get();
				/* 登入次數 */
				Integer lgd = 0;
				if (member.getMemLgd() != null)
					lgd = member.getMemLgd();
				member.setMemLgd(lgd + 1);

				memberRepository.save(member);

				String memUuid = member.getMemUuid();

				HashMap<String, String> map = new HashMap<String, String>();
				map.put("memUuid", memUuid);

				String JWTtoken = JwtTokenUtils.generateToken(map); // 取得token

				jsonParam.put("token", JWTtoken);
				jsonParam.put("msg", "登入成功！");
				jsonParam.put("status", SystemInfo.DATA_OK);
				jsonStr = jsonParam.toJSONString();

				out.println(jsonStr);
			} else {

				jsonParam.put("msg", "登入失敗 !  請檢查信箱與密碼是否輸入錯誤 !?");
				jsonParam.put("status", SystemInfo.DATA_FAIL);
				jsonStr = jsonParam.toJSONString();

				out.println(jsonStr);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Member getMemberFromMemberDTO(MemberDTO memberDTO) {
		Member member = new Member();
		member.setMemAddress(memberDTO.getMemAddress());
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
		return member;
	}

}
