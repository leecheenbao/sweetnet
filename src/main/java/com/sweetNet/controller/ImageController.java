package com.sweetNet.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.sweetNet.model.MemberImage;
import com.sweetNet.repository.MemberImageRepository;
import com.sweetNet.service.MemberImageService;
import com.sweetNet.until.ConfigInfo;
import com.sweetNet.until.JwtTokenUtils;
import com.sweetNet.until.Until;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "ImageController")
@RestController
@RequestMapping("/api")
public class ImageController {
	@Value("${filePath}")
	private String filePath;

	private static Boolean tokenCheck = false;

	@Autowired
	private MemberImageService memberImageService;
	@Autowired
	private MemberImageRepository memberImageRepository;

	/**
	 * 會員照片上傳
	 * 
	 * @param request
	 * @return JSONObject
	 */
	@ApiOperation("會員照片上傳")
	@PostMapping("/uploadImage")
	public JSONObject uploading(@RequestHeader("Authorization") String au, @RequestParam("file") MultipartFile[] file) {
		// 獲取到JSONObject
		JSONObject jsonParam = new JSONObject();
		String token = au.substring(7);
		String states = ConfigInfo.DATA_OK;
		String memUuid = JwtTokenUtils.getJwtMemUuid(token);
		try {
			tokenCheck = JwtTokenUtils.validateToken(token);
			if (tokenCheck) {

				for (int i = 0; i < file.length; i++) {

					MemberImage memberImage = new MemberImage();
					Map<String, String> filemap = Until.uploadFile(file[i].getBytes(), filePath, memUuid);
					String imageUrl = String.valueOf(filemap.get("imageUrl"));

					memberImage.setMemUuid(memUuid);
					memberImage.setImageUrl(imageUrl);

					memberImageService.save(memberImage);
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
	
	
}
