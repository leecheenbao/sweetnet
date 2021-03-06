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
	 * ??????????????????
	 */
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", required = false, dataType = "String", name = "memMail", value = "????????????????????????", example = "sweetnet@gmail.com"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "String", name = "memPwd", value = "????????????????????????????????????8??????", example = "12345678"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "String", name = "memNickname", value = "????????????", example = "black"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "String", name = "memDep", value = "????????????", example = "???????????????$$$"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "Integer", name = "memSex", value = "?????????0????????????1????????????", example = "1") })
	@ApiOperation("??????????????????")
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
	 * ?????????????????? - ?????????????????????
	 */
	@ApiOperation("?????????????????? - ????????????????????????????????????")
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
		return new RedirectView(url); // ????????????????????????url
	}

	/**
	 * ???????????? - ????????????????????????????????????????????????????????????
	 */
	@ApiOperation("???????????? - ????????????????????????????????????????????????????????????")
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
		return new RedirectView(url); // ????????????????????????url
	}

	/**
	 * ????????????
	 */
	@ApiOperation("????????????")
	@PostMapping(value = "/mailtoadmin")
	public JsonResult<List<MailDTO>> mailToAdmin(@Valid @RequestBody MailDTO mailDTO) {
		List<MailDTO> list = new ArrayList<>();
		SendMail sendMail = new SendMail();
		sendMail.sendMailToAdmin(mailDTO.getName(), mailDTO.getMail(), mailDTO.getContent());
		list.add(mailDTO);
		return new JsonResult<>(list);
	}

	/**
	 * ??????reCAPTCHA
	 * 
	 * @return
	 */
	@ApiOperation("??????reCAPTCHA")
	@PostMapping(value = "/verifyToken")
	public <T> JsonResult JsonResultverify(HttpServletRequest request, @Valid @RequestBody String token) {
		String response = request.getParameter("g-recaptcha-response");
		logger.info("????????????");

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
	 * ??????????????????
	 */
	@ApiImplicitParams({ @ApiImplicitParam(paramType = "header", name = "Authorization", value = "JWT Token"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "String", name = "memName", value = "??????", example = "Paul"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "String", name = "memCountry", value = "??????", example = "?????????"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "String", name = "memArea", value = "??????", example = "?????????"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "Integer", name = "memAge", value = "??????", example = "33"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "String", name = "memAssets", value = "??????1~5???????????????????????????????????????????????????", example = "1"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "Date", name = "memBirthday", value = "??????", example = "1989-10-10"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "Integer", name = "memAlcohol", value = "????????????", example = "1"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "String", name = "memDep", value = "????????????", example = "???????????????$$$"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "Integer", name = "memEdu", value = "????????????(0????????????1????????????2????????????3????????????4?????????)", example = "3"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "Integer", name = "memHeight", value = "??????", example = "175"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "Integer", name = "memWeight", value = "??????", example = "70"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "Integer", name = "memIncome", value = "????????????", example = "1"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "Integer", name = "memIsvip", value = "????????????????????????", defaultValue = "0"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "Integer", name = "memMarry", value = "???????????????0????????????1????????????", example = "1"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "String", name = "memNickname", value = "????????????", example = "black"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "String", name = "memPhone", value = "????????????", example = "0912345678"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "Integer", name = "memSmoke", value = "???????????????0????????????1????????????2????????????", example = "2"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "String", name = "memAbout", value = "????????????", example = "ex:?????????20?????????"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "Integer", name = "phoneStates", value = "??????????????????????????????", defaultValue = "0") })
	@ApiOperation("??????????????????")
	@PutMapping(value = "/user")
	public String createAccountInfo(@RequestHeader("Authorization") String au,
			@RequestBody MemberInfoDTO memberInfoDTO) {

		String token = au.substring(7);
		String memUuid = JwtTokenUtils.getJwtMemUuid(token); // ??????token

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

				// ????????????
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

	@ApiOperation("???????????????????????????")
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
	 * ??????????????????
	 */
	@ApiOperation("??????????????????")
	@GetMapping(value = "/user")
	public List<Map<Object, Object>> getUserInfo(@RequestHeader("Authorization") String au) {
		String token = au.substring(7);
		String memUuid = JwtTokenUtils.getJwtMemUuid(token); // ??????token

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
	 * ?????????????????? - ??????????????????????????????????????????????????????????????????
	 */
	@ApiOperation("?????????????????? - ????????????????????????????????????????????????????????????????????????????????????(action:hot=??????????????????new=????????????normal)")
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

				logger.info("?????????????????????" + memberDTOs.size() + "???????????????");
				Integer count = 0;
				for (MemberDTO member : memberDTOs) {
					String uuid = member.getMemUuid();
					Images images = imagesService.findByMemUuid(uuid);
					Map<Object, Object> map = new HashMap<Object, Object>();
					logger.info(uuid + "/////??????///" + (images != null && uuid.equals(images.getMemUuid())));
					if (images != null && uuid.equals(images.getMemUuid())) {
						map.put("images", images);
						count++;
					} else {
						map.put("images", null);
					}
					map.put("member", member);
					result.add(map);
				}
				logger.info("??????????????????" + count + "???????????????");
			}
		} catch (TokenExpiredException | AuthException | SignatureException e) {
			e.printStackTrace();
		}

		return result;
	}


	@ApiImplicitParams({ @ApiImplicitParam(paramType = "header", name = "Authorization", value = "JWT Token"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "String", name = "memPhone", value = "????????????????????????"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "String", name = "otp", example = "354045"),
			@ApiImplicitParam(paramType = "query", required = false, dataType = "String", name = "secret", example = "6NMG67YDLUWNXQ3W") })
	@ApiOperation("??????OTP??????")
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
		String otpMessage = "??????OTP???????????? " + otp + " ??????60??????????????????";
		return otpMessage;
	}

	/* ??????VIP */
	public boolean checkIsVIP(MemberDTO memberDTO) {
		boolean check = memberDTO.getMemIsvip() == 1;
		return check;
	}

	/* ?????????????????? */
	public boolean checkIsMemStates(MemberDTO memberDTO) {
		boolean check = memberDTO.getMemSta() == 1;
		return check;
	}

}