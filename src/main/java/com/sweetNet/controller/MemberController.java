package com.sweetNet.controller;

import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.security.auth.message.AuthException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.aerogear.security.otp.api.Base32;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.google.gson.Gson;
import com.infobip.sms.SendSmsBasic;
import com.sweetNet.dto.CityDTO;
import com.sweetNet.dto.MemberDTO;
import com.sweetNet.dto.MemberInfoDTO;
import com.sweetNet.dto.SignUpDTO;
import com.sweetNet.model.Member;
import com.sweetNet.model.MemberImage;
import com.sweetNet.repository.MemberImageRepository;
import com.sweetNet.repository.MemberRepository;
import com.sweetNet.service.CityService;
import com.sweetNet.service.MemberImageService;
import com.sweetNet.service.MemberService;
import com.sweetNet.until.AesHelper;
import com.sweetNet.until.JwtTokenUtils;
import com.sweetNet.until.PhoneUtil;
import com.sweetNet.until.SystemInfo;

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

	@Autowired
	private CityService cityService;
	private static Boolean tokenCheck = false;

	/**
	 * 建立會員帳號
	 * 
	 * @param request
	 * @return JSONObject
	 */
	@ApiOperation("建立會員帳號")
	@PostMapping(value = "/user")
	public void createAccount(SignUpDTO signUpDTO) {
		Member member = new Member();
		List<String> msgList = new ArrayList<String>();

		String states = SystemInfo.DATA_OK;
		String mail_regex = "^\\w{1,63}@[a-zA-Z0-9]{2,63}\\.[a-zA-Z]{2,63}(\\.[a-zA-Z]{2,63})?$";
		Pattern pattern = Pattern.compile(mail_regex);
		try {

			String memUuid = UUID.randomUUID().toString();
			String memMail = signUpDTO.getMemMail();
			String memPwd = AesHelper.encrypt(signUpDTO.getMemPwd());
			String memNickname = signUpDTO.getMemNickname();
			String memDep = signUpDTO.getMemDep();
			Integer memSex = signUpDTO.getMemSex();

			MemberDTO memberDTOcheck = memberService.findOneByEmail(memMail);
			if (memberDTOcheck.getMemMail() != null) {
				states = SystemInfo.DATA_FAIL;
				msgList.add("此信箱已註冊過");
			}

			if (("").equals(memMail)) {
				msgList.add("請檢察Email");
				states = SystemInfo.DATA_FAIL;
			} else if (!pattern.matcher(memMail).find()) {
				msgList.add("Email格式不正確");
				states = SystemInfo.DATA_FAIL;
			} else if (("").equals(AesHelper.decrypt(memPwd)) || AesHelper.decrypt(memPwd).length() < 8) {
				msgList.add("請檢查密碼");
				states = SystemInfo.DATA_FAIL;
			} else if (("").equals(memNickname)) {
				msgList.add("請檢查暱稱");
				states = SystemInfo.DATA_FAIL;
			} else if (("").equals(memSex)) {
				msgList.add("請檢查資料性別");
				states = SystemInfo.DATA_FAIL;
			}

			if (states.equals(SystemInfo.DATA_OK)) {
				member.setMemUuid(memUuid);
				member.setMemMail(memMail);
				member.setMemPwd(memPwd);
				member.setMemNickname(memNickname);
				member.setMemDep(memDep);
				member.setMemSex(memSex);
				memberService.save(member);
			}
		} catch (Exception e) {
			e.printStackTrace();
			states = SystemInfo.DATA_FAIL;
		}

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
			@ApiParam("UUID：memUuid、姓名：memName、電話：memPhone、生日(YYYY/MM/DD)：memBirthday、地址:memAddress、縣市區域：memArea、年齡：memAge、身高：memHeight、"
					+ "體重：memWeight、教育程度(1：高中、2：二專五專、3：學士、4：碩士、5:博士、)：memEdu、婚姻狀況(1未婚2已婚)：memMarry、飲酒習慣(1經常2偶爾3從不)：memAlcohol、吸菸習慣(1經常2偶爾3從不)：memSmoke、年收入：memIncome、"
					+ "資產(萬)：memAssets、VIP狀態(0：一般、1：VIP):memIsvip") MemberInfoDTO memberInfoDTO) {
		String token = au.substring(7);
		String memUuid = JwtTokenUtils.getJwtMemUuid(token); // 取得token

		String states = SystemInfo.DATA_ERR_SYS;
		String msg = "";
		String phone_regex = "(09)+[\\d]{8}";
		Pattern pattern = Pattern.compile(phone_regex);
		try {

			tokenCheck = JwtTokenUtils.validateToken(token);

			if (tokenCheck) {
				String memName = memberInfoDTO.getMemName();
				String memNickname = memberInfoDTO.getMemNickname();
				String memPhone = memberInfoDTO.getMemPhone();
				String memBirthday = memberInfoDTO.getMemBirthday();
				Integer memAge = Integer.valueOf(memberInfoDTO.getMemAge());
				String memAddress = memberInfoDTO.getMemAddress();
				String memArea = memberInfoDTO.getMemArea();
				Integer memHeight = Integer.valueOf(memberInfoDTO.getMemHeight());
				Integer memWeight = Integer.valueOf(memberInfoDTO.getMemWeight());
				Integer memEdu = Integer.valueOf(memberInfoDTO.getMemEdu());
				Integer memMarry = Integer.valueOf(memberInfoDTO.getMemEdu());
				Integer memAlcohol = Integer.valueOf(memberInfoDTO.getMemAlcohol());
				Integer memSmoke = Integer.valueOf(memberInfoDTO.getMemSmoke());
				Integer memIncome = Integer.valueOf(memberInfoDTO.getMemIncome());
				Integer memAssets = Integer.valueOf(memberInfoDTO.getMemAssets());
				Integer memIsvip = 0;

				if (memberInfoDTO.getMemIsvip() != null) {
					memIsvip = Integer.valueOf(memberInfoDTO.getMemIsvip());
				}

				// 今日日期
				SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd");
				Calendar calendar = Calendar.getInstance();
				Date dateObj = calendar.getTime();
				String mem_rdate = dtf.format(dateObj);
				Integer memSta = 1;

				if (("").equals(memPhone) || !pattern.matcher(memPhone).find()) {
					states = SystemInfo.DATA_FAIL;
					msg = "請輸入正確手機號碼";
				}
				Member member = new Member();
				member.setMemUuid(memUuid);
				MemberDTO memberDTO = memberService.findOneByUuid(memUuid);

				member.setMemUuid(memUuid);
				member.setMemName(memName);
				member.setMemNickname(memNickname);
				member.setMemPhone(memPhone);
				member.setMemBirthday(memBirthday);
				member.setMemAge(memAge);
				member.setMemAddress(memAddress);
				member.setMemArea(memArea);
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
				states = SystemInfo.DATA_OK;
				msg = "Insert Success";
			} else {

			}
		} catch (TokenExpiredException | AuthException | SignatureException e) {
			e.printStackTrace();
			states = SystemInfo.DATA_FAIL;
			msg = e.getMessage();
		}

		JSONObject result = new JSONObject();
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
	@ApiOperation("顯示個人資料")
	@GetMapping(value = "/user")
	public MemberDTO getUserInfo(@RequestHeader("Authorization") String au) {
		String token = au.substring(7);
		String memUuid = JwtTokenUtils.getJwtMemUuid(token); // 取得token

		MemberDTO memberDTO = new MemberDTO();

		try {

			tokenCheck = JwtTokenUtils.validateToken(token);

			if (tokenCheck) {

				memberDTO = memberService.findOneByUuid(memUuid);

				List<MemberImage> memberImageList = memberImageRepository.findByMemUuid(memUuid);

			}
		} catch (TokenExpiredException | AuthException | SignatureException e) {
			e.printStackTrace();
		}

		return memberDTO;
	}

	/**
	 * 顯示會員資料 - 以性別分類男生只能看到女生，女生只能看到男生
	 * 
	 * @param request
	 * @return JSONObject
	 */
	@ApiOperation("顯示會員資料 - 以登入會員的性別分類，男生只能看到女生、女生只能看到男生並加入以縣市搜尋")
	@GetMapping(value = "/users/{city}")
	public List<MemberDTO> getUserInfoByCity(@RequestHeader("Authorization") String au, @PathVariable String city) {
		String token = au.substring(7);

		List<MemberDTO> memberDTOs = new ArrayList<MemberDTO>();
		try {

			String memUuid = JwtTokenUtils.getJwtMemUuid(token);
			tokenCheck = JwtTokenUtils.validateToken(token);

			if (tokenCheck) {

				MemberDTO memberDTO = memberService.findOneByUuid(memUuid);

				Integer memSex = memberDTO.getMemSex();
				String memArea = city;

				/* 取得異性代碼 */
				if (memSex == 1) {
					memSex = 2;
					memberDTOs = memberService.findByMemSexAndMemArea(memSex, memArea);
				} else if (memSex == 2) {
					memSex = 1;
					memberDTOs = memberService.findByMemSexAndMemArea(memSex, memArea);
				}

				if (memSex == 0) {
					memberDTOs = memberService.findAll();
				}

			}
		} catch (TokenExpiredException | AuthException | SignatureException e) {
			e.printStackTrace();
		}

		return memberDTOs;
	}

	/**
	 * 顯示會員資料 - 以性別分類男生只能看到女生，女生只能看到男生
	 * 
	 * @param request
	 * @return JSONObject
	 */
	@ApiOperation("顯示會員資料 - 以登入會員的性別分類，男生只能看到女生、女生只能看到男生")
	@GetMapping(value = "/users")
	public JSONObject getUserInfoBySex(@RequestHeader("Authorization") String au) {
		JSONObject result = new JSONObject();
		String token = au.substring(7);
		String msg = "success";
		String states = SystemInfo.DATA_OK;

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
		} catch (TokenExpiredException | AuthException | SignatureException e) {
			e.printStackTrace();
			states = SystemInfo.DATA_FAIL;
			msg = e.getMessage();
		}

		result.put("states", states);
		result.put("msg", msg);
		return result;
	}

	@ApiOperation("取得縣市資料")
	@GetMapping(value = "/country")
	public List<CityDTO> getCountry() {
		List<CityDTO> cityDTOs = null;
		try {
			cityDTOs = cityService.findAll();
			System.out.println(cityDTOs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cityDTOs;
	}

	@ApiOperation("發送OTP簡訊")
	@PostMapping(value = "/OTP/sendOTP")
	public JSONObject sendSMS(@RequestHeader("Authorization") String au, @RequestBody HashMap<String, String> user) {
		JSONObject result = new JSONObject();
		String token = au.substring(7);
		String msg = "success";
		String states = SystemInfo.DATA_OK;

		try {

			String memUuid = JwtTokenUtils.getJwtMemUuid(token);
			tokenCheck = JwtTokenUtils.validateToken(token);

			if (tokenCheck) {
				PhoneUtil pu = new PhoneUtil();
				String phoneNumber = user.get("phoneNumber");
				String recipient = pu.checkPhone(phoneNumber);
				String secret = Base32.random();
				String OTP = pu.creatOTP(secret);
				String messageText = String.valueOf(OTP);

				SendSmsBasic ssb = new SendSmsBasic();
				ssb.sendSMS(recipient, messageText);

				result.put("secret", secret);
				result.put("OTP", OTP);
			}

		} catch (TokenExpiredException | AuthException | SignatureException e) {
			e.printStackTrace();
			states = SystemInfo.DATA_FAIL;
			msg = e.getMessage();
		}
		result.put("states", states);
		result.put("msg", msg);
		return result;
	}

	@ApiOperation("驗證OTP簡訊")
	@PostMapping(value = "/OTP/verifyOTP")
	public JSONObject verifyOTP(@RequestHeader("Authorization") String au, @RequestBody HashMap<String, String> user) {
		JSONObject result = new JSONObject();
		String token = au.substring(7);
		String msg = "success";
		String states = SystemInfo.DATA_OK;

		try {
			tokenCheck = JwtTokenUtils.validateToken(token);
			String memUuid = JwtTokenUtils.getJwtMemUuid(token);
			if (tokenCheck) {
				PhoneUtil pu = new PhoneUtil();
				String OTP = user.get("OTP");
				String secret = user.get("secret");
				Boolean verifyOTP = pu.verifyOTP(secret, OTP);

				if (verifyOTP) {
					Member member = new Member();
					member.setMemUuid(memUuid);
					Example<Member> memberExample = Example.of(member);
					member = memberRepository.findOne(memberExample).get();

					member.setPhoneStates(1);
					;
					memberService.save(member);
				}
				result.put("verifyOTP", verifyOTP);

			}

		} catch (TokenExpiredException | AuthException | SignatureException e) {
			e.printStackTrace();
			states = SystemInfo.DATA_FAIL;
			msg = e.getMessage();
		}

		result.put("states", states);
		result.put("msg", msg);
		return result;
	}

}