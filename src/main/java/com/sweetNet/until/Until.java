package com.sweetNet.until;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Until {
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

	public void getInterfaces() {
		try {
			Enumeration e = NetworkInterface.getNetworkInterfaces();

			while (e.hasMoreElements()) {
				NetworkInterface netface = (NetworkInterface) e.nextElement();
				System.out.println("Net interface: " + netface.getName());

				Enumeration e2 = netface.getInetAddresses();

				while (e2.hasMoreElements()) {
					InetAddress ip = (InetAddress) e2.nextElement();
					System.out.println("IP address: " + ip.toString());
				}
			}
		} catch (Exception e) {
			System.out.println("e: " + e);
		}
	}

	public static void main(String[] args) {
		Until u = new Until();
		u.getInterfaces();
	}

}
