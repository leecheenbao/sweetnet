package com.sweetNet.until;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Until {

	/** 取得前端Json Param **/
	public static JSONObject getJSONParam(HttpServletRequest request) {
		JSONObject jsonParam = null;
		try {
			// 獲取輸入流
			BufferedReader streamReader = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));

			// 寫入資料到Stringbuilder
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = streamReader.readLine()) != null) {
				sb.append(line);
			}
			jsonParam = JSONObject.parseObject(sb.toString());
			// 直接將json資訊打印出來
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonParam;
	}

	/** 合併多個Json資料 **/
	public static String organizeJson(String... jsonList) throws Exception {
		JsonObject jsonObj = null;
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		for (String json : jsonList) {
			if (jsonObj != null) {
				jsonObj = jsonMerge(jsonObj, gson.fromJson(json, JsonObject.class));
			} else {
				jsonObj = gson.fromJson(json, JsonObject.class);
			}
		}
		JsonObject jsonStringsRoot = new JsonObject();
		jsonStringsRoot.add("data", jsonObj);
		return gson.toJson(jsonStringsRoot);
	}

	private static JsonObject jsonMerge(JsonObject jsonA, JsonObject jsonB) throws Exception {
		for (Map.Entry<String, JsonElement> sourceEntry : jsonA.entrySet()) {
			String key = sourceEntry.getKey();
			JsonElement value = sourceEntry.getValue();
			if (!jsonB.has(key)) {
				if (!value.isJsonNull()) {
					jsonB.add(key, value);
				}
			} else {
				if (!value.isJsonNull()) {
					if (value.isJsonObject()) {
						jsonMerge(value.getAsJsonObject(), jsonB.get(key).getAsJsonObject());
					} else {
						jsonB.add(key, value);
					}
				} else {
					jsonB.remove(key);
				}
			}
		}
		return jsonB;
	}

	public static void checkRex(String str) {
		String regex = "^\\w{1,63}@[a-zA-Z0-9]{2,63}\\.[a-zA-Z]{2,63}(\\.[a-zA-Z]{2,63})?$";
	}

	public static Map<String, String> uploadFile(byte[] file, String filePath, String memUuid) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		File targetFile = new File(filePath + "/" + memUuid);
		if (!targetFile.exists()) {
			targetFile.mkdirs();
		}
		String fileName = file.toString();
		/* 取得副檔名 */
		String fe = "";
		if (fileName.contains("."))
			fe = fileName.substring(fileName.lastIndexOf(".") + 1);

		Date date = new Date();
		String dateStr = String.valueOf(date.getTime());

		/* 重組檔名 */
		fileName = dateStr + ".jpeg" + fe;

		String url = filePath + "/" + memUuid + "/" + fileName;
		FileOutputStream out = new FileOutputStream(url);
		out.write(file);
		out.flush();
		out.close();

		map.put("imageName", fileName);
		map.put("imageUrl", url);

		return map;
	}

	public static String encodeBase64(byte[] encodeMe) {
		byte[] encodedBytes = Base64.getEncoder().encode(encodeMe);
		return new String(encodedBytes);
	}

	public static byte[] decodeBase64(String encodedData) {
		byte[] decodedBytes = Base64.getDecoder().decode(encodedData.getBytes());
		return decodedBytes;
	}

	private static final String BASE_URL = "https://4mwm9m.api.infobip.com";
	private static final String API_KEY = "App b0b5721ba401bbb11dbf88a43e836d78-0dcc4e31-69c1-4efd-b029-b9d60bc7b357";
	private static final String MEDIA_TYPE = "application/json";

	private static final String SENDER = "InfoSMS";
	private static final String RECIPIENT = "886919268790";
	private static final String MESSAGE_TEXT = "This is a sample message";

	public static void main(String arg[]) {

		String bodyJson = String.format(
				"{\"messages\":[{\"from\":\"%s\",\"destinations\":[{\"to\":\"%s\"}],\"text\":\"%s\"}]}", SENDER,
				RECIPIENT, MESSAGE_TEXT);

	}

}
