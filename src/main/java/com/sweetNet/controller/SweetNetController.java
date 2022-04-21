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

	Integer states = 200;
	List<String> msgList = new ArrayList<String>();

	@RequestMapping(value = "/GET/users", produces = "application/json;charset=UTF-8")
	public Map getUserInfo() {
		Iterable<MemberInfo> mem;

		mem = memberInfoRepository.findAll();

		Map<String, Object> mapResult = new HashMap<>();
		for (MemberInfo m : mem) {
			mapResult.put("data", mem);
		}
		return mapResult;
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

		String mail_regex = "^\\w{1,63}@[a-zA-Z0-9]{2,63}\\.[a-zA-Z]{2,63}(\\.[a-zA-Z]{2,63})?$";
		Pattern pattern = Pattern.compile(mail_regex);
		msgList.removeAll(msgList);
		try {

			String mem_uuid = UUID.randomUUID().toString();
			String mem_mail = jsonParam.getString("mem_mail");
			String mem_pwd = AesHelper.encrypt(jsonParam.getString("mem_pwd"));
			String mem_nickname = jsonParam.getString("mem_nickname");
			String mem_dep = jsonParam.getString("mem_dep");
			Integer mem_sex = Integer.valueOf(jsonParam.getString("mem_sex"));

			member.setMem_mail(mem_mail);
			Example<Member> example = Example.of(member);
			Optional<Member> eresult = memberRepository.findOne(example);

			if (eresult.isPresent()) {
				if (mem_mail.equals(eresult.get().getMem_mail())) {
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

			member.setMem_uuid(mem_uuid);
			member.setMem_mail(mem_mail);
			member.setMem_pwd(mem_pwd);
			member.setMem_nickname(mem_nickname);
			member.setMem_dep(mem_dep);
			member.setMem_sex(mem_sex);

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
	@RequestMapping(value = "/POST/memberinfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public JSONObject createAccountInfo(HttpServletRequest request) {

		// 獲取到JSONObject
		JSONObject jsonParam = Until.getJSONParam(request);
		MemberInfo memberinfo = new MemberInfo();
		Integer states = 200;

		String phone_regex = "(09)+[\\d]{8}";
		Pattern pattern = Pattern.compile(phone_regex);
		msgList.removeAll(msgList);
		try {

			String meminfo_uuid = jsonParam.getString("meminfo_uuid");
			if (("").equals(meminfo_uuid)) {
				meminfo_uuid = UUID.randomUUID().toString();
			}

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

			memberinfo.setMeminfo_uuid(meminfo_uuid);
			memberinfo.setMem_uuid(mem_uuid);
			memberinfo.setMem_name(mem_name);
			memberinfo.setMem_phone(mem_phone);
			memberinfo.setMem_birthday(mem_birthday);
			memberinfo.setMem_age(mem_age);
			memberinfo.setMem_address(mem_address);
			memberinfo.setMem_height(mem_height);
			memberinfo.setMem_weight(mem_weight);
			memberinfo.setMem_edu(mem_edu);
			memberinfo.setMem_marry(mem_marry);
			memberinfo.setMem_alcohol(mem_alcohol);
			memberinfo.setMem_smoke(mem_smoke);
			memberinfo.setMem_income(mem_income);
			memberinfo.setMem_assets(mem_assets);
			memberinfo.setMem_isvip(mem_isvip);
			memberinfo.setMem_lgd(mem_lgd);
			memberinfo.setMem_rdate(mem_rdate);
			memberinfo.setMem_sta(mem_sta);

			if (states == 200) {
				memberinfoService.save(memberinfo);
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
}