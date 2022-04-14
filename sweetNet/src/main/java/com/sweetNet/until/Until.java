package com.sweetNet.until;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;

public class Until {
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

	public static void checkRex(String str) {
		String regex = "^\\w{1,63}@[a-zA-Z0-9]{2,63}\\.[a-zA-Z]{2,63}(\\.[a-zA-Z]{2,63})?$";
	}
}
