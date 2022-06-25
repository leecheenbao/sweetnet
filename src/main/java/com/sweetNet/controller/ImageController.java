package com.sweetNet.controller;

import java.net.InetAddress;
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
import com.sweetNet.until.ImageUntil;
import com.sweetNet.until.JwtTokenUtils;
import com.sweetNet.until.SystemInfo;

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
		String states = SystemInfo.DATA_OK;
		String memUuid = JwtTokenUtils.getJwtMemUuid(token);
		try {
			tokenCheck = JwtTokenUtils.validateToken(token);
			if (tokenCheck) {

				for (int i = 0; i < file.length; i++) {

					/* 取得副檔名 */
					String suffix = file[i].getOriginalFilename()
							.substring(file[i].getOriginalFilename().lastIndexOf("."));

					if (!"".equals(suffix) && suffix.equals(".JPG") || suffix.equals(".PNG") || suffix.equals(".GIF")
							|| suffix.equals(".JPEG") | suffix.equals(".BMP")) {
						System.out.println("test");
					}
					MemberImage memberImage = new MemberImage();

					Map<String, String> filemap = ImageUntil.uploadFile(file[i].getBytes(), filePath, memUuid, suffix);

					String imageUrl = String.valueOf(filemap.get("imageUrl"));

					InetAddress address = InetAddress.getLocalHost();// 獲取本地IP位置 //PC-20140317PXKX/192.168.0.121
					String hostAddress = address.getHostAddress();// 192.168.0.121
					imageUrl = "http://" + hostAddress + ":8083/images" + imageUrl;

					memberImage.setMemUuid(memUuid);
					memberImage.setImageUrl(imageUrl);

					memberImageService.save(memberImage);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			states = SystemInfo.DATA_ERR_SYS;
			jsonParam.put("msg", e.getMessage());
			jsonParam.put("states", states);
			return jsonParam;
		}
		jsonParam.put("msg", SystemInfo.SYS_MESSAGE_SUCCESS);
		jsonParam.put("states", states);
		return jsonParam;
	}

//	/**
//	 * 設定會員封面照片
//	 * 
//	 * @param request
//	 * @return JSONObject
//	 */
//	@ApiOperation("設定會員封面照片-id為imagesData中要更改為封面照片的序號")
//	@PostMapping("/coverImage/{id}")
//	public JSONObject test(@RequestHeader("Authorization") String au, @PathVariable int id) {
//		// 獲取到JSONObject
//		JSONObject jsonParam = new JSONObject();
//		String token = au.substring(7);
//		String states = SystemInfo.DATA_OK;
//		String msg = "";
//		String memUuid = JwtTokenUtils.getJwtMemUuid(token);
//		try {
//			tokenCheck = JwtTokenUtils.validateToken(token);
//			if (tokenCheck) {
//				Iterable<MemberImage> memberImages = memberImageService.findByUuid(memUuid);
//
//				/* 檢核id是否為該會員的照片 */
//				ArrayList<Integer> checkList = new ArrayList<Integer>();
//				for (MemberImage m : memberImages) {
//					checkList.add(m.getId());
//				}
//
//				if (checkList.contains(id)) {
//					for (MemberImage m : memberImages) {
//						memberImageService.saveSeq(m, id);
//					}
//					msg = SystemInfo.SYS_MESSAGE_SUCCESS;
//				} else {
//					states = SystemInfo.DATA_ERR_SYS;
//					msg = "此圖片ID不屬於該會員";
//				}
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			states = SystemInfo.DATA_ERR_SYS;
//			jsonParam.put("msg", e.getMessage());
//			jsonParam.put("states", states);
//			return jsonParam;
//		}
//		jsonParam.put("msg", msg);
//		jsonParam.put("states", states);
//		return jsonParam;
//	}
}
