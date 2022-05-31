package com.sweetNet.until;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

public class GetLocalJSON {

	public static String getCountry(File file) {
		String jsonStr = "";
		try {
			FileReader fileReader = new FileReader(file);
			Reader reader = new InputStreamReader(new FileInputStream(file), "Utf-8");
			int ch = 0;
			StringBuffer sb = new StringBuffer();
			while ((ch = reader.read()) != -1) {
				sb.append((char) ch);
			}
			fileReader.close();
			reader.close();
			jsonStr = sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonStr;
	}

	public static JSONArray getCityJSON() {
		File f = new File("country.json");
		JSONArray jsonArray = new JSONArray();

		String country = GetLocalJSON.getCountry(f);

		Gson gson = new Gson();
		List<Map<String, Object>> list = new ArrayList();
		list = gson.fromJson(country, List.class);
		
		for (int i = 0; i < list.size(); i++) {

			JSONObject json = new JSONObject();
			String city = (String) list.get(i).get("NAME_2014");
			String id = (String) list.get(i).get("ISO3166");

			if (!("").equals(city) && !("").equals(id)) {
				json.put("city", city);
				json.put("cityId", id);
			}
			if (!json.isEmpty()) {
				jsonArray.add(json);
			}
		}
		return jsonArray;
	}

	public static void main(String args[]) {
		JSONArray jsonArray = GetLocalJSON.getCityJSON();
		System.out.println(jsonArray);

	}

}
