package com.sweetNet.until;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256Util {
	public static String getSHA1Digest(String plainText) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			e.getMessage();

		}
		md.update(plainText.getBytes()); 
		byte[] output = md.digest();

		String digestVal = HexUtility.bytesToHex(output);
		// for security reason, 可以 dump digest value, 但不可以 dump plainText
		
		return digestVal;
	}
}
