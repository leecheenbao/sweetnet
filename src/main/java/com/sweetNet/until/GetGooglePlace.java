package com.sweetNet.until;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

public class GetGooglePlace {

	public static void main(String[] args) {
		try {
			GetGooglePlace.test();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static final String MY_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=24.95375,121.22575&radius=500&types=food&name=吃到飽&language=zh-TW&key=";
	private static final String MY_KEY = "AIzaSyAqd2T9uz6V8f1jJOTS0EFDB5gQyTf8Mc0";

	public static void test() throws IOException {
		StringBuilder sb = new StringBuilder();
		URL url = new URL(MY_URL + MY_KEY);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.setUseCaches(false);
		con.setDoInput(true);
		int statusCode = con.getResponseCode();
		System.out.println("Status Code = " + statusCode);
		InputStream is = con.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);

		String str;
		while ((str = br.readLine()) != null)
			sb.append(str);

		br.close();
		isr.close();
		is.close();
		con.disconnect();

		System.out.println(sb.toString());
		Gson gson = new Gson();
		Map map = gson.fromJson(sb.toString(), HashMap.class);
		System.out.println(map.get("results"));
		// Parse JSON

	}
}
