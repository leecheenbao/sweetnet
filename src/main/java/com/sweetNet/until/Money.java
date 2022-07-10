package com.sweetNet.until;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

public class Money {

	public static String result(String info) {
		// 介面地址
		String requestUrl = "https://ccore.spgateway.com/MPG/period";
		// params用於儲存要請求的引數
		Map PostData = new HashMap();
		PostData.put("RespondType", "JSON");
		PostData.put("TimeStamp", new Date().getTime());
		PostData.put("Version", "1.1");
		PostData.put("LangType", "zh-TW");
		PostData.put("MerOrderNo", "201406010001");
		PostData.put("ProdDesc", "SugarDaddyVIP");
		PostData.put("PeriodAmt", 1680);
		PostData.put("PeriodType", "M");
		PostData.put("PeriodPoint", "01");
		PostData.put("PeriodStartType", 2);
		PostData.put("PeriodTimes", "1");
		PostData.put("PayerEmail", "leecheenbao@gmail.com");
		
		String encPostData = AesHelper.encrypt(PostData.toString());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("MerchantID_", "201406010001");
		params.put("PostData_", encPostData);
		
		AesHelper.encrypt(params.toString());
		String string = httpRequest(requestUrl, params);
		// 處理返回的JSON資料並返回
		System.out.println(params.toString());
//		JSONObject jsonObject = new JSONObject(string);
//		System.out.println(jsonObject.get("showapi_res_id"));
		Gson gson = new Gson();
		return gson.toJson(string);
	}

	private static String httpRequest(String requestUrl, Map params) {
		// buffer用於接受返回的字元
		StringBuffer buffer = new StringBuffer();
		try {
			// 建立URL,把請求地址給補全,其中urlencode()方法用於把params裡的引數給取出來
			URL url = new URL(requestUrl + "?" + urlencode(params));
			// 開啟http連線
			HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
			httpUrlConn.setDoInput(true);
			httpUrlConn.setRequestMethod("POST");
			httpUrlConn.connect();

			// 獲得輸入
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			// 將bufferReader的值給放到buffer裡
			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			// 關閉bufferReader和輸入流
			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();
			inputStream = null;
			// 斷開連線
			httpUrlConn.disconnect();

		} catch (Exception e) {
			e.printStackTrace();
		}
		// 返回字串
		return buffer.toString();
	}

	public static String urlencode(Map<String, Object> data) {
		// 將map裡的引數變成像 showapi_appid=###&showapi_sign=###&的樣子
		StringBuilder sb = new StringBuilder();
		for (Map.Entry i : data.entrySet()) {
			try {
				sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue() + "", "UTF-8")).append("&");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	// 測試是否有效
	public static void main(String[] args) {

		System.out.println(result(""));
	}
}
