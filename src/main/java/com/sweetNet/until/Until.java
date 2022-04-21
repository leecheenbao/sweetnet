package com.sweetNet.until;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Until {
	
	/** 取得前端Json Param**/
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

	/** 合併多個Json資料**/
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
}
