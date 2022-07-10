package com.sweetNet.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.sweetNet.dto.ImagesDTO;
import com.sweetNet.model.Images;
import com.sweetNet.model.JsonResult;
import com.sweetNet.service.ImagesService;
import com.sweetNet.until.ConfigInfo;
import com.sweetNet.until.ImageUntil;
import com.sweetNet.until.JwtTokenUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "ImageController")
@RestController
@RequestMapping("/api")
public class ImageController {
	private static Boolean tokenCheck = false;

	@Autowired
	private ImagesService imagesService;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 會員照片上傳
	 */
	@ApiOperation("會員照片上傳")
	@PostMapping("/uploadImage")
	public String uploading(@RequestHeader("Authorization") String au, @RequestBody List<ImagesDTO> imagesDTOs) {
		String token = au.substring(7);
		String memUuid = JwtTokenUtils.getJwtMemUuid(token);

		String msg = ConfigInfo.SYS_MESSAGE_SUCCESS;
		String states = ConfigInfo.DATA_OK;
		try {
			tokenCheck = JwtTokenUtils.validateToken(token);
			if (tokenCheck) {

				int count = 1;
				/* 判斷前端送來的圖片是會員第幾個欄位，有照片的畫再對對應資料庫欄位做更新 */
				for (ImagesDTO imagesDTO : imagesDTOs) {
					if (imagesDTO.getImage() != null) {
						List<Object> list = ImageUntil.GenerateImage(imagesDTO.getImage(), memUuid);

						String imageUrl = String.valueOf(list.get(0));
						Boolean op = (boolean) list.get(1);
						if (op) {

							Images images = new Images();
							Images checkimages = imagesService.findByMemUuid(memUuid);
							if (checkimages != null) {
								images = checkimages;
							}

							switch (count) {
							case 1:
								images.setImage_1(imageUrl);
								break;
							case 2:
								images.setImage_2(imageUrl);
								break;
							case 3:
								images.setImage_3(imageUrl);
								break;
							case 4:
								images.setImage_4(imageUrl);
								break;
							case 5:
								images.setImage_5(imageUrl);
								break;
							}

							images.setMemUuid(memUuid);
							imagesService.save(images);
						}
						count++;
					}
					logger.info("照片上傳 UUID=" + memUuid);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			msg = ConfigInfo.SYS_MESSAGE_ERROR;
			states = ConfigInfo.DATA_FAIL;
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("states", states);
		map.put("msg", msg);

		return new Gson().toJson(map);
	}

	/**
	 * 管理員刪除會員照片
	 */
	@ApiOperation("管理員刪除會員照片")
	@DeleteMapping("/deleteImage/{memUuid}/{index}")
	public JsonResult deleteImage(@RequestHeader("Authorization") String au, @PathVariable String memUuid,
			@PathVariable Integer index) {
		JsonResult jsonResult = new JsonResult<>();
		List resultlist = new ArrayList();
		String token = au.substring(7);

		try {
			tokenCheck = JwtTokenUtils.validateToken(token);
			if (tokenCheck) {

				Images images = imagesService.findByMemUuid(memUuid);
				switch (index) {
				case 1:
					images.setImage_1(null);
					break;
				case 2:
					images.setImage_2(null);
					break;
				case 3:
					images.setImage_3(null);
					break;
				case 4:
					images.setImage_4(null);
					break;
				case 5:
					images.setImage_5(null);
					break;
				}
				resultlist.add("imageIndex：" + index);
				resultlist.add("memUuid：" + memUuid);
				jsonResult.setData(resultlist);
				imagesService.save(images);
				logger.info("照片刪除 UUID=" + memUuid);
			}

		} catch (Exception e) {
			e.printStackTrace();
			jsonResult.setCode(ConfigInfo.DATA_FAIL);
			jsonResult.setMsg(e.getMessage());
			logger.error(e.getMessage());
		}

		return jsonResult;
	}
}
