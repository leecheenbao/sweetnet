package com.sweetNet.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonObject;
import com.sweetNet.until.CashUntil;
import com.sweetNet.until.SHA256Util;

@RestController
@RequestMapping("/api")
public class CashFlowController {
	/* 測試交易 */
	private static final String RECAPTCHA_SERVICE_URL = "https://ccore.newebpay.com/MPG/mpg_gateway";
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private static final String hashKey = "2pLphOdskTywJjx9HHAMuQOcjO1G4kUj";
	private static final String hashIV = "POKWoMqn4wPBiBFC";
	private static final String merchantID = "MS1425062640";

//	hashKey:x5rtPvNCStYqXVe0c2kaQgNhb2Lxk48k
//	HashIV:PMDI6OaO3ENQcvvC
	@GetMapping(value = "/cash")
	protected String getAllBulletin() throws IOException, ParseException {
		StringBuffer response = new StringBuffer();
		URL obj = new URL(RECAPTCHA_SERVICE_URL);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
		con.setRequestMethod("POST");
		try {

			Long timestamp = new Date().getTime();
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("MerchantID", merchantID);			//商店ID
			params.put("RespondType", "JSON");				//回應種類JSON/String
			params.put("TimeStamp", timestamp);				//時間戳記
			params.put("Version", "2.0");					//API版本，固定2.0
			params.put("MerchantOrderNo", "S" + timestamp);	//訂單編號，為唯一值
			params.put("Amt", "500");						//商品價格
			params.put("ItemDesc", "TEST");					//商品描述
			params.put("LoginType", 0);

			String dataInfo = CashUntil.getParams(params);

			dataInfo = "HashKey=" + hashKey + "&" + dataInfo + "&HashIV=" + hashIV;
			String tradeInfo = CashUntil.Encrypt(dataInfo, hashKey, hashIV);

			String tradeSha = SHA256Util.getSHA1Digest(tradeInfo);

			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("MerchantID", merchantID);
			jsonObject.addProperty("Version", "2.0");
			jsonObject.addProperty("TradeInfo", tradeInfo);
			jsonObject.addProperty("TradeSha", tradeSha);
			logger.info(jsonObject.toString());
			String postParams = jsonObject.toString();
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(postParams);
			wr.flush();
			wr.close();

			int responseCode = con.getResponseCode();

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return response.toString();
	}
}
