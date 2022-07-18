package com.sweetNet.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.sweetNet.model.Images;
import com.sweetNet.model.JsonResult;
import com.sweetNet.model.Message;
import com.sweetNet.service.ImagesService;
import com.sweetNet.service.MemberService;
import com.sweetNet.service.MessageService;
import com.sweetNet.until.ConfigInfo;
import com.sweetNet.until.JwtTokenUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "MessageController")
@RestController
@RequestMapping("/api")
public class MessageController {
	@Autowired
	private MessageService messageService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private ImagesService imagesService;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private static Boolean tokenCheck = false;

	@ApiOperation("發送站內信")
	@PostMapping("/sendMessage")
	public JsonResult<Message> putMessage(@RequestHeader("Authorization") String au, @RequestBody Message message) {
		JsonResult<Message> jsonResult = new JsonResult<Message>();
		String token = au.substring(7);
		String sendId = JwtTokenUtils.getJwtMemUuid(token);
		try {
			tokenCheck = JwtTokenUtils.validateToken(token);
			if (tokenCheck) {
				message.setSendId(sendId);
				messageService.save(message);
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
			jsonResult.setData(message);
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(ConfigInfo.DATA_FAIL);
			return jsonResult;
		}
		jsonResult.setData(message);
		return jsonResult;
	}

	@ApiOperation("取得站內信列表(0：預設、1：屏蔽 )")
	@GetMapping("/messages/{uuid}/{states}")
	public JsonResult<List<Message>> getMessages(@RequestHeader("Authorization") String au, @PathVariable String uuid,
			@PathVariable Integer states) {
		JsonResult<List<Message>> jsonResult = new JsonResult<List<Message>>();

		String token = au.substring(7);
		String mineUuid = JwtTokenUtils.getJwtMemUuid(token);
		try {
			tokenCheck = JwtTokenUtils.validateToken(token);
			List<Message> messagesRec = new ArrayList<Message>();
			List<Message> messagesSend = new ArrayList<Message>();
			List<Message> result = new ArrayList<Message>();
			if (tokenCheck) {
				messagesRec = messageService.findBySendIdAndRecId(uuid, mineUuid, states);
				messagesSend = messageService.findBySendIdAndRecId(mineUuid, uuid, states);
				result.addAll(messagesRec);
				result.addAll(messagesSend);

				jsonResult.setData(result);
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(ConfigInfo.DATA_FAIL);
			return jsonResult;
		}

		return jsonResult;
	}

	@ApiOperation("取得訊息會員清單")
	@GetMapping("/memberList")
	public JsonResult<List<Map<Object, Object>>> getMemberList(@RequestHeader("Authorization") String au) {
		JsonResult<List<Map<Object, Object>>> jsonResult = new JsonResult<List<Map<Object, Object>>>();
		List<Map<Object, Object>> result = new ArrayList<Map<Object, Object>>();
		String token = au.substring(7);
		String memUuid = JwtTokenUtils.getJwtMemUuid(token);

		try {
			tokenCheck = JwtTokenUtils.validateToken(token);
			Set<String> members = new HashSet<String>();
			if (tokenCheck) {
				members = messageService.findByRecId(memUuid);
				for (String uuid : members) {
					Map<Object, Object> map = new HashMap<Object, Object>();
					MemberDTO memberDTO = memberService.findOneByUuid(uuid);
					Images images = imagesService.findByMemUuid(uuid);
					if (images != null && uuid.equals(images.getMemUuid())) {
						map.put("images", images);
					} else {
						map.put("images", null);
					}

					map.put("member", memberDTO);
					result.add(map);
				}

				jsonResult.setData(result);
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(ConfigInfo.DATA_FAIL);
			return jsonResult;
		}

		return jsonResult;
	}

}
