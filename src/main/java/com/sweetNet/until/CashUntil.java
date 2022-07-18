package com.sweetNet.until;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class CashUntil {
	public static void main(String[] args) throws Exception {
		String hashKey = "2pLphOdskTywJjx9HHAMuQOcjO1G4kUj";
		String hashIV = "POKWoMqn4wPBiBFC";
		String MerchantID = "MS1425062640";
		Long timestamp = new Date().getTime();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("MerchantID", MerchantID);
		params.put("RespondType", "JSON");
		params.put("TimeStamp", timestamp);
		params.put("Version", "2.0");
		params.put("LangType", "zh-tw");
		params.put("MerchantOrderNo", "sweet" + timestamp);
		params.put("Amt", "500");
		params.put("HashKey", hashKey);
		params.put("Email", "leecheenbao@gmail.com");

		/* 需要加密的字串 */
//		String content = "HashKey=2pLphOdskTywJjx9HHAMuQOcjO1G4kUj&5eaf8d2a36d0e658f212168ed367d8004d9992c91d56a621688565eb7fe362d73f9a3319b70c8d2d6043ce303f33214faa4a5a4dc1c2b066ea45b78597f5190cf01a340c5001f9304af460207a9c85e7d11421882c712679f5b0deefd7f112a6755379828ceca2a3b4a8305fa1a4c085b4be95c289c71e98201e8cb019c05e2ae93e092313c2526dfc7f6afa1a1bfcad79bc35818d47eeac54c130e57158485a&HashIV=POKWoMqn4wPBiBFC";
		String content = params.toString();
		String dataInfo = getParams(params);
		System.out.println("dataInfo ：\n" + dataInfo);
		System.out.println("==================================");
		/* 加密 */
		String TradeInfo = CashUntil.Encrypt(dataInfo, hashKey, hashIV);
		System.out.println("TradeInfo ：\n" + TradeInfo);
		System.out.println("==================================");

		/* SHA256 */
		String TradeSha = SHA256Util.getSHA1Digest(TradeInfo);
		System.out.println("TradeSha ：\n" + TradeSha);
		System.out.println("==================================");

		/* 解密 */
		String DeString = CashUntil.Decrypt(TradeInfo, hashKey, hashIV);
		System.out.println("解密后的字串是：\n" + DeString);
		System.out.println("==================================");

	}

	/* 將params組成藍新要求的格式 */
	public static String getParams(Map<String, Object> map) {
		Set<String> keys = map.keySet();

		StringBuffer sb = new StringBuffer();
		for (String key : keys) {
			sb.append(key + "=" + map.get(key) + "&");
		}
		String result = sb.toString();
		if (result.lastIndexOf("&") == result.length() - 1) {
			result = result.substring(0, result.length() - 1);
		}
		return result;
	}

	// 加密
	public static String Encrypt(String content, String hashKey, String hashIV) throws Exception {
		if (hashKey == null) {
			System.out.print("Key为空null");
			return null;
		}
		byte[] raw = hashKey.getBytes();
		SecretKeySpec hashKeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");// "算法/模式/补码方式"
		IvParameterSpec iv = new IvParameterSpec(hashIV.getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
		cipher.init(Cipher.ENCRYPT_MODE, hashKeySpec, iv);
		byte[] encrypted = cipher.doFinal(content.getBytes());

		return new BASE64Encoder().encode(encrypted);// 此处使用BASE64做转码功能，同时能起到2次加密的作用。
	}

	// 解密
	public static String Decrypt(String content, String hashKey, String hashIV) throws Exception {
		try {
			// 判断Key是否正确
			if (hashKey == null) {
				System.out.print("Key为空null");
				return null;
			}
			byte[] raw = hashKey.getBytes("ASCII");
			SecretKeySpec hashKeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			IvParameterSpec iv = new IvParameterSpec(hashIV.getBytes());
			cipher.init(Cipher.DECRYPT_MODE, hashKeySpec, iv);
			byte[] encrypted1 = new BASE64Decoder().decodeBuffer(content);// 先用base64解密
			try {
				byte[] original = cipher.doFinal(encrypted1);
				String originalString = new String(original);
				return originalString;
			} catch (Exception e) {
				System.out.println(e.toString());
				return null;
			}
		} catch (Exception ex) {
			System.out.println(ex.toString());
			return null;
		}
	}
}
