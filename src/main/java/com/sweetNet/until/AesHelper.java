package com.sweetNet.until;

import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/** AES 加解密 **/
public class AesHelper {
	private static String TAG = AesHelper.class.getSimpleName();
	private static int KEY_LENGTH = KeyLength.KEY128;
	private static String secKey = "m79p*H0**Yhu*CV8";

	public static class KeyLength {
		public static final int KEY128 = 16;
		public static final int KEY256 = 32;
	}

	public static void setKeyLength(int keyLength) {
		KEY_LENGTH = keyLength;
	}

	private static SecretKeySpec createKey() {
		byte[] data = null;
		StringBuffer sb = new StringBuffer(KEY_LENGTH);
		sb.append(secKey);

		// 依 KEY_LENGTH 判斷是否補 0
		while (sb.length() < KEY_LENGTH) {
			sb.append("0");
		}

		if (sb.length() > KEY_LENGTH) {
			sb.setLength(KEY_LENGTH);
		}

		try {
			data = sb.toString().getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return new SecretKeySpec(data, "AES");
	}

	public static byte[] encrypt(byte[] content) {
		try {
			SecretKeySpec key = createKey();
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] result = cipher.doFinal(content);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String encrypt(String content) {
		byte[] data = null;
		try {
			data = content.getBytes("UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		data = encrypt(data);
		String result = convbyte2hex(data);
		return result;
	}

	public static byte[] decrypt(byte[] content) {
		try {
			SecretKeySpec key = createKey();
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] result = cipher.doFinal(content);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String decrypt(String content) {
		byte[] data = null;
		try {
			data = convhex2byte(content);
		} catch (Exception e) {
			e.printStackTrace();
		}
		data = decrypt(data);
		if (data == null)
			return null;
		String result = null;
		try {
			result = new String(data, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return result;
	}

	public static String convbyte2hex(byte[] b) {
		StringBuffer sb = new StringBuffer(b.length * 2);
		String tmp = "";
		for (int n = 0; n < b.length; n++) {
			tmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (tmp.length() == 1) {
				sb.append("0");
			}
			sb.append(tmp);
		}
		return sb.toString().toUpperCase();
	}

	public static byte[] convhex2byte(String inputString) {
		if (inputString == null || inputString.length() < 2) {
			return new byte[0];
		}
		inputString = inputString.toLowerCase();
		int l = inputString.length() / 2;
		byte[] result = new byte[l];
		for (int i = 0; i < l; ++i) {
			String tmp = inputString.substring(2 * i, 2 * i + 2);
			result[i] = (byte) (Integer.parseInt(tmp, 16) & 0xFF);
		}
		return result;
	}
	
	public static void main(String[] args) {
		String str = "admin";
		System.out.println(AesHelper.encrypt(str));
	}
}