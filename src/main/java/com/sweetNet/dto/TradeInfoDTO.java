package com.sweetNet.dto;

import java.util.Date;

public class TradeInfoDTO {
	private String merchantId;
	private Long timeStamp = new Date().getTime();
	private String version = "2.0";
	private String langType = "zh-tw";
	private String merchanOrderNo;
	private Integer amt;
	private String itemDesc;
	private Integer tradeLimit;
	private String expireDate;
	private String reutrnURL;
	
}
