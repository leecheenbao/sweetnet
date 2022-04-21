package com.sweetNet.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
	@RequestMapping(value = "/POST/Login", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/plain;charset=UTF-8");
		JSONObject jsonParam = Until.getJSONParam(request);
		Member member = new Member();
		String jsonStr = "";

		// 移除cookie
		Cookie cookie = new Cookie("token", null);
		cookie.setMaxAge(0);
		response.addCookie(cookie);

		try (PrintWriter out = response.getWriter()) {
			System.out.println("test");
			// 驗證信箱、密碼
			String mem_mail = jsonParam.getString("mem_mail");
			String mem_pwd = AesHelper.encrypt(jsonParam.getString("mem_pwd"));

			jsonParam.remove("mem_pwd");
			member.setMem_mail(mem_mail);
			member.setMem_pwd(mem_pwd);
			Example<Member> example = Example.of(member);
			Optional<Member> eresult = memberRepository.findOne(example);

			if (eresult.isPresent()) {
				MemberInfo memberInfo = new MemberInfo();
				memberInfo.setMem_uuid(eresult.get().getMem_uuid());
				Example<MemberInfo> memberInfoExample = Example.of(memberInfo);
				MemberInfo ud = memberInfoRepository.findOne(memberInfoExample).get();

				String token = UUID.randomUUID().toString();
				int cookieAge = (int) (60 * 60 * 0.5); // 30 min
				cookie = new Cookie("token", token);
				cookie.setMaxAge(cookieAge);
				response.addCookie(cookie);

				jsonParam.put("token", token);
				jsonParam.put("msg", "登入成功！");
				jsonParam.put("status", "200");

				jsonStr = Until.organizeJson(jsonParam.toString(), ud.toJson().toString());

				out.println(jsonStr);
			} else {

				jsonParam.put("msg", "登入失敗 !  請檢查信箱與密碼是否輸入錯誤 !?");
				jsonParam.put("status", "500");

				jsonStr = Until.organizeJson(jsonParam.toString());

				out.println(jsonStr);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
