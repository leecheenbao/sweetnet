package com.sweetNet.until;

import org.jboss.aerogear.security.otp.Totp;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

public class PhoneUtil {
	/* 驗證手機國碼 */
	public String checkPhone(String phoneNumber) {
		PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
		PhoneNumber swissNumberProto = new PhoneNumber();
		try {
			swissNumberProto = phoneUtil.parse(phoneNumber, "TW");

			String phone = phoneUtil.format(swissNumberProto, PhoneNumberFormat.E164);
			System.out.println(phone);
		} catch (NumberParseException e) {
			System.err.println("NumberParseException was thrown: " + e.toString());
		}
		return phoneUtil.format(swissNumberProto, PhoneNumberFormat.E164);
	}

	public String creatOTP(String secret) {
		Totp totp = new Totp(secret);
		return totp.now();
	}

	public Boolean verifyOTP(String secret, String OTP) {
		Totp totp = new Totp(secret);
		return totp.verify(OTP);
	}

//
//	private int generate(String secret, long interval) {
//		return hash(secret, interval);
//	}
//
//	private int hash(String secret, long interval) {
//		byte[] hash = new byte[0];
//		try {
//			// Base32 encoding is just a requirement for google authenticator. We can remove
//			// it on the next releases.
//			hash = new Hmac(Hash.SHA1, Base32.decode(secret), interval).digest();
//		} catch (NoSuchAlgorithmException e) {
//			e.printStackTrace();
//		} catch (InvalidKeyException e) {
//			e.printStackTrace();
//		} catch (Base32.DecodingException e) {
//			e.printStackTrace();
//		}
//		return bytesToInt(hash);
//	}
//
//	private int bytesToInt(byte[] hash) {
//		// put selected bytes into result int
//		int offset = hash[hash.length - 1] & 0xf;
//
//		int binary = ((hash[offset] & 0x7f) << 24) | ((hash[offset + 1] & 0xff) << 16)
//				| ((hash[offset + 2] & 0xff) << 8) | (hash[offset + 3] & 0xff);
//
//		return binary % Digits.SIX.getValue();
//	}
//
//	/**
//	 * Retrieves the current OTP
//	 *
//	 * @return OTP
//	 */
//	public String now() {
//		return leftPadding(hash(secret, Clock.getCurrentInterval()));
//	}
//
//	private String leftPadding(int otp) {
//		return String.format("%06d", otp);
//	}

}