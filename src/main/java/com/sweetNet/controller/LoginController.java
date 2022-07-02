package com.sweetNet.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.sweetNet.dto.LoginDTO;
import com.sweetNet.dto.MemberDTO;
import com.sweetNet.dto.SignUpDTO;
import com.sweetNet.model.Member;
import com.sweetNet.repository.MemberRepository;
import com.sweetNet.service.MemberService;
import com.sweetNet.until.AesHelper;
import com.sweetNet.until.ConfigInfo;
import com.sweetNet.until.JwtTokenUtils;
import com.sweetNet.until.SendMail;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", required = false, dataType = "String", name = "memMail", value = "信箱", example = "test001@gmail.com"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "String", name = "memPwd", value = "密碼", example = "12345678") })
	@ApiOperation("登入")
	@PostMapping(value = "/login")
	protected String login(@RequestBody LoginDTO loginDTO) throws ServletException, IOException {

		Member member = new Member();

		Map<String, Object> map = new HashMap<String, Object>();
		String msg = ConfigInfo.SYS_MESSAGE_SUCCESS;
		String states = ConfigInfo.DATA_OK;

		try {

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

				HashMap<String, String> dataMap = new HashMap<String, String>();
				dataMap.put("memUuid", memUuid);

				String JWTtoken = JwtTokenUtils.generateToken(dataMap); // 取得token
				map.put("token", JWTtoken);
			} else {
				states = ConfigInfo.DATA_FAIL;
				msg = "登入失敗 !  請檢查信箱與密碼是否輸入錯誤 !?";
			}

		} catch (Exception e) {
			e.printStackTrace();
			states = ConfigInfo.DATA_FAIL;
			msg = "登入失敗 !  請檢查信箱與密碼是否輸入錯誤 !?";
		}

		map.put("states", states);
		map.put("msg", msg);

		Gson gson = new Gson();

		return gson.toJson(map);
	}

	@ApiOperation("忘記密碼")
	@PostMapping(value = "/forget")
	protected String forget(@RequestBody SignUpDTO signUpDTO) throws ServletException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		String msg = ConfigInfo.SYS_MESSAGE_SUCCESS;
		String states = ConfigInfo.DATA_OK;

		try {

			String mail = signUpDTO.getMemMail();
			StringBuffer subject = new StringBuffer();
			subject.append("忘記密碼");
			StringBuffer content = new StringBuffer();
			content.append("請點擊下方連結盡速更改密碼");

			SendMail sendMail = new SendMail();
			sendMail.sendMail_SugarDaddy(String.valueOf(subject), String.valueOf(content), mail);
		} catch (Exception e) {
			e.printStackTrace();
		}

		map.put("states", states);
		map.put("msg", msg);

		Gson gson = new Gson();

		return gson.toJson(map);
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
		return member;
	}

}
