package com.sweetNet.until;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sun.misc.BASE64Decoder;

public class ImageUntil {
	private static String getBase64Format(final String base64ImgData) {
		// 從前端取得完整Base64含前綴
		String base64string = base64ImgData.trim();

		// 取得前綴定位
		int index = base64string.indexOf("base64,") + 7;
		String newImage = base64string.substring(index, base64string.length());// 去除前缀

		return newImage;
	}

	/**
	 * 判斷圖片base64字符串的文件格式
	 * 
	 * @param base64ImgData
	 * @return
	 */
	private static String extractMimeType(final String base64ImgData) {
		final Pattern mime = Pattern.compile("^data:([a-zA-Z0-9]+/[a-zA-Z0-9]+).*,.*");
		final Matcher matcher = mime.matcher(base64ImgData);
		if (!matcher.find())
			return "";
		return matcher.group(1).toLowerCase().split("/")[1];
	}

	/**
	 * 對字節數組字符串進行Base64解碼並生成圖片
	 * 
	 * @param imgStr      Base64字符串
	 * @param imgFilePath 生成圖片保存路徑
	 * @return boolean
	 */
	public static List<Object> GenerateImage(String base64ImgData, String imgFilePath, String localIP, String memUuid) {
		String fileName = "";
		String URL = "";
		String connectURL = "";
		Boolean check = false;
		try {
			if (base64ImgData != null) {

				/* 解析前端送來的base64 */
				String imageData = ImageUntil.getBase64Format(base64ImgData);

				/* 判斷副檔名 */
				String subName = ImageUntil.extractMimeType(base64ImgData);

				/* 重組檔案名稱 */
				fileName = String.valueOf(new Date().getTime()) + "." + subName;
				/* 以會員UUID開新目錄已經存在就不開 */
				File targetFile = new File(imgFilePath + "/" + memUuid);
				targetFile.setWritable(true, false);
				if (!targetFile.exists()) {
					targetFile.mkdirs();
				}

				BASE64Decoder decoder = new BASE64Decoder();

				// Base64解碼
				byte[] bytes = decoder.decodeBuffer(imageData);
				for (int i = 0; i < bytes.length; ++i) {
					if (bytes[i] < 0) {// 調整異常數據
						bytes[i] += 256;
					}
				}
				// 檔案實體位置
				URL = String.valueOf(targetFile + "/" + fileName);
				
				// 對外連線位置
				connectURL = String.valueOf(localIP + "/" + URL.substring(10, URL.length())).replace("\\", "/");
				// 生成檔案
				OutputStream out = new FileOutputStream(URL);
				out.write(bytes);
				out.flush();
				out.close();
				check = true;
			} else {
				return null;
			}
		} catch (Exception e) {
		}
		List<Object> list = new ArrayList<Object>();
		list.add(connectURL);
//		list.add(URL);
		list.add(check);

		return list;
	}
}
