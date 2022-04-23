package com.sweetNet.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
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

	/**
	 * 顯示會員資料 - 以性別分類男生只能看到女生，女生只能看到男生
	 * 
	 * @param request
	 * @return JSONObject
	 */
	@GetMapping(value = "/list/users")
	public JSONObject getUserInfoForSex() {
		// 獲取到JSONObject
		JSONObject jsonParam = Until.getJSONParam(request);
		JSONObject result = new JSONObject();
		JSONArray jsonArray = new JSONArray();

		String msg = "OK";
		Integer states = 200;

		try {
			/* 取得異性代碼 */
			Integer mem_sex = jsonParam.getInteger("mem_sex");
			if (mem_sex == 1) {
				mem_sex = 2;
			} else if (mem_sex == 2) {
				mem_sex = 1;
			}

			String token = jsonParam.getString("token");

			boolean isLogin = this.isLogin(token);

			if (isLogin) {

				JSONObject jsonData = new JSONObject();
				List<Member> memberList = (List<Member>) memberRepository.findByMemSex(mem_sex);

				List<Map<String, JSONObject>> dataList = new ArrayList<Map<String, JSONObject>>();
				for (Member member : memberList) {
					Map<String, JSONObject> map = new HashMap<String, JSONObject>();
					String uuid = member.getMemUuid();
					Optional<MemberInfo> memberInfoList = memberInfoRepository.findByMemUuidAndMemSta(uuid, 1);
					if (memberInfoList.isPresent()) {
						jsonData = memberInfoList.get().toJson();
						map.put("userInfo", jsonData);
					}

					if (!map.isEmpty()) {
						dataList.add(map);
					}

				}

				Gson gson = new Gson();

				String str = gson.toJson(dataList);
				jsonArray = JSON.parseArray(str);
				jsonArray.toJSONString();

				result.put("data", jsonArray);
			} else {
				msg = "token錯誤";
				states = 403;
				result.put("msg", msg);
				result.put("states", states);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		result.put("states", states);
		result.put("msg", msg);
		return result;
	}

	/**
	 * 顯示個人資料
	 * 
	 * @param request
	 * @return JSONObject
	 */
	@GetMapping(value = "/get/user")
	public JSONObject getUserInfo() {
		// 獲取到JSONObject
		JSONObject jsonParam = Until.getJSONParam(request);
		String msg = "OK";
		Integer states = 200;
		JSONObject result = new JSONObject();

		try {
			String mem_uuid = jsonParam.getString("mem_uuid");
			String token = jsonParam.getString("token");

			boolean isLogin = this.isLogin(token);

			if (isLogin) {

				MemberInfo memberInfo = new MemberInfo();
				memberInfo.setMemUuid(mem_uuid);

				Example<MemberInfo> example = Example.of(memberInfo);
				MemberInfo ud = memberInfoRepository.findOne(example).get();
				result.put("data", ud.toJson());
			} else {
				msg = "token錯誤";
				states = 403;
				result.put("msg", msg);
				result.put("states", states);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		result.put("states", states);
		result.put("msg", msg);
		return result;
	}

	/**
	 * 建立會員帳號
	 * 
	 * @param request
	 * @return JSONObject
	 */
	@PostMapping(value = "/post/member")
	public JSONObject createAccount(HttpServletRequest request) {

		// 獲取到JSONObject
		JSONObject jsonParam = Until.getJSONParam(request);
		Member member = new Member();
		List<String> msgList = new ArrayList<String>();
		Integer states = 200;
		String mail_regex = "^\\w{1,63}@[a-zA-Z0-9]{2,63}\\.[a-zA-Z]{2,63}(\\.[a-zA-Z]{2,63})?$";
		Pattern pattern = Pattern.compile(mail_regex);
		try {

			String mem_uuid = UUID.randomUUID().toString();
			String mem_mail = jsonParam.getString("mem_mail");
			String mem_pwd = AesHelper.encrypt(jsonParam.getString("mem_pwd"));
			String mem_nickname = jsonParam.getString("mem_nickname");
			String mem_dep = jsonParam.getString("mem_dep");
			Integer mem_sex = Integer.valueOf(jsonParam.getString("mem_sex"));

			member.setMemMail(mem_mail);
			Example<Member> example = Example.of(member);
			Optional<Member> eresult = memberRepository.findOne(example);

			if (eresult.isPresent()) {
				if (mem_mail.equals(eresult.get().getMemMail())) {
					states = 403;
					msgList.add("此信箱已註冊過");
				}
			}

			if (("").equals(mem_mail)) {
				msgList.add("請檢察Email");
				states = 403;
			} else if (!pattern.matcher(mem_mail).find()) {
				msgList.add("Email格式不正確");
				states = 403;
			} else if (("").equals(AesHelper.decrypt(mem_pwd)) || AesHelper.decrypt(mem_pwd).length() < 8) {
				msgList.add("請檢查密碼");
				states = 403;
			} else if (("").equals(mem_nickname)) {
				msgList.add("請檢查暱稱");
				states = 403;
			} else if (("").equals(mem_sex)) {
				msgList.add("請檢查資料性別");
				states = 403;
			}

			member.setMemUuid(mem_uuid);
			member.setMemMail(mem_mail);
			member.setMemPwd(mem_pwd);
			member.setMemNickname(mem_nickname);
			member.setMemDep(mem_dep);
			member.setMemSex(mem_sex);

			if (states == 200) {
				memberService.save(member);
				msgList.add("OK");
			}

		} catch (Exception e) {
			states = 500;
			msgList.add("系統發生問題");
		}

		JSONObject result = new JSONObject();
		result.put("states", states);
		result.put("msg", msgList);

		return result;
	}

	/**
	 * 填寫會員資料
	 * 
	 * @param request
	 * @return
	 */
	@PostMapping(value = "/post/memberinfo")
	public JSONObject createAccountInfo(HttpServletRequest request) {

		// 獲取到JSONObject
		JSONObject jsonParam = Until.getJSONParam(request);
		MemberInfo memberinfo = new MemberInfo();
		Integer states = 500;

		String phone_regex = "(09)+[\\d]{8}";
		Pattern pattern = Pattern.compile(phone_regex);
		List<String> msgList = new ArrayList<String>();
		try {

			String tokenCheck = null;
			String token = jsonParam.getString("token");
			Cookie[] cookies = request.getCookies();
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("token") && cookie.getValue().equals(token)) {
					tokenCheck = cookie.getValue();
				}
			}

			if (token.equals(tokenCheck)) {

				String mem_uuid = jsonParam.getString("mem_uuid");
				String mem_name = jsonParam.getString("mem_name");
				String mem_phone = jsonParam.getString("mem_phone");
				String mem_birthday = jsonParam.getString("mem_birthday");
				Integer mem_age = jsonParam.getInteger("mem_age");
				String mem_address = jsonParam.getString("mem_address");
				Integer mem_height = jsonParam.getInteger("mem_height");
				Integer mem_weight = jsonParam.getInteger("mem_weight");
				Integer mem_edu = jsonParam.getInteger("mem_edu");
				Integer mem_marry = jsonParam.getInteger("mem_marry");
				Integer mem_alcohol = jsonParam.getInteger("mem_alcohol");
				Integer mem_smoke = jsonParam.getInteger("mem_smoke");
				Integer mem_income = jsonParam.getInteger("mem_income");
				Integer mem_assets = jsonParam.getInteger("mem_assets");
				Integer mem_isvip = jsonParam.getInteger("mem_isvip");
				Integer mem_lgd = jsonParam.getInteger("mem_lgd");
				if (mem_lgd == null) {
					mem_lgd = 1;
				}

				// 今日日期
				SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd");
				Calendar calendar = Calendar.getInstance();
				Date dateObj = calendar.getTime();
				String mem_rdate = dtf.format(dateObj);
				Integer mem_sta = jsonParam.getInteger("mem_sta");
				if (mem_sta == null) {
					mem_sta = 1;
				}

				if (("").equals(mem_phone) || !pattern.matcher(mem_phone).find()) {
					states = 403;
					msgList.add("請輸入正確手機號碼");
				}

				memberinfo.setMemUuid(mem_uuid);
				memberinfo.setMemName(mem_name);
				memberinfo.setMemPhone(mem_phone);
				memberinfo.setMemBirthday(mem_birthday);
				memberinfo.setMemAge(mem_age);
				memberinfo.setMemAddress(mem_address);
				memberinfo.setMemHeight(mem_height);
				memberinfo.setMemWeight(mem_weight);
				memberinfo.setMemEdu(mem_edu);
				memberinfo.setMemMarry(mem_marry);
				memberinfo.setMemAlcohol(mem_alcohol);
				memberinfo.setMemSmoke(mem_smoke);
				memberinfo.setMemIncome(mem_income);
				memberinfo.setMemAssets(mem_assets);
				memberinfo.setMemIsvip(mem_isvip);
				memberinfo.setMemLgd(mem_lgd);
				memberinfo.setMemRdate(mem_rdate);
				memberinfo.setMemSta(mem_sta);

				memberinfoService.save(memberinfo);
				states = 200;
				msgList.add("OK");

			} else {
				states = 403;
				msgList.add("token不相符");
			}
		} catch (Exception e) {
			states = 500;
			msgList.add("系統發生問題");
		}

		JSONObject result = new JSONObject();
		result.put("states", states);
		result.put("msg", msgList);

		return result;
	}

	/*判斷是否登入*/
	private boolean isLogin(String token) {
		String tokenCheck = "";
		Cookie[] cookies = request.getCookies();
		boolean isLogin = false;
		for (Cookie cookie : cookies) {
			if ("token".equals(cookie.getName())) {
				tokenCheck = cookie.getValue();
				if (token.equals(tokenCheck)) {
					isLogin = true;
				}
			}
		}
		return isLogin;

	}
}