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
	 */
	public static List<Object> GenerateImage(String base64ImgData, String memUuid) {
		String imgFilePath = ConfigInfo.IMAGE_PATH;
		String localIP = ConfigInfo.REAL_PATH;
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
		list.add(check);

		return list;
	}

	/* 後臺圖片 */
	public static String GenerateDashboardImage(String code, String base64ImgData) {
		String fileName = "";
		String URL = "";
		String connectURL = "";
		try {
			if (base64ImgData != null) {

				/* 解析前端送來的base64 */
				String imageData = ImageUntil.getBase64Format(base64ImgData);

				/* 判斷副檔名 */
				String subName = ImageUntil.extractMimeType(base64ImgData);
				/* 重組檔案名稱 */
				fileName = String.valueOf(code + "." + subName);
				/* 以會員UUID開新目錄已經存在就不開 */
				File targetFile = new File(ConfigInfo.DASHBOARD_IMG_PATH);
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
				connectURL = String
						.valueOf(ConfigInfo.REAL_PATH + ConfigInfo.DASHBOARD_IMG_CONNECT_PATH + "/" + fileName)
						.replace("\\", "/");
				// 生成檔案
				OutputStream out = new FileOutputStream(URL);
				out.write(bytes);
				out.flush();
				out.close();
			} else {
				return null;
			}
		} catch (Exception e) {
		}

		return connectURL;
	}

	public static void deleteFile(String URL) {

		File file = new File(URL);
		if (file.exists()) {// 判斷路徑是否存在
			if (file.isFile()) {// boolean isFile():測試此抽象路徑名錶示的檔案是否是一個標準檔案。
				file.delete();
			}
			file.delete();
		} else {
			System.out.println("該file路徑不存在！！");
		}
	}

	public static void main(String[] args) {
		String URL = "http://sugarbabytw.com:8083/sweetNetImg/images/e8bd588c-8bc0-4b06-bf14-db8049c821de/1656783948926.png";
		String[] url = URL.split("/");

		String filePath = "/home/hsa/sweetNetImg/images";
		URL = filePath + "/" + url[url.length - 2] + "/" + url[url.length - 1];
		
		System.out.println(URL);
//		deleteFile(URL);
	}
}
