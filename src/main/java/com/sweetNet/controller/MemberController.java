package com.sweetNet.controller;

import java.io.IOException;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.security.auth.message.AuthException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.servlet.view.RedirectView;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.google.gson.Gson;
import com.sweetNet.dto.MailDTO;
import com.sweetNet.dto.MemberDTO;
import com.sweetNet.dto.MemberInfoDTO;
import com.sweetNet.dto.PhoneOtpDTO;
import com.sweetNet.dto.SearchConditionDTO;
import com.sweetNet.dto.SignUpDTO;
import com.sweetNet.model.Images;
import com.sweetNet.model.JsonResult;
import com.sweetNet.model.Member;
import com.sweetNet.repository.MemberRepository;
import com.sweetNet.service.ImagesService;
import com.sweetNet.service.MemberService;
import com.sweetNet.serviceImpl.MemberServiceImpl;
import com.sweetNet.until.AesHelper;
import com.sweetNet.until.ConfigInfo;
import com.sweetNet.until.JwtTokenUtils;
import com.sweetNet.until.PhoneUtil;
import com.sweetNet.until.SendMail;
import com.sweetNet.until.VerifyRecaptcha;

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
	private ImagesService imagesService;

	private static Boolean tokenCheck = false;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 建立會員帳號
	 */
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", required = false, dataType = "String", name = "memMail", value = "會員電子郵件（）", example = "sweetnet@gmail.com"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "String", name = "memPwd", value = "會員電子郵件（需大於等於8碼）", example = "12345678"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "String", name = "memNickname", value = "會員暱稱", example = "black"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "String", name = "memDep", value = "會員自述", example = "我有很多錢$$$"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "Integer", name = "memSex", value = "性別（0：女生、1：男生）", example = "1") })
	@ApiOperation("建立會員帳號")
	@PostMapping(value = "/user")
	public String createAccount(HttpServletRequest requset, @RequestBody @Valid SignUpDTO signUpDTO) {
		Member member = new Member();
		String msg = ConfigInfo.SYS_MESSAGE_SUCCESS;
		String states = ConfigInfo.DATA_OK;
		String action = request.getServletPath().replace("/", "");
		try {

			String memUuid = UUID.randomUUID().toString();
			String memMail = signUpDTO.getMemMail();
			String memPwd = AesHelper.encrypt(signUpDTO.getMemPwd());
			String memNickname = signUpDTO.getMemNickname();
			String memDep = signUpDTO.getMemDep();
			Integer memSex = signUpDTO.getMemSex();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String memRdate = sdf.format(new Date());
			MemberDTO memberDTOcheck = memberService.findOneByEmail(memMail);
			if (memberDTOcheck.getMemMail() != null) {
				msg = ConfigInfo.ALREADY_REGISTER;
				states = ConfigInfo.DATA_FAIL;
			}

			if (states.equals(ConfigInfo.DATA_OK)) {
				member.setMemUuid(memUuid);
				member.setMemMail(memMail);
				member.setMemPwd(memPwd);
				member.setMemNickname(memNickname);
				member.setMemDep(memDep);
				member.setMemSex(memSex);
				member.setMemRdate(memRdate);
				memberService.save(member);

				SendMail sendMail = new SendMail();
				sendMail.sendMail_SugarDaddy(action, memUuid, memMail);
			}
		} catch (Exception e) {
			logger.debug("message", e.getMessage());
			e.printStackTrace();
			msg = ConfigInfo.SYS_MESSAGE_ERROR;
			states = ConfigInfo.DATA_FAIL;
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("states", states);
		map.put("msg", msg);

		Gson gson = new Gson();

		return gson.toJson(map);
	}

	/**
	 * 開通會員狀態 - 寄驗證信到信箱
	 */
	@ApiOperation("開通會員狀態 - 寄驗證信到信箱從信箱點擊")
	@GetMapping(value = "/user/{memUuid}")
	public RedirectView updateAccount(@PathVariable String memUuid) {
		MemberDTO memberDTO = new MemberDTO();
		memberDTO = memberService.findOneByUuid(memUuid);
		if (memberDTO != null) {
			memberDTO.setMemSta(1);
			Member member = new MemberServiceImpl().getMemberFromMemberDTO(memberDTO);
			memberService.save(member);
		}
		String url = "https://sugarbabytw.com:8443/sugardaddyDevelop/dist/login/";
		return new RedirectView(url); // 重新導向到指定的url
	}

	/**
	 * 忘記密碼 - 寄驗證信到信箱從信箱點擊導至到更改密碼頁
	 */
	@ApiOperation("忘記密碼 - 寄驗證信到信箱從信箱點擊導至到更改密碼頁")
	@PutMapping(value = "/updatePWD")
	public RedirectView updatePwd(@RequestBody SignUpDTO signUpDTO) {
		MemberDTO memberDTO = new MemberDTO();
		logger.info("data", signUpDTO.getMemUuid());
		memberDTO = memberService.findOneByUuid(signUpDTO.getMemUuid());
		String pwd = AesHelper.encrypt(signUpDTO.getMemPwd());
		if (memberDTO.getMemMail().equals(signUpDTO.getMemMail())) {
			Member member = new MemberServiceImpl().getMemberFromMemberDTO(memberDTO);
			member.setMemPwd(pwd);
			memberService.save(member);
		}
		String url = ConfigInfo.REAL_PATH + "/sugardaddyDevelop/dist/login/";
		return new RedirectView(url); // 重新導向到指定的url
	}

	/**
	 * 連絡站長
	 */
	@ApiOperation("連絡站長")
	@PostMapping(value = "/mailtoadmin")
	public JsonResult<List<MailDTO>> mailToAdmin(@Valid @RequestBody MailDTO mailDTO) {
		List<MailDTO> list = new ArrayList<>();
		SendMail sendMail = new SendMail();
		sendMail.sendMailToAdmin(mailDTO.getName(), mailDTO.getMail(), mailDTO.getContent());
		list.add(mailDTO);
		return new JsonResult<>(list);
	}

	/**
	 * 驗證reCAPTCHA
	 * 
	 * @return
	 */
	@ApiOperation("驗證reCAPTCHA")
	@PostMapping(value = "/verifyToken")
	public <T> JsonResult JsonResultverify(HttpServletRequest request, @Valid @RequestBody String token) {
		String response = request.getParameter("g-recaptcha-response");
		logger.info("日誌輸出");

		List list = new ArrayList<>();
		try {
			boolean check = VerifyRecaptcha.isValid(token);

			list.add(check);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new JsonResult<>(list);
	}

	/**
	 * 填寫會員資料
	 */
	@ApiImplicitParams({ @ApiImplicitParam(paramType = "header", name = "Authorization", value = "JWT Token"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "String", name = "memName", value = "姓名", example = "Paul"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "String", name = "memCountry", value = "縣市", example = "基隆市"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "String", name = "memArea", value = "區域", example = "仁愛區"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "Integer", name = "memAge", value = "年齡", example = "33"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "String", name = "memAssets", value = "財力1~5（基礎、進階、高級、最高、可商議）", example = "1"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "Date", name = "memBirthday", value = "生日", example = "1989-10-10"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "Integer", name = "memAlcohol", value = "飲酒習慣", example = "1"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "String", name = "memDep", value = "會員自述", example = "我有很多錢$$$"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "Integer", name = "memEdu", value = "教育程度(0：其他、1：高中、2：大學、3：碩士、4：博士)", example = "3"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "Integer", name = "memHeight", value = "身高", example = "175"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "Integer", name = "memWeight", value = "體重", example = "70"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "Integer", name = "memIncome", value = "預留欄位", example = "1"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "Integer", name = "memIsvip", value = "會員狀態（不填）", defaultValue = "0"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "Integer", name = "memMarry", value = "婚姻狀況（0：未婚、1：已婚）", example = "1"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "String", name = "memNickname", value = "會員暱稱", example = "black"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "String", name = "memPhone", value = "電話號碼", example = "0912345678"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "Integer", name = "memSmoke", value = "抽菸習慣（0：不抽、1：偶爾、2：經常）", example = "2"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "String", name = "memAbout", value = "關於自己", example = "ex:我家有20間房子"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "Integer", name = "phoneStates", value = "手機驗證狀態（不填）", defaultValue = "0") })
	@ApiOperation("填寫會員資料")
	@PutMapping(value = "/user")
	public String createAccountInfo(@RequestHeader("Authorization") String au,
			@RequestBody MemberInfoDTO memberInfoDTO) {

		String token = au.substring(7);
		String memUuid = JwtTokenUtils.getJwtMemUuid(token); // 取得token

		String states = ConfigInfo.DATA_ERR_SYS;
		String msg = "";
		try {

			tokenCheck = JwtTokenUtils.validateToken(token);

			if (tokenCheck) {
				String memName = memberInfoDTO.getMemName();
				String memNickname = memberInfoDTO.getMemNickname();
				String memPhone = memberInfoDTO.getMemPhone();
				String memBirthday = memberInfoDTO.getMemBirthday();
				String memCountry = memberInfoDTO.getMemCountry();
				String memArea = memberInfoDTO.getMemArea();
				String memAbout = memberInfoDTO.getMemAbout();
				String memDep = memberInfoDTO.getMemDep();
				Integer memHeight = Integer.valueOf(memberInfoDTO.getMemHeight());
				Integer memWeight = Integer.valueOf(memberInfoDTO.getMemWeight());
//				Integer memEdu = Integer.valueOf(memberInfoDTO.getMemEdu());
//				Integer memMarry = Integer.valueOf(memberInfoDTO.getMemEdu());
//				Integer memAlcohol = Integer.valueOf(memberInfoDTO.getMemAlcohol());
//				Integer memSmoke = Integer.valueOf(memberInfoDTO.getMemSmoke());
				Integer memIncome = Integer.valueOf(memberInfoDTO.getMemIncome());
				Integer memBody = Integer.valueOf(memberInfoDTO.getMemBody());

				Integer memPattern = Integer.valueOf(memberInfoDTO.getMemPattern());
				Integer memAssets = Integer.valueOf(memberInfoDTO.getMemAssets());
				Integer memIsvip = 0;

				if (memberInfoDTO.getMemIsvip() != null) {
					memIsvip = Integer.valueOf(memberInfoDTO.getMemIsvip());
				}

				// 今日日期
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String memRdate = sdf.format(new Date());

//				Date date = new Date();
//				date = sdf.format(date);

				Integer memSta = 1;

				Member member = new Member();
				member.setMemUuid(memUuid);
				MemberDTO memberDTO = memberService.findOneByUuid(memUuid);

				member.setMemPwd(memberDTO.getMemPwd());
				member.setMemMail(memberDTO.getMemMail());
				member.setMemSex(memberDTO.getMemSex());
				member.setMemLgd(memberDTO.getMemLgd());
				member.setMemPhone(memberDTO.getMemPhone());
				member.setPhoneStates(memberDTO.getPhoneStates());

				member.setMemSeq(memberDTO.getMemSeq());
				member.setMemUuid(memUuid);
				member.setMemName(memName);
				member.setMemNickname(memNickname);
				member.setMemPhone(memPhone);
				member.setMemBirthday(memBirthday);
				member.setMemCountry(memCountry);
				member.setMemArea(memArea);
				member.setMemHeight(memHeight);
				member.setMemWeight(memWeight);
//				member.setMemEdu(memEdu);
//				member.setMemMarry(memMarry);
//				member.setMemAlcohol(memAlcohol);
//				member.setMemSmoke(memSmoke);
				member.setMemIncome(memIncome);
				member.setMemBody(memBody);
				member.setMemPattern(memPattern);
				member.setMemAssets(memAssets);
				member.setMemIsvip(memIsvip);
				member.setMemRdate(memRdate);
				member.setMemSta(memSta);
				member.setMemAbout(memAbout);
				member.setMemDep(memDep);

				memberService.save(member);
				states = ConfigInfo.DATA_OK;
				msg = ConfigInfo.SYS_MESSAGE_SUCCESS;
			} else {

			}
		} catch (TokenExpiredException | AuthException | SignatureException e) {
			logger.debug("message", e.getMessage());
			states = ConfigInfo.DATA_FAIL;
			msg = e.getMessage();
		}

		Map<String, String> map = new HashMap<String, String>();
		map.put("states", states);
		map.put("msg", msg);

		Gson gson = new Gson();

		return gson.toJson(map);
	}

	@ApiOperation("管理員更改會員狀態")
	@PutMapping(value = "/user/{memUuid}/{memSta}/{memIsvip}")
	public String updateAccountInfo(@RequestHeader("Authorization") String au, @PathVariable String memUuid,
			@PathVariable Integer memSta, @PathVariable Integer memIsvip) {

		String token = au.substring(7);

		String states = ConfigInfo.DATA_ERR_SYS;
		String msg = "";
		try {
			tokenCheck = JwtTokenUtils.validateToken(token);
			if (tokenCheck) {
				MemberDTO memberDTO = memberService.findOneByUuid(memUuid);
				if (memberDTO != null) {
					Member member = new MemberServiceImpl().getMemberFromMemberDTO(memberDTO);
					member.setMemSta(memSta);
					member.setMemIsvip(memIsvip);
					memberService.save(member);
					states = ConfigInfo.DATA_OK;
					msg = ConfigInfo.SYS_MESSAGE_SUCCESS;
				}
			}

		} catch (TokenExpiredException | AuthException | SignatureException e) {
			logger.debug("message", e.getMessage());
			states = ConfigInfo.DATA_FAIL;
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
	 */
	@ApiOperation("顯示個人資料")
	@GetMapping(value = "/user")
	public List<Map<Object, Object>> getUserInfo(@RequestHeader("Authorization") String au) {
		String token = au.substring(7);
		String memUuid = JwtTokenUtils.getJwtMemUuid(token); // 取得token

		MemberDTO memberDTO = new MemberDTO();

		List<Map<Object, Object>> list = new ArrayList<Map<Object, Object>>();

		try {

			tokenCheck = JwtTokenUtils.validateToken(token);

			if (tokenCheck) {
				Map<Object, Object> map = new HashMap<Object, Object>();
				memberDTO = memberService.findOneByUuid(memUuid);

				Images images = imagesService.findByMemUuid(memUuid);
				map.put("member", memberDTO);
				map.put("images", images);
				list.add(map);
				System.out.println(list);
			}
		} catch (TokenExpiredException | AuthException | SignatureException e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * 顯示會員資料 - 以性別分類男生只能看到女生，女生只能看到男生
	 */
	@ApiOperation("顯示會員資料 - 以登入會員的性別分類，男生只能看到女生、女生只能看到男生(action:hot=依照活躍度、new=新會員、normal)")
	@GetMapping(value = "/users/{action}")
	public List<Map<Object, Object>> getUserInfoBySex(@RequestHeader("Authorization") String au,
			SearchConditionDTO searchConditionDTO, @PathVariable String action) {
		List<Map<Object, Object>> result = new ArrayList<Map<Object, Object>>();
		String token = au.substring(7);
		List<MemberDTO> memberDTOs = new ArrayList<MemberDTO>();
		try {

			String memUuid = JwtTokenUtils.getJwtMemUuid(token);
			tokenCheck = JwtTokenUtils.validateToken(token);

			if (tokenCheck) {

				MemberDTO memberDTO = memberService.findOneByUuid(memUuid);

				searchConditionDTO.setMemSex(memberDTO.getMemSex());
				memberDTOs = memberService.findByCondition(action, searchConditionDTO);

				logger.info("取得會員資料共" + memberDTOs.size() + "筆會員資料");
				Integer count = 0;
				for (MemberDTO member : memberDTOs) {
					String uuid = member.getMemUuid();
					Images images = imagesService.findByMemUuid(uuid);
					Map<Object, Object> map = new HashMap<Object, Object>();
					logger.info(uuid + "/////照片///" + (images != null && uuid.equals(images.getMemUuid())));
					if (images != null && uuid.equals(images.getMemUuid())) {
						map.put("images", images);
						count++;
					} else {
						map.put("images", null);
					}
					map.put("member", member);
					result.add(map);
				}
				logger.info("有照片的會員" + count + "筆會員資料");
			}
		} catch (TokenExpiredException | AuthException | SignatureException e) {
			e.printStackTrace();
		}

		return result;
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
		String msg = ConfigInfo.SYS_MESSAGE_SUCCESS;
		String states = ConfigInfo.DATA_OK;

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
			states = ConfigInfo.DATA_FAIL;
			msg = e.getMessage();
		}

		map.put("states", states);
		map.put("msg", msg);
		return gson.toJson(map);
	}

	public String OTPMsg(String otp) {
		String otpMessage = "您的OTP驗證碼： " + otp + " 請於60秒內輸入驗證";
		return otpMessage;
	}

	/* 驗證VIP */
	public boolean checkIsVIP(MemberDTO memberDTO) {
		boolean check = memberDTO.getMemIsvip() == 1;
		return check;
	}

	/* 驗證會員狀態 */
	public boolean checkIsMemStates(MemberDTO memberDTO) {
		boolean check = memberDTO.getMemSta() == 1;
		return check;
	}

}