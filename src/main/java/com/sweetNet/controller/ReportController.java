package com.sweetNet.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sweetNet.dto.MemberDTO;
import com.sweetNet.model.JsonResult;
import com.sweetNet.model.Report;
import com.sweetNet.service.MemberService;
import com.sweetNet.service.ReportService;
import com.sweetNet.until.JwtTokenUtils;
import com.sweetNet.until.SendMail;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "ReportController")
@RestController
@RequestMapping("/api")
public class ReportController {

	@Autowired
	private ReportService reportService;
	@Autowired
	private MemberService memberService;
	private static Boolean tokenCheck = false;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@ApiOperation("取得檢舉清單")
	@GetMapping(value = "/reports")
	protected JsonResult<List<Report>> getAllReport() {
		List<Report> list = new ArrayList<>();
		list = reportService.findAll();
		return new JsonResult<>(list);
	}

	@ApiOperation("取得單一筆檢舉")
	@GetMapping(value = "/report/{id}")
	protected JsonResult<Report> getOneReportById(@PathVariable Integer id) {
		Report report = new Report();
		report = reportService.findOne(id);
		return new JsonResult<>(report);
	}

	@ApiOperation("使用者檢舉")
	@PostMapping(value = "/report")
	protected JsonResult<Report> insertReport(@RequestHeader("Authorization") String au, @RequestBody Report report) {
		String token = au.substring(7);
		String memUuid = JwtTokenUtils.getJwtMemUuid(token); // 取得token

		try {
			tokenCheck = JwtTokenUtils.validateToken(token);

			if (tokenCheck) {

				MemberDTO sendMemberDTO = memberService.findOneByUuid(report.getSendId());
				MemberDTO recMemberDTO = memberService.findOneByUuid(report.getRecId());
				reportService.save(report);

				SendMail sendMail = new SendMail();
				/* 通知檢舉人 */
				sendMail.sendMail_SugarDaddy("report_send", sendMemberDTO.getMemUuid(), sendMemberDTO.getMemMail());
				/* 通知被檢舉人 */
				sendMail.sendMail_SugarDaddy("report_rec", recMemberDTO.getMemUuid(), recMemberDTO.getMemMail());

				/* 通知站長 */
				StringBuffer sb = new StringBuffer();
				sb.append(report.getContent());
				sendMail.sendMailToAdmin(sendMemberDTO.getMemNickname(), sendMemberDTO.getMemUuid(),
						report.getContent());
			}
		} catch (Exception e) {
			e.getStackTrace();
			logger.error(e.getMessage());
		}
		return new JsonResult<>(report);
	}

//	@ApiOperation("編輯公告訊息")
//	@PutMapping(value = "/Report")
//	protected Report updateReport(@RequestBody Report report) {
//		report.setReportDate(new Date());
//		reportService.save(report);
//		return report;
//	}
//
//	@ApiOperation("刪除公告")
//	@DeleteMapping(value = "/Report/{id}")
//	protected List<Report> deleteReportById(@PathVariable Integer id) {
//		reportService.delete(id);
//		return reportService.findAll();
//	}

}
