package com.sweetNet.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.sweetNet.dto.ImagesDTO;
import com.sweetNet.model.Images;
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
	@Value("${filePath}")
	private String filePath;

	@Value("${localIP}")
	private String localIP;

	private static Boolean tokenCheck = false;

	@Autowired
	private ImagesService imagesService;

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
				for (ImagesDTO imagesDTO : imagesDTOs) {
					if (imagesDTO.getImage() != null) {
						List<Object> list = ImageUntil.GenerateImage(imagesDTO.getImage(), filePath, localIP, memUuid);

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

}
