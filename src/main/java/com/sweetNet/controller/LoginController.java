package com.sweetNet.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.google.gson.Gson;
import com.sweetNet.dto.LoginDTO;
import com.sweetNet.dto.MemberDTO;
import com.sweetNet.dto.SignUpDTO;
import com.sweetNet.model.Member;
import com.sweetNet.repository.MemberRepository;
import com.sweetNet.service.MemberService;
import com.sweetNet.serviceImpl.MemberServiceImpl;
import com.sweetNet.until.AesHelper;
import com.sweetNet.until.ConfigInfo;
import com.sweetNet.until.JwtTokenUtils;
import com.sweetNet.until.SendMail;
import com.sweetNet.until.Until;

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
	private Logger logger = LoggerFactory.getLogger(this.getClass());

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

		// 驗證信箱、密碼
		String memMail = loginDTO.getMemMail();
		String memPwd = AesHelper.encrypt(loginDTO.getMemPwd());
		try {

			member.setMemMail(memMail);
			member.setMemPwd(memPwd);
			MemberDTO memberDTO = memberService.findOneByEmailAndPwd(memMail, memPwd);

			if (memberDTO != null) {

				member = new MemberServiceImpl().getMemberFromMemberDTO(memberDTO);
				/* 登入次數 */
				Integer lgd = 0;
				if (member.getMemLgd() != null)
					lgd = member.getMemLgd();

				String memRdate = member.getMemRdate();
				Integer count = Until.getBetweenDateCount(memRdate);
				Integer accountValue = Until.getAccountValue(member.getMemLgd(), count, member.getMemIsvip());

				member.setMemSeq(accountValue);
				member.setMemLgd(lgd + 1);
				memberRepository.save(member);

				String memUuid = member.getMemUuid();

				HashMap<String, String> dataMap = new HashMap<String, String>();
				dataMap.put("memUuid", memUuid);

				String JWTtoken = JwtTokenUtils.generateToken(dataMap); // 取得token
				map.put("token", JWTtoken);

				logger.info("登入成功 mail = " + memMail);
			} else {
				states = ConfigInfo.DATA_FAIL;
				msg = "登入失敗 !  請檢查信箱與密碼是否輸入錯誤 !?";
			}

		} catch (Exception e) {
			e.printStackTrace();
			states = ConfigInfo.DATA_FAIL;
			msg = "登入失敗 !  請檢查信箱與密碼是否輸入錯誤 !?";
			logger.error("登入失敗 mail = " + memMail);
			logger.error(e.getMessage());
		}

		map.put("states", states);
		map.put("msg", msg);

		Gson gson = new Gson();

		return gson.toJson(map);
	}

	@ApiOperation("忘記密碼")
	@PostMapping(value = "/forget")
	protected String forget(HttpServletRequest request, @RequestBody SignUpDTO signUpDTO)
			throws ServletException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		String msg = ConfigInfo.SYS_MESSAGE_SUCCESS;
		String states = ConfigInfo.DATA_OK;
		String action = request.getServletPath().replace("/", "");
		try {
			MemberDTO memberDTO = memberService.findOneByEmail(signUpDTO.getMemMail());
			String mail = memberDTO.getMemMail();
			String memUuid = memberDTO.getMemUuid();
			if (memUuid != null) {
				SendMail sendMail = new SendMail();
				sendMail.sendMail_SugarDaddy(action, memUuid, mail);
			} else {
				states = ConfigInfo.DATA_NORESULT;
				msg = "請確認信箱";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		map.put("states", states);
		map.put("msg", msg);

		Gson gson = new Gson();

		return gson.toJson(map);
	}

	/**
	 * 忘記密碼導頁更改密碼 - 寄驗證信到信箱
	 */
	@ApiOperation("收信後更改密碼 - 寄驗證信到信箱從信箱點擊")
	@RequestMapping(value = "/forget/{memUuid}", method = RequestMethod.GET)
	public RedirectView updateAccount(RedirectAttributes attr, @PathVariable String memUuid) {
		MemberDTO memberDTO = new MemberDTO();
		memberDTO = memberService.findOneByUuid(memUuid);
		String url = "";
		if (memberDTO.getMemUuid() != null) {

			String memMail = memberDTO.getMemMail();
			attr.addAttribute("memMail", memMail);
			attr.addAttribute("memUuid", memUuid);
			url = ConfigInfo.REAL_PATH + "/sugardaddyDevelop/dist/resend-password";

		}
		return new RedirectView(url); // 重新導向到指定的url
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
