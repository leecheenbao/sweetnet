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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.sweetNet.model.Member;
import com.sweetNet.repository.MemberRepository;
import com.sweetNet.until.AesHelper;
import com.sweetNet.until.ConfigInfo;
import com.sweetNet.until.JwtTokenUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "LoginController")
@RestController
public class LoginController {

	@Autowired
	private MemberRepository memberRepository;

	/**
	 * 會員登入
	 * 
	 * @param request
	 * @return JSONObject
	 */
	@ApiOperation("登入")
	@PostMapping(value = "/login")
	protected void doPost(@RequestBody HashMap<String, String> user, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/plain;charset=UTF-8");
		JSONObject jsonParam = new JSONObject();
		Member member = new Member();
		String jsonStr = "";

		try (PrintWriter out = response.getWriter()) {

			// 驗證信箱、密碼
			String memMail = user.get("memMail");
			String memPwd = AesHelper.encrypt(user.get("memPwd"));

			member.setMemMail(memMail);
			member.setMemPwd(memPwd);
			Example<Member> example = Example.of(member);
			Optional<Member> eresult = memberRepository.findOne(example);
			if (eresult.isPresent()) {

				Example<Member> memberExample = Example.of(member);
				Member ud = memberRepository.findOne(memberExample).get();
				/* 登入次數 */
				Integer lgd = 0;
				if (ud.getMemLgd() != null)
					lgd = ud.getMemLgd();
				ud.setMemLgd(lgd + 1);

				memberRepository.save(ud);

				String memUuid = ud.getMemUuid();
				user.put("memUuid", memUuid);

				String JWTtoken = JwtTokenUtils.generateToken(user); // 取得token

				JSONObject userData = ud.toJson();
				userData.put("memMail", memMail);

				jsonParam.put("token", JWTtoken);
				jsonParam.put("msg", "登入成功！");
				jsonParam.put("status", ConfigInfo.DATA_OK);
				jsonParam.put("data", userData);
				jsonStr = jsonParam.toJSONString();

				out.println(jsonStr);
			} else {

				jsonParam.put("msg", "登入失敗 !  請檢查信箱與密碼是否輸入錯誤 !?");
				jsonParam.put("status", ConfigInfo.DATA_FAIL);
				jsonStr = jsonParam.toJSONString();

				out.println(jsonStr);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
}
