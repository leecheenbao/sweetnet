package com.sweetNet.controller;

import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.google.gson.Gson;
import com.infobip.sms.SendSmsBasic;
import com.sweetNet.dto.CityDTO;
import com.sweetNet.dto.MemberDTO;
import com.sweetNet.dto.MemberInfoDTO;
import com.sweetNet.dto.PhoneOtpDTO;
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
	public String createAccountInfo(@RequestHeader("Authorization") String au, MemberInfoDTO memberInfoDTO) {

		String token = au.substring(7);
		String memUuid = JwtTokenUtils.getJwtMemUuid(token); // 取得token

		String states = SystemInfo.DATA_ERR_SYS;
		String msg = "";
		Pattern pattern = Pattern.compile(SystemInfo.PHONE_REGEX);
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
					msg = SystemInfo.ERROR_PHONE;
				}
				Member member = new Member();
				member.setMemUuid(memUuid);
				MemberDTO memberDTO = memberService.findOneByUuid(memUuid);

				member.setMemPwd(memberDTO.getMemPwd());
				member.setMemMail(memberDTO.getMemMail());
				member.setMemPhone(memberDTO.getMemPhone());
				member.setMemSex(memberDTO.getMemSex());
				member.setMemDep(memberDTO.getMemDep());
				member.setMemLgd(memberDTO.getMemLgd());
				member.setPhoneStates(memberDTO.getPhoneStates());

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
				msg = SystemInfo.SYS_MESSAGE_SUCCESS;
			} else {

			}
		} catch (TokenExpiredException | AuthException | SignatureException e) {
			e.printStackTrace();
			states = SystemInfo.DATA_FAIL;
			msg = e.getMessage();
		}

		Map<String, String> map = new HashMap<String, String>();
		map.put("states", states);
		map.put("msg", msg);

		Gson gson = new Gson();

		return gson.toJson(map);
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
	public List<MemberDTO> getUserInfoBySex(@RequestHeader("Authorization") String au) {

		String token = au.substring(7);
		List<MemberDTO> memberDTOs = new ArrayList<MemberDTO>();
		try {

			String memUuid = JwtTokenUtils.getJwtMemUuid(token);
			tokenCheck = JwtTokenUtils.validateToken(token);

			if (tokenCheck) {

				Member member = new Member();
				member.setMemUuid(memUuid);

				MemberDTO memberDTO = memberService.findOneByUuid(memUuid);

				Integer memSex = memberDTO.getMemSex();

				/* 取得異性代碼 */
				if (memSex == 1) {
					memSex = 2;
					memberDTOs = memberService.findByMemSex(memSex);
				} else if (memSex == 2) {
					memSex = 1;
					memberDTOs = memberService.findByMemSex(memSex);
				} else if (memSex == 0) {
					memberDTOs = memberService.findAll();
				}

			}
		} catch (TokenExpiredException | AuthException | SignatureException e) {
			e.printStackTrace();
		}

		return memberDTOs;
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
	public String sendSMS(@RequestHeader("Authorization") String au, PhoneOtpDTO phoneOtpDTO) {
		Gson gson = new Gson();
		Map<String, String> map = new HashMap<String, String>();

		String token = au.substring(7);
		String msg = SystemInfo.SYS_MESSAGE_SUCCESS;
		String states = SystemInfo.DATA_OK;

		try {

			tokenCheck = JwtTokenUtils.validateToken(token);

			if (tokenCheck) {
				PhoneUtil pu = new PhoneUtil();
				String recipient = pu.checkPhone(phoneOtpDTO.getMemPhone());
				String secret = Base32.random();
				String OTP = pu.creatOTP(secret);

				SendSmsBasic.sendSMS(recipient, this.OTPMsg(OTP));

				map.put("OTP", OTP);
				map.put("secret", secret);
			}

		} catch (TokenExpiredException | AuthException | SignatureException e) {
			e.printStackTrace();
			states = SystemInfo.DATA_FAIL;
			msg = e.getMessage();
		}

		map.put("states", states);
		map.put("msg", msg);

		return gson.toJson(map);
	}

	@ApiOperation("驗證OTP簡訊")
	@PostMapping(value = "/OTP/verifyOTP")
	public String verifyOTP(@RequestHeader("Authorization") String au, PhoneOtpDTO phoneOtpDTO) {
		Gson gson = new Gson();
		Map<String, String> map = new HashMap<String, String>();

		String token = au.substring(7);
		String msg = SystemInfo.SYS_MESSAGE_SUCCESS;
		String states = SystemInfo.DATA_OK;

		try {
			tokenCheck = JwtTokenUtils.validateToken(token);
			String memUuid = JwtTokenUtils.getJwtMemUuid(token);
			if (tokenCheck) {
				PhoneUtil pu = new PhoneUtil();
				String OTP = phoneOtpDTO.getOtp();
				String secret = phoneOtpDTO.getSecret();
				Boolean verifyOTP = pu.verifyOTP(secret, OTP);

				if (verifyOTP) {
					Member member = new Member();
					member.setMemUuid(memUuid);
					Example<Member> memberExample = Example.of(member);
					member = memberRepository.findOne(memberExample).get();

					member.setPhoneStates(1);
					memberService.save(member);
				}
				map.put("verifyOTP", String.valueOf(verifyOTP));

			}

		} catch (TokenExpiredException | AuthException | SignatureException e) {
			e.printStackTrace();
			states = SystemInfo.DATA_FAIL;
			msg = e.getMessage();
		}

		map.put("states", states);
		map.put("msg", msg);
		return gson.toJson(map);
	}

	public Map<String, String> resultMap(String msg, String states) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("msg", msg);
		map.put("states", states);
		return map;
	}

	public String OTPMsg(String otp) {
		String otpMessage = "您的OTP驗證碼： " + otp + " 請於60秒內輸入驗證";
		return otpMessage;
	}

}