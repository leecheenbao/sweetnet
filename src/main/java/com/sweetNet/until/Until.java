package com.sweetNet.until;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Until {

	public static void main(String[] args) {
		Until.getDateFromAge(10);
	}

	/* 計算註冊天數 */
	public static String getDateFromAge(Integer age) {
		String ageDate = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Long date = new Date().getTime();
			Long time = (long) (1000 * 60 * 60 * 24);
			Long day = date - (time * 365 * 10);

			Date d = new Date(day);
			ageDate = sdf.format(d);
		} catch (Exception e) {
			e.printStackTrace();
		}
//		Integer count = (betweenDate == 0) ? 1 : Math.toIntExact(betweenDate + 1);
		return ageDate;
	}

	/* 計算會員活躍度分數 */
	public static Integer getAccountValue(Integer memLgd, Integer count, Integer memIsVip) {
		double bonus = (memIsVip == 1) ? 1.5 : 1;
		double result = memLgd * bonus + count;
		return Integer.valueOf((int) Math.round(result));
	}

	/* 計算註冊天數 */
	public static Integer getBetweenDateCount(String memRdate) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		long betweenDate = 0;
		try {
			Date beginDate = sdf.parse(memRdate);
			Date endDate = sdf.parse(today);
			betweenDate = (endDate.getTime() - beginDate.getTime()) / (1000 * 60 * 60 * 24);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		Integer count = (betweenDate == 0) ? 1 : Math.toIntExact(betweenDate + 1);
		return count;
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

	/* FileToBase64 */
	public static String encodeBase64(byte[] encodeMe) {
		byte[] encodedBytes = Base64.getEncoder().encode(encodeMe);
		return new String(encodedBytes);
	}

	/* base64ToFile */
	public static byte[] decodeBase64(String encodedData) {
		byte[] decodedBytes = Base64.getDecoder().decode(encodedData.getBytes());
		return decodedBytes;
	}

}
