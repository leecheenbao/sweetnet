package com.sweetNet.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.sweetNet.model.Member;
import com.sweetNet.model.MemberImage;
import com.sweetNet.repository.MemberImageRepository;
import com.sweetNet.repository.MemberRepository;
import com.sweetNet.service.MemberImageService;
import com.sweetNet.service.MemberService;
import com.sweetNet.until.AesHelper;
import com.sweetNet.until.ConfigInfo;
import com.sweetNet.until.JwtTokenUtils;
import com.sweetNet.until.Until;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author Paul
 */
@Api(tags = "MemberController")
@RestController
@RequestMapping("/api")
public class MemberController {
	@Autowired
	HttpServletRequest request;
	@Autowired
	HttpServletResponse response;
	@Autowired
	private MemberService memberService;
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private MemberImageService memberImageService;
	@Autowired
	private MemberImageRepository memberImageRepository;

	private static Boolean tokenCheck = false;

	/**
	 * 建立會員帳號
	 * 
	 * @param request
	 * @return JSONObject
	 */
	@ApiOperation("建立會員帳號")
	@PostMapping(value = "/user")
	public JSONObject createAccount(@RequestBody HashMap<String, String> user) {

		// 獲取到JSONObject
		JSONObject jsonParam = Until.getJSONParam(request);
		Member member = new Member();
		List<String> msgList = new ArrayList<String>();

		String states = ConfigInfo.DATA_OK;

		String mail_regex = "^\\w{1,63}@[a-zA-Z0-9]{2,63}\\.[a-zA-Z]{2,63}(\\.[a-zA-Z]{2,63})?$";
		Pattern pattern = Pattern.compile(mail_regex);
		try {

			String memUuid = UUID.randomUUID().toString();
			String memMail = user.get("memMail");
			String memPwd = AesHelper.encrypt(user.get("memPwd"));
			String memNickname = user.get("memNickname");
			String memDep = user.get("memDep");
			Integer memSex = Integer.valueOf(user.get("memSex"));

			member.setMemMail(memMail);
			Example<Member> example = Example.of(member);
			Optional<Member> eresult = memberRepository.findOne(example);

			if (eresult.isPresent()) {
				if (memMail.equals(eresult.get().getMemMail())) {
					states = ConfigInfo.DATA_FAIL;
					msgList.add("此信箱已註冊過");
				}
			}

			if (("").equals(memMail)) {
				msgList.add("請檢察Email");
				states = ConfigInfo.DATA_FAIL;
			} else if (!pattern.matcher(memMail).find()) {
				msgList.add("Email格式不正確");
				states = ConfigInfo.DATA_FAIL;
			} else if (("").equals(AesHelper.decrypt(memPwd)) || AesHelper.decrypt(memPwd).length() < 8) {
				msgList.add("請檢查密碼");
				states = ConfigInfo.DATA_FAIL;
			} else if (("").equals(memNickname)) {
				msgList.add("請檢查暱稱");
				states = ConfigInfo.DATA_FAIL;
			} else if (("").equals(memSex)) {
				msgList.add("請檢查資料性別");
				states = ConfigInfo.DATA_FAIL;
			}

			member.setMemUuid(memUuid);
			member.setMemMail(memMail);
			member.setMemPwd(memPwd);
			member.setMemNickname(memNickname);
			member.setMemDep(memDep);
			member.setMemSex(memSex);

			if (states == ConfigInfo.DATA_OK) {
				memberService.save(member);
				msgList.add("OK");
			}

		} catch (Exception e) {
			states = ConfigInfo.DATA_ERR_SYS;
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
	@ApiOperation("填寫會員資料")
	@PutMapping(value = "/user")
	public JSONObject createAccountInfo(@RequestHeader("Authorization") String au,
			@ApiParam("UUID：memUuid、姓名：memName、電話：memPhone、生日(YYYY/MM/DD)：memBirthday、年齡：memAge、身高：memHeight、"
					+ "體重：memWeight、教育程度(1：高中、2：二專五專、3：學士、4：碩士、5:博士、)：memEdu、婚姻狀況(1未婚2已婚)：memMarry、飲酒習慣(1經常2偶爾3從不)：memAlcohol、吸菸習慣(1經常2偶爾3從不)：memSmoke、年收入：memIncome、"
					+ "資產(萬)：memAssets、VIP狀態(0：一般、1：VIP):memIsvip") @RequestBody HashMap<String, String> user) {

		String JWTtoken = JwtTokenUtils.generateToken(user); // 取得token
		String states = ConfigInfo.DATA_ERR_SYS;

		String phone_regex = "(09)+[\\d]{8}";
		Pattern pattern = Pattern.compile(phone_regex);
		List<String> msgList = new ArrayList<String>();
		try {

			tokenCheck = JwtTokenUtils.validateToken(JWTtoken);

			if (tokenCheck) {
				String token = au.substring(7);
				String memUuid = JwtTokenUtils.getJwtMemUuid(token);
				String memName = user.get("memName");
				String memPhone = user.get("memPhone");
				String memBirthday = user.get("memBirthday");
				Integer memAge = Integer.valueOf(user.get("memAge"));
				String memAddress = user.get("memAddress");
				Integer memHeight = Integer.valueOf(user.get("memHeight"));
				Integer memWeight = Integer.valueOf(user.get("memWeight"));
				Integer memEdu = Integer.valueOf(user.get("memEdu"));
				Integer memMarry = Integer.valueOf(user.get("memMarry"));
				Integer memAlcohol = Integer.valueOf(user.get("memAlcohol"));
				Integer memSmoke = Integer.valueOf(user.get("memSmoke"));
				Integer memIncome = Integer.valueOf(user.get("memIncome"));
				Integer memAssets = Integer.valueOf(user.get("memAssets"));
				Integer memIsvip = 0;

				if (user.get("memIsvip") != null) {
					memIsvip = Integer.valueOf(user.get("memIsvip"));
				}

				// 今日日期
				SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd");
				Calendar calendar = Calendar.getInstance();
				Date dateObj = calendar.getTime();
				String mem_rdate = dtf.format(dateObj);
				Integer memSta = 1;

				if (("").equals(memPhone) || !pattern.matcher(memPhone).find()) {
					states = ConfigInfo.DATA_FAIL;
					msgList.add("請輸入正確手機號碼");
				}
				Member member = new Member();
				member.setMemUuid(memUuid);
				Example<Member> memberExample = Example.of(member);
				member = memberRepository.findOne(memberExample).get();

				member.setMemUuid(memUuid);
				member.setMemName(memName);
				member.setMemPhone(memPhone);
				member.setMemBirthday(memBirthday);
				member.setMemAge(memAge);
				member.setMemAddress(memAddress);
				member.setMemHeight(memHeight);
				member.setMemWeight(memWeight);
				member.setMemEdu(memEdu);
				member.setMemMarry(memMarry);
				member.setMemAlcohol(memAlcohol);
				member.setMemSmoke(memSmoke);
				member.setMemIncome(memIncome);
				member.setMemAssets(memAssets);
				member.setMemIsvip(memIsvip);
				member.setMemRdate(mem_rdate);
				member.setMemSta(memSta);
				memberService.save(member);
				states = ConfigInfo.DATA_OK;
				msgList.add("Insert Success");
			} else {

			}
		} catch (Exception e) {
			states = ConfigInfo.DATA_ERR_SYS;
			msgList.add("系統發生問題");
		}

		JSONObject result = new JSONObject();
		result.put("states", states);
		result.put("msg", msgList);

		return result;
	}

	/**
	 * 顯示個人資料
	 * 
	 * @param request
	 * @return JSONObject
	 */
	@ApiOperation("顯示個人資料")
	@GetMapping(value = "/user")
	public JSONObject getUserInfo(@RequestHeader("Authorization") String au) {
		String token = au.substring(7);
		String memUuid = JwtTokenUtils.getJwtMemUuid(token); // 取得token

		String msg = "failed";
		String states = ConfigInfo.DATA_FAIL;
		JSONObject result = new JSONObject();

		try {

			tokenCheck = JwtTokenUtils.validateToken(token);

			if (tokenCheck) {

				Member member = new Member();
				member.setMemUuid(memUuid);

				Example<Member> example = Example.of(member);
				Member ud = memberRepository.findOne(example).get();
				result.put("data", ud.toJson());
				states = ConfigInfo.DATA_OK;
				msg = "success";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		result.put("states", states);
		result.put("msg", msg);
		return result;
	}

	/**
	 * 顯示會員資料 - 以性別分類男生只能看到女生，女生只能看到男生
	 * 
	 * @param request
	 * @return JSONObject
	 */
	@ApiOperation("顯示會員資料 - 以登入會員的性別分類，男生只能看到女生、女生只能看到男生")
	@GetMapping(value = "/users")
	public JSONObject getUserInfoForSex(@RequestHeader("Authorization") String au) {
		JSONObject result = new JSONObject();
		String token = au.substring(7);
		String msg = "success";
		String states = ConfigInfo.DATA_OK;

		try {

			String memUuid = JwtTokenUtils.getJwtMemUuid(token);
			tokenCheck = JwtTokenUtils.validateToken(token);

			if (tokenCheck) {

				Member member = new Member();
				member.setMemUuid(memUuid);
				Example<Member> example = Example.of(member);
				Optional<Member> memberData = memberRepository.findOne(example);
				Integer memSex = memberData.get().getMemSex();

				List<Member> memberList = null;
				
				/* 取得異性代碼 */
				if (memSex == 1) {
					memSex = 2;
					memberList = (List<Member>) memberRepository.findByMemSex(memSex);
				} else if (memSex == 2) {
					memSex = 1;
					memberList = (List<Member>) memberRepository.findByMemSex(memSex);
				} else if (memSex == 0) {
					memberList = (List<Member>) memberRepository.findAll();
				}

				Gson gson = new Gson();
				JSONArray jsonArray = new JSONArray();

				jsonArray = JSON.parseArray(gson.toJson(memberList));
				/* 整理json */
				for (int i = 0; i < jsonArray.size(); i++) {
					JSONObject j = (JSONObject) jsonArray.get(i);
					List<MemberImage> memberImageList = memberImageRepository.findByMemUuid((String) j.get("memUuid"));
					j.put("ImageData", memberImageList);
					// j.put(key, value)
					j.remove("memPwd");
				}
				result.put("data", jsonArray);
			}
		} catch (Exception e) {
			e.printStackTrace();
			states = ConfigInfo.DATA_FAIL;
			msg = "error";
		}

		result.put("states", states);
		result.put("msg", msg);
		return result;
	}

}