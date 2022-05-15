package com.sweetNet.until;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

import com.alibaba.fastjson.JSONObject;

public class httpPostWithJson {

	private static final String BASE_URL = "http://localhost:8082/sweetNet/api/user";
	private static final String API_KEY = "App b0b5721ba401bbb11dbf88a43e836d78-0dcc4e31-69c1-4efd-b029-b9d60bc7b357";
	private static final String MEDIA_TYPE = "application/json";

	private static final String SENDER = "InfoSMS";
	private static final String RECIPIENT = "886919268790";
	private static final String MESSAGE_TEXT = "This is a sample message";

	public static void main(String[] args) {
		
//		String bodyJson = String.format(
//				"{\"messages\":[{\"from\":\"%s\",\"destinations\":[{\"to\":\"%s\"}],\"text\":\"%s\"}]}", SENDER,
//				RECIPIENT, MESSAGE_TEXT);
		JSONObject json = new JSONObject();
		json.put("memMail", "adminadmin@gmail.com");
		json.put("memPwd", "adminmemSexmemSex");
		json.put("memNickname", "admin");
		json.put("memDep", "admin");
		json.put("memSex", "admin");
//		json = (JSONObject) json.parse(bodyJson);
		System.out.println(json);
		boolean isSuccess = httpPostWithJson(json, BASE_URL, API_KEY);
	}

	public static boolean httpPostWithJson(JSONObject jsonObj, String url, String appId) {
		boolean isSuccess = false;

		HttpPost post = null;
		try {
			HttpClient httpClient = new DefaultHttpClient();

			// 设置超时时间
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 2000);
			httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 2000);

			post = new HttpPost(url);
			// 构造消息头
			post.setHeader("Authorization", appId);
			post.setHeader("Content-type", "application/json");
			post.setHeader("Accept", "application/json");

			// 构建消息实体
			StringEntity entity = new StringEntity(jsonObj.toString(), Charset.forName("UTF-8"));
			entity.setContentEncoding("UTF-8");
			// 发送Json格式的数据请求
			entity.setContentType("application/json");
			post.setEntity(entity);

			HttpResponse response = httpClient.execute(post);

			// 检验返回码
			int statusCode = response.getStatusLine().getStatusCode();
			String text = (String) response.toString();
			if (statusCode != HttpStatus.SC_OK) {
				isSuccess = false;
			} else {
				int retCode = 0;
				String sessendId = "";
				// 返回码中包含retCode及会话Id
				for (Header header : response.getAllHeaders()) {

					System.out.println(header.getName());
//					if (header.getName().equals("Connection")) {
//						retCode = Integer.parseInt(header.getValue());
//					}
					if (header.getName().equals("Connection")) {
						sessendId = header.getValue();
						System.out.println(sessendId);
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			isSuccess = false;
		} finally {
			if (post != null) {
				try {
					post.releaseConnection();
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		return isSuccess;
	}

	private static final String MY_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=24.95375,121.22575&radius=500&types=food&name=吃到飽&language=zh-TW&key=AIzaSyAYmC8oUYc9DGAZn8hqZKakFeclhAbTRSI";

	public static void test() throws IOException {
		StringBuilder sb = new StringBuilder();
		URL url = new URL(MY_URL);
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

		// Parse JSON

	}
}
