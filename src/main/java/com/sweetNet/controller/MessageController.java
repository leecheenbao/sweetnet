package com.sweetNet.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Blob;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.sweetNet.model.Member;
import com.sweetNet.model.Message;
import com.sweetNet.model.MessageContent;
import com.sweetNet.repository.MessageContentRepository;
import com.sweetNet.repository.MessageRepository;
import com.sweetNet.service.MemberService;
import com.sweetNet.service.MessageContentService;
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
	private MessageRepository messageRepository;
	@Autowired
	private MessageContentService messageContentService;
	@Autowired
	private MessageContentRepository messageContentRepository;
	@Autowired
	private MemberService memberService;

	private static Boolean tokenCheck = false;

	@ApiOperation("取得站內信列表")
	@GetMapping("/messages")
	public JSONObject getMessages(@RequestHeader("Authorization") String au) {
		// 獲取到JSONObject
		JSONObject jsonParam = new JSONObject();
		String token = au.substring(7);
		String states = ConfigInfo.DATA_OK;
		String recId = JwtTokenUtils.getJwtMemUuid(token);
		try {
			tokenCheck = JwtTokenUtils.validateToken(token);
			if (tokenCheck) {

				List<Message> mList = messageRepository.findByRecId(recId);
				List<Map<String, Object>> messageList = new ArrayList<Map<String, Object>>();
				for (Message m : mList) {
					int index = 0;
					Map<String, Object> map = new HashMap<String, Object>();
					List<MessageContent> mcList = messageContentRepository.findByMessageId(m.getMessageId());
					String messageId = m.getMessageId();
					String sendId = m.getSendId();
					Integer msgStates = m.getStates();
					Blob blob = mcList.get(index).getContent();
					String content = convertBlob(blob);
					String pdate = mcList.get(index++).getPdate();

					Member sendMember = memberService.findOneByUuid(sendId);
					Member recMember = memberService.findOneByUuid(recId);
					String sendNickName = sendMember.getMemNickname();
					String recNickName = recMember.getMemNickname();

					map.put("messageId", messageId);
					map.put("sendId", sendId);
					map.put("sendNickname", sendNickName);
					map.put("recId", recId);
					map.put("recNickname", recNickName);
					map.put("content", content);
					map.put("pdate", pdate);
					map.put("states", msgStates);
					messageList.add(map);
				}
				jsonParam.put("data", messageList);
			}

		} catch (Exception e) {
			e.printStackTrace();
			states = ConfigInfo.DATA_ERR_SYS;
			jsonParam.put("msg", e.getMessage());
			jsonParam.put("states", states);
			return jsonParam;
		}
		jsonParam.put("msg", ConfigInfo.SYS_MESSAGE_SUCCESS);
		jsonParam.put("states", states);

		return jsonParam;
	}

	@ApiOperation("取得站內信")
	@GetMapping("/message/{messageId}")
	public JSONObject getMessage(@RequestHeader("Authorization") String au, @PathVariable String messageId) {
		// 獲取到JSONObject
		JSONObject jsonParam = new JSONObject();
		String token = au.substring(7);
		String states = ConfigInfo.DATA_OK;
		String recId = JwtTokenUtils.getJwtMemUuid(token);
		try {
			tokenCheck = JwtTokenUtils.validateToken(token);
			if (tokenCheck) {

				MessageContent mc = messageContentService.findByMsgId(messageId);
				Message m = messageService.findByMsgId(messageId);
				if (m.getId() != null) {

					m.setStates(1);
					messageService.save(m);

					Blob blob = mc.getContent();
					String content = convertBlob(blob);

					Member sendMember = memberService.findOneByUuid(m.getSendId());
					Member recMember = memberService.findOneByUuid(recId);
					String sendNickName = sendMember.getMemNickname();
					String recNickName = recMember.getMemNickname();

					JSONObject json = new JSONObject();
					json.put("messageId", messageId);
					json.put("sendId", m.getSendId());
					json.put("sendNickName", sendNickName);
					json.put("recId", recId);
					json.put("recNickname", recNickName);
					json.put("content", content);
					json.put("pdate", mc.getPdate());
					json.put("states", m.getStates());
					jsonParam.put("data", json);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			states = ConfigInfo.DATA_ERR_SYS;
			jsonParam.put("msg", e.getMessage());
			jsonParam.put("states", states);
			return jsonParam;
		}
		jsonParam.put("msg", ConfigInfo.SYS_MESSAGE_SUCCESS);
		jsonParam.put("states", states);

		return jsonParam;
	}

	@ApiOperation("發送站內信")
	@PostMapping("/sendMessage")
	public JSONObject putMessage(@RequestHeader("Authorization") String au, @RequestBody HashMap<String, String> user) {
		// 獲取到JSONObject
		JSONObject jsonParam = new JSONObject();
		String token = au.substring(7);
		String states = ConfigInfo.DATA_OK;
		String sendId = JwtTokenUtils.getJwtMemUuid(token);
		try {
			tokenCheck = JwtTokenUtils.validateToken(token);
			if (tokenCheck) {
				String recId = user.get("recId");
				String contentStr = user.get("content");
				String messageId = String.valueOf(UUID.randomUUID());

				byte[] content = contentStr.getBytes();
				Blob blob = new SerialBlob(content);

				SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd hh:ss:mm");
				Calendar calendar = Calendar.getInstance();
				Date dateObj = calendar.getTime();
				String pdate = dtf.format(dateObj);

				MessageContent mc = new MessageContent();
				mc.setMessageId(messageId);
				mc.setContent(blob);
				mc.setPdate(pdate);
				messageContentRepository.save(mc);

				Message m = new Message();
				m.setMessageId(messageId);
				m.setSendId(sendId);
				m.setRecId(recId);
				m.setStates(0);
				messageRepository.save(m);

				System.out.println(messageId);
			}

		} catch (Exception e) {
			e.printStackTrace();
			states = ConfigInfo.DATA_ERR_SYS;
			jsonParam.put("msg", e.getMessage());
			jsonParam.put("states", states);
			return jsonParam;
		}
		jsonParam.put("msg", ConfigInfo.SYS_MESSAGE_SUCCESS);
		jsonParam.put("states", states);
		return jsonParam;
	}

	// 以下為專門處理 Blob 物件，轉換成 String，並回傳該 String
	public String convertBlob(Blob blob) throws IOException {

		StringBuffer temp = new StringBuffer();
		String line = "";
		BufferedReader reader = null;

		try {
			// 編碼 和 BufferSize 請自行使用
			reader = new BufferedReader(new InputStreamReader(blob.getBinaryStream(), "UTF8"), 1024);

			// 透過迴圈，取資料，用 \n 換行
			while ((line = reader.readLine()) != null) {
				temp.append(line).append("\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			line = null;
			if (reader != null)
				reader.close();

		}
		return temp.toString();
	}
}