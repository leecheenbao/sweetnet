package com.sweetNet.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

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
import com.sweetNet.service.MemberInfoService;
import com.sweetNet.service.MemberService;
import com.sweetNet.until.AesHelper;
import com.sweetNet.until.Until;

/**
 * @author Paul
 */
@RestController
public class SweetNetController {
	@Autowired
	HttpServletRequest request;
	@Autowired
	HttpServletResponse response;
	@Autowired
	private MemberService memberService;
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private MemberInfoService memberinfoService;
	@Autowired
	private MemberInfoRepository memberInfoRepository;
	private Integer mem_sex;

	@RequestMapping(value = "/GET/users", produces = "application/json;charset=UTF-8")
	public Map getUserInfo() {
		Iterable<MemberInfo> mem;

		mem = memberInfoRepository.findAll();

		Map<String, Object> mapResult = new HashMap<>();
		for (MemberInfo m : mem) {
			mapResult.put("mem", mem);
		}
		System.out.println("test");
		return mapResult;
	}

	/**
	 * 會員登入
	 * 
	 * @param request
	 * @return JSONObject
	 */
	@RequestMapping(value = "/POST/Login", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public JSONObject accountLogin(HttpServletRequest request) {

		JSONObject jsonParam = Until.getJSONParam(request);
		Integer states = 200;
		String msg = "";
		String token = "";
		Member member = new Member();

		try {

			String mem_mail = jsonParam.getString("mem_mail");
			String mem_pwd = jsonParam.getString("mem_pwd");

			member.setMem_mail(mem_mail);

			Example<Member> example = Example.of(member);
			Optional<Member> result = memberRepository.findOne(example);

			if (result.isPresent()) {
				String pwd = AesHelper.decrypt(result.get().getMem_pwd());
				if (pwd.equals(mem_pwd)) {
					msg = "登入成功";
					token = String.valueOf(UUID.randomUUID());
				} else {
					msg = "請確認密碼";
				}
				System.out.println(pwd);
			} else {
				msg = "請確認信箱是否正確";
			}

		} catch (Exception e) {
			states = 500;
			msg = "系統發生問題";
		}

		JSONObject result = new JSONObject();
		result.put("states", states);
		result.put("msg", msg);
		result.put("token", token);

		return result;
	}

	/**
	 * 建立會員帳號
	 * 
	 * @param request
	 * @return JSONObject
	 */
	@RequestMapping(value = "/POST/member", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public JSONObject createAccount(HttpServletRequest request) {

		// 獲取到JSONObject
		JSONObject jsonParam = Until.getJSONParam(request);
		Member member = new Member();
		Integer states = 200;
		String msg = "";
		UUID uuid = UUID.randomUUID();
		String mail_regex = "^\\w{1,63}@[a-zA-Z0-9]{2,63}\\.[a-zA-Z]{2,63}(\\.[a-zA-Z]{2,63})?$";
		Pattern pattern = Pattern.compile(mail_regex);

		try {

			String mem_mail = jsonParam.getString("mem_mail");
			String mem_pwd = AesHelper.encrypt(jsonParam.getString("mem_pwd"));
			String mem_nickname = jsonParam.getString("mem_nickname");
			String mem_dep = jsonParam.getString("mem_dep");
			mem_sex = Integer.valueOf(jsonParam.getString("mem_sex"));

			member.setMem_mail(mem_mail);
			Example<Member> example = Example.of(member);
			Optional<Member> eresult = memberRepository.findOne(example);

			if (eresult.isPresent()) {
				if (mem_mail.equals(eresult.get().getMem_mail())) {
					states = 403;
					msg = "此信箱已註冊過";
				}
			}

			if (("").equals(mem_mail)) {
				msg = "請檢察Email";
				states = 403;
			} else if (!pattern.matcher(mem_mail).find()) {
				msg = "Email格式不正確";
				states = 403;
			} else if (("").equals(AesHelper.decrypt(mem_pwd)) || AesHelper.decrypt(mem_pwd).length() < 8) {
				msg = "請檢查密碼";
				states = 403;
			} else if (("").equals(mem_nickname)) {
				msg = "請檢查暱稱";
				states = 403;
			} else if (("").equals(mem_dep)) {
				msg = "請檢查";
				states = 403;
			} else if (("").equals(mem_sex)) {
				msg = "請檢查資料性別";
				states = 403;
			}

			member.setMem_uuid(String.valueOf(uuid));
			member.setMem_mail(mem_mail);
			member.setMem_pwd(mem_pwd);
			member.setMem_nickname(mem_nickname);
			member.setMem_dep(mem_dep);
			member.setMem_sex(mem_sex);

			if (states == 200) {
				memberService.save(member);
				msg = "OK";
			}

		} catch (Exception e) {
			states = 500;
			msg = "系統發生問題";
		}

		JSONObject result = new JSONObject();
		result.put("states", states);
		result.put("msg", msg);

		return result;
	}

	/**
	 * 填寫會員資料
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/POST/memberinfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public JSONObject createAccountInfo(HttpServletRequest request) {

		// 獲取到JSONObject
		JSONObject jsonParam = Until.getJSONParam(request);
		Member member = new Member();
		Integer states = 200;
		String msg = "";
		UUID uuid = UUID.randomUUID();
		String mail_regex = "^\\w{1,63}@[a-zA-Z0-9]{2,63}\\.[a-zA-Z]{2,63}(\\.[a-zA-Z]{2,63})?$";
		Pattern pattern = Pattern.compile(mail_regex);

		try {

			String mem_mail = jsonParam.getString("mem_mail");
			String mem_pwd = AesHelper.encrypt(jsonParam.getString("mem_pwd"));
			String mem_nickname = jsonParam.getString("mem_nickname");
			String mem_dep = jsonParam.getString("mem_dep");
			Integer mem_sex = Integer.valueOf(jsonParam.getString("mem_sex"));

			List<Member> accountList = memberRepository.findAll();
			for (Member m : accountList) {
				if (mem_mail.equals(m.getMem_mail())) {
					states = 403;
					msg = "此信箱已註冊過";
				}
			}

			if (("").equals(mem_mail)) {
				msg = "請檢察Email";
				states = 403;
			} else if (!pattern.matcher(mem_mail).find()) {
				msg = "Email格式不正確";
				states = 403;
			} else if (("").equals(AesHelper.decrypt(mem_pwd)) || AesHelper.decrypt(mem_pwd).length() < 8) {
				msg = "請檢查密碼";
				states = 403;
			} else if (("").equals(mem_nickname)) {
				msg = "請檢查暱稱";
				states = 403;
			} else if (("").equals(mem_dep)) {
				msg = "請檢查";
				states = 403;
			} else if (("").equals(mem_sex)) {
				msg = "請檢查資料性別";
				states = 403;
			}

			member.setMem_uuid(String.valueOf(uuid));
			member.setMem_mail(mem_mail);
			member.setMem_pwd(mem_pwd);
			member.setMem_nickname(mem_nickname);
			member.setMem_dep(mem_dep);
			member.setMem_sex(mem_sex);

			if (states != 1) {
				memberService.save(member);
				states = 0;
				msg = "OK";
			}

		} catch (Exception e) {
			states = 500;
			msg = "系統發生問題";
		}

		JSONObject result = new JSONObject();
		result.put("states", states);
		result.put("msg", msg);

		return result;
	}
}