package com.sweetNet.model;

import com.sweetNet.until.ConfigInfo;

public class JsonResult<T> {
	private T data;
	private String code;
	private String msg;

	/**
	 * 若沒有data返回，默認狀態碼為0
	 */
	public JsonResult(T data) {
		this.data = data;
		this.code = ConfigInfo.DATA_OK;
		this.msg = ConfigInfo.SYS_MESSAGE_SUCCESS;
	}

	public JsonResult() {
		this.code = ConfigInfo.DATA_OK;
		this.msg = ConfigInfo.SYS_MESSAGE_SUCCESS;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
