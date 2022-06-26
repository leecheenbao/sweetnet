package com.sweetNet.controller;

import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.security.auth.message.AuthException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.google.gson.Gson;
import com.infobip.sms.SendSmsBasic;
import com.sweetNet.dto.CityDTO;
import com.sweetNet.dto.MemberDTO;
import com.sweetNet.dto.MemberInfoDTO;
import com.sweetNet.dto.PhoneOtpDTO;
import com.sweetNet.dto.SignUpDTO;
import com.sweetNet.model.Member;
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
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", required = false, dataType = "String", name = "memMail", value = "會員電子郵件（）", example = "sweetnet@gmail.com"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "String", name = "memPwd", value = "會員電子郵件（需大於等於8碼）", example = "12345678"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "String", name = "memNickname", value = "會員暱稱", example = "black"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "String", name = "memDep", value = "會員自述", example = "我有很多錢$$$"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "Integer", name = "memSex", value = "性別（0：女生、1：男生）", example = "1") })
	@ApiOperation("建立會員帳號")
	@PostMapping(value = "/user")
	public String createAccount(@RequestBody @Valid SignUpDTO signUpDTO) {
		Member member = new Member();
		String msg = SystemInfo.SYS_MESSAGE_SUCCESS;
		String states = SystemInfo.DATA_OK;

		try {

			String memUuid = UUID.randomUUID().toString();
			String memMail = signUpDTO.getMemMail();
			String memPwd = AesHelper.encrypt(signUpDTO.getMemPwd());
			String memNickname = signUpDTO.getMemNickname();
			String memDep = signUpDTO.getMemDep();
			Integer memSex = signUpDTO.getMemSex();

			MemberDTO memberDTOcheck = memberService.findOneByEmail(memMail);
			if (memberDTOcheck.getMemMail() != null) {
				msg = SystemInfo.ALREADY_REGISTER;
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
			msg = SystemInfo.SYS_MESSAGE_ERROR;
			states = SystemInfo.DATA_FAIL;
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("states", states);
		map.put("msg", msg);

		Gson gson = new Gson();

		return gson.toJson(map);
	}

	/**
	 * 填寫會員資料
	 * 
	 * @param request
	 * @return
	 */
	@ApiImplicitParams({ @ApiImplicitParam(paramType = "header", name = "Authorization", value = "JWT Token"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "String", name = "memName", value = "姓名", example = "Paul"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "String", name = "memAddress", value = "地址", example = "XX區XX路XXX巷XXX號XX樓"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "Integer", name = "memAge", value = "年齡", example = "33"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "String", name = "memArea", value = "地址縣市", example = "KEE"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "String", name = "memAssets", value = "年齡", example = "33"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "Date", name = "memBirthday", value = "生日", example = "1989-10-10"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "Integer", name = "memAlcohol", value = "飲酒習慣", example = "1"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "String", name = "memDep", value = "會員自述", example = "我有很多錢$$$"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "Integer", name = "memEdu", value = "教育程度(0：其他、1：高中、2：大學、3：碩士、4：博士)", example = "3"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "Integer", name = "memHeight", value = "身高", example = "175"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "Integer", name = "memWeight", value = "體重", example = "70"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "Integer", name = "memIncome", value = "財力1~5（基礎、進階、高級、最高、可商議）", example = "1"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "Integer", name = "memIsvip", value = "會員狀態（不填）"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "Integer", name = "memMarry", value = "婚姻狀況（0：未婚、1：已婚）", example = "1"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "String", name = "memNickname", value = "會員暱稱", example = "black"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "String", name = "memPhone", value = "電話號碼", example = "0912345678"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "Integer", name = "memSmoke", value = "抽菸習慣（0：不抽、1：偶爾、2：經常）", example = "2"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "Integer", name = "phoneStates", value = "手機驗證狀態（不填）") })
	@ApiOperation("填寫會員資料")
	@PutMapping(value = "/user")
	public String createAccountInfo(@RequestHeader("Authorization") String au,
			@RequestBody MemberInfoDTO memberInfoDTO) {

		String token = au.substring(7);
		String memUuid = JwtTokenUtils.getJwtMemUuid(token); // 取得token

		String states = SystemInfo.DATA_ERR_SYS;
		String msg = "";
		Pattern pattern = Pattern.compile(SystemInfo.PHONE_REGEX);
		try {

			tokenCheck = JwtTokenUtils.validateToken(token);

			if (tokenCheck) {

				Integer memIsvip = 0;

				if (memberInfoDTO.getMemIsvip() != null) {
					memIsvip = Integer.valueOf(memberInfoDTO.getMemIsvip());
				}

				// 資料修改日期
				String mem_rdate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
				Integer memSta = 1;

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
				member.setMemName(memberInfoDTO.getMemName());
				member.setMemNickname(memberInfoDTO.getMemNickname());
				member.setMemPhone(memberInfoDTO.getMemPhone());
				member.setMemBirthday(memberInfoDTO.getMemBirthday());
				member.setMemAge(memberInfoDTO.getMemAge());
				member.setMemAddress(memberInfoDTO.getMemAddress());
				member.setMemArea(memberInfoDTO.getMemArea());
				member.setMemHeight(memberInfoDTO.getMemHeight());
				member.setMemWeight(memberInfoDTO.getMemWeight());
				member.setMemEdu(memberInfoDTO.getMemEdu());
				member.setMemMarry(memberInfoDTO.getMemMarry());
				member.setMemAlcohol(memberInfoDTO.getMemAlcohol());
				member.setMemSmoke(memberInfoDTO.getMemSmoke());
				member.setMemIncome(memberInfoDTO.getMemIncome());
				member.setMemAssets(memberInfoDTO.getMemAssets());
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

	@ApiImplicitParams({ @ApiImplicitParam(paramType = "header", name = "Authorization", value = "JWT Token"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "String", name = "memPhone", value = "電話號碼", example = "0919268790"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "String", name = "otp", value = "（不填）"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "String", name = "secret", value = "（不填）") })
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

	@ApiImplicitParams({ @ApiImplicitParam(paramType = "header", name = "Authorization", value = "JWT Token"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "String", name = "memPhone", value = "電話號碼（不填）"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "String", name = "otp", example = "354045"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "String", name = "secret", example = "6NMG67YDLUWNXQ3W") })
	@ApiOperation("驗證OTP簡訊")
	@PostMapping(value = "/OTP/verifyOTP")
	public String verifyOTP(@RequestHeader("Authorization") String au, @RequestBody PhoneOtpDTO phoneOtpDTO) {
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