package com.sweetNet.until;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;

public class ImageUntil {
	private static MimetypesFileTypeMap mtftp;

	public void ImageCheck() {
		mtftp = new MimetypesFileTypeMap();
		mtftp.addMimeTypes("image png tif jpg jpeg bmp");
	}

	public static boolean isImage(File file) {
		String mimetype = mtftp.getContentType(file);
		String type = mimetype.split("/")[0];
		return type.equals("image");
	}

	public static Map<String, String> uploadFile(byte[] file, String filePath, String memUuid, String suffix)
			throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		File targetFile = new File(filePath + "/" + memUuid);
		targetFile.setWritable(true, false);

		if (!targetFile.exists()) {
			targetFile.mkdirs();
		}

		Date date = new Date();
		String dateStr = String.valueOf(date.getTime());

		/* 重組檔名 */
		String fileName = dateStr + suffix;

		String originalUrl = filePath + "/" + memUuid + "/" + fileName;
		String publicUrl = "/" + memUuid + "/" + fileName;
		System.out.println(originalUrl);
		System.out.println(publicUrl);
		FileOutputStream out = new FileOutputStream(originalUrl);
		out.write(file);
		out.flush();
		out.close();

		map.put("imageName", fileName);
		map.put("imageUrl", publicUrl);

		return map;
	}
}
