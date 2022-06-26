package com.sweetNet.dto;

import javax.validation.constraints.NotBlank;

public class PhoneOtpDTO {
	
	@NotBlank(message = "電話不可為空")
	private String memPhone;
	
	private String secret;
	
	@NotBlank(message = "OTP不可為空")
	private String otp;

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getMemPhone() {
		return memPhone;
	}

	public void setMemPhone(String memPhone) {
		this.memPhone = memPhone;
	}

}
