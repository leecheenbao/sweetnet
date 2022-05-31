package com.sweetNet.until;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @ClassName: ApiDemo
 * @Description: TODO
 *
 */
public class test {

	/**
	 * 簡訊提供商開設賬號時提供一下引數:
	 * 
	 * 賬號、密碼、通訊key、IP、埠
	 */
	static String account = "你的賬號";
	static String password = "你的密碼";
	static String veryCode = "通訊key";
	static String http_url = "172.26.64.1";

	public static String vcode = createRandomVcode();
	/**
	 * 預設字元編碼集
	 */
	public static final String CHARSET_UTF8 = "UTF-8";

	public static String createRandomVcode() {
		// 驗證碼
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < 4; i++) {
			buffer.append((int) (Math.random() * 9));
		}
		return buffer.toString();
	}

	/**
	 * 查詢賬號餘額
	 * 
	 * @return 賬號餘額，乘以10為簡訊條數 String xml字串，格式請參考文件說明
	 */
	public static String getBalance() {
		String balanceUrl = http_url + "/service/httpService/httpInterface.do?method=getAmount";
		Map<String, String> params = new HashMap<String, String>();
		params.put("username", account);
		params.put("password", password);
		params.put("veryCode", veryCode);
		String result = sendHttpPost(balanceUrl, params);
		return result;
	}

	/**
	 * 傳送普通簡訊 普通簡訊傳送需要人工稽核 請求地址：
	 * UTF-8編碼：/service/httpService/httpInterface.do?method=sendUtf8Msg
	 * GBK編碼：/service/httpService/httpInterface.do?method=sendGbkMsg
	 * 
	 * @param mobile  手機號碼, 多個號碼以英文逗號隔開,最多支援100個號碼
	 * @param content 簡訊內容
	 * @return String xml字串，格式請參考文件說明
	 */
	public static String sendSms(String mobile, String content) {
		String sendSmsUrl = http_url + "/service/httpService/httpInterface.do?method=sendMsg";
		Map<String, String> params = new HashMap<String, String>();
		params.put("username", account);
		params.put("password", password);
		params.put("veryCode", veryCode);
		params.put("mobile", mobile);
		params.put("content", content);
		params.put("msgtype", "1");
		params.put("code", "utf-8");
		String result = sendHttpPost(sendSmsUrl, params);
		return result;
	}

	/**
	 * 模版簡訊,無需人工稽核，直接傳送 (簡訊模版的建立參考客戶端操作手冊)
	 * 模版：@1@會員，動態驗證碼@2@(五分鐘內有效)。請注意保密，勿將驗證碼告知他人。 引數值:@1@=某某,@2@=4293
	 * 手機接收內容：【簡訊簽名】某某會員，動態驗證碼4293(五分鐘內有效)。請注意保密，勿將驗證碼告知他人。
	 * 
	 * 請求地址： UTF-8編碼：/service/httpService/httpInterface.do?method=sendUtf8Msg
	 * GBK編碼：/service/httpService/httpInterface.do?method=sendGbkMsg 注意：
	 * 1.傳送模板簡訊變數值不能包含英文逗號和等號（, =） 2.特殊字元進行轉義: + %2b 空格 %20 & %26 % %25
	 * 
	 * @param mobile  手機號碼
	 * @param tplId   模板編號，對應客戶端模版編號
	 * @param content 模板引數值，以英文逗號隔開，如：@1@=某某,@2@=4293
	 * @return xml字串，格式請參考文件說明
	 */
	public static String sendTplSms(String mobile, String tplId, String content) {
		String sendTplSmsUrl = http_url + "/service/httpService/httpInterface.do?method=sendUtf8Msg";
		Map<String, String> params = new HashMap<String, String>();
		params.put("username", account);
		params.put("password", password);
		params.put("veryCode", veryCode);
		params.put("mobile", mobile);
		params.put("content", content); // 變數值，以英文逗號隔開
		params.put("msgtype", "2"); // 2-模板簡訊
		params.put("tempid", tplId); // 模板編號
		params.put("code", "utf-8");
		String result = sendHttpPost(sendTplSmsUrl, params);
		return result;
	}

	/***
	 * 查詢下發簡訊的狀態報告
	 * 
	 * @return String xml字串，格式請參考文件說明
	 */
	public static String queryReport() {
		String reportUrl = http_url + "/service/httpService/httpInterface.do?method=queryReport";
		Map<String, String> params = new HashMap<String, String>();
		params.put("username", account);
		params.put("password", password);
		params.put("veryCode", veryCode);
		String result = sendHttpPost(reportUrl, params);

		return result;
	}

	/**
	 * 查詢上行回覆簡訊
	 * 
	 * @return String xml字串，格式請參考文件說明
	 */
	public static String queryMo() {
		String moUrl = http_url + "/service/httpService/httpInterface.do?method=queryMo";
		Map<String, String> params = new HashMap<String, String>();
		params.put("username", account);
		params.put("password", password);
		params.put("veryCode", veryCode);
		String result = sendHttpPost(moUrl, params);
		return result;
	}

	/***
	 * 
	 * @param apiUrl    介面請求地址
	 * @param paramsMap 請求引數集合
	 * @return xml字串，格式請參考文件說明 String
	 */
	private static String sendHttpPost(String apiUrl, Map<String, String> paramsMap) {
		String responseText = "";
		StringBuilder params = new StringBuilder();
		Iterator<Map.Entry<String, String>> iterator = paramsMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, String> entry = iterator.next();
			params.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
		}

		try {
			URL url = new URL(apiUrl);
			URLConnection connection = url.openConnection();
			connection.setDoOutput(true);
			OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), CHARSET_UTF8);
			out.write(params.toString()); // post的關鍵所在！
			out.flush();
			out.close();
			// 讀取響應返回值
			InputStream is = connection.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is, CHARSET_UTF8));
			String temp = "";
			while ((temp = br.readLine()) != null) {
				responseText += temp;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return responseText;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
//		 查詢賬號餘額
		 System.out.println(getBalance());

		// 傳送普通簡訊，修改接收簡訊的手機號碼及簡訊內容,多個號碼以英文逗號隔開，最多支援100個號碼
		// System.out.println(sendSms("159*******1,159*******2",
		// "您的驗證碼是8888,請注意保密，勿將驗證碼告知他人。"));

		// System.out.println(sendTplSms("這裡是你的手機號碼","你的模板的編號","引數=value"));

		// 查詢下發簡訊的狀態報告
//      System.out.println(queryReport());

		// 查詢上行回覆簡訊
		// System.out.println(queryMo());
	}

}