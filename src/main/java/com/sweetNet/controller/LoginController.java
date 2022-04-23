package com.sweetNet.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.sweetNet.model.Member;
import com.sweetNet.model.MemberInfo;
import com.sweetNet.repository.MemberInfoRepository;
import com.sweetNet.repository.MemberRepository;
import com.sweetNet.until.AesHelper;
import com.sweetNet.until.Until;

@RestController
public class LoginController {

	@Autowired
	private MemberInfoRepository memberInfoRepository;
	@Autowired
	private MemberRepository memberRepository;

	/**
	 * 會員登入
	 * 
	 * @param request
	 * @return JSONObject
	 */
	@PostMapping(value = "/post/Login")
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/plain;charset=UTF-8");
		JSONObject jsonParam = Until.getJSONParam(request);
		Member member = new Member();
		String jsonStr = "";

		// 移除cookie
		Cookie tokenCookie = new Cookie("token", null);
		tokenCookie.setMaxAge(0);
		response.addCookie(tokenCookie);

		try (PrintWriter out = response.getWriter()) {
			// 驗證信箱、密碼
			String mem_mail = jsonParam.getString("mem_mail");
			String mem_pwd = AesHelper.encrypt(jsonParam.getString("mem_pwd"));

			jsonParam.remove("mem_pwd");
			member.setMemMail(mem_mail);
			member.setMemPwd(mem_pwd);
			Example<Member> example = Example.of(member);
			Optional<Member> eresult = memberRepository.findOne(example);
			if (eresult.isPresent()) {
				MemberInfo memberInfo = new MemberInfo();
				memberInfo.setMemUuid(eresult.get().getMemUuid());
				Example<MemberInfo> memberInfoExample = Example.of(memberInfo);
				MemberInfo ud = memberInfoRepository.findOne(memberInfoExample).get();

				String token = UUID.randomUUID().toString();
				int cookieAge = (int) (60 * 15 * 1); // 15 min

				tokenCookie.setMaxAge(cookieAge);
				tokenCookie = new Cookie("token", token);

				response.addCookie(tokenCookie);

				jsonParam.put("msg", "登入成功！");
				jsonParam.put("status", "200");

				jsonStr = Until.organizeJson(jsonParam.toString(), ud.toJson().toString());

				out.println(jsonStr);
			} else {

				jsonParam.put("msg", "登入失敗 !  請檢查信箱與密碼是否輸入錯誤 !?");
				jsonParam.put("status", "500");
				jsonStr = jsonParam.toJSONString();
//				jsonStr = Until.organizeJson(jsonParam.toString());

				out.println(jsonStr);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getDateTime() {
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMddhhmmss");
		Date date = new Date();
		String strDate = sdFormat.format(date);
		return strDate;
	}

}
