package com.sweetNet.until;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.ParseException;

import com.google.gson.Gson;

public class VerifyRecaptcha {
	private static final String RECAPTCHA_SERVICE_URL = "https://www.google.com/recaptcha/api/siteverify";
	private static final String SECRET_KEY = "6Lcc9-AgAAAAAK2gzAZdUjMQ2NTrgXTcjXibrTjJ";
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * checks if a user is valid
	 * 
	 * @param clientRecaptchaResponse
	 * @return true if human, false if bot
	 * @throws IOException
	 * @throws ParseException
	 */
	public static boolean isValid(String clientRecaptchaResponse) throws IOException, ParseException {
		if (clientRecaptchaResponse == null || "".equals(clientRecaptchaResponse)) {
			return false;
		}

		URL obj = new URL(RECAPTCHA_SERVICE_URL);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

		con.setRequestMethod("POST");
		con.setRequestProperty("Accept-Language", "de-CH");

		// add client result as post parameter
		String postParams = "secret=" + SECRET_KEY + "&response=" + clientRecaptchaResponse;

		// send post request to google recaptcha server
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(postParams);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		new VerifyRecaptcha().logger.info("Post parameters: " + postParams);
		new VerifyRecaptcha().logger.info("Response Code: " + responseCode);
		System.out.println("Post parameters: " + postParams);
		System.out.println("Response Code: " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		System.out.println(response.toString());

		// Parse JSON-response
		Gson gson = new Gson();
		Map map = gson.fromJson(response.toString(), Map.class);

		Boolean success = (Boolean) map.get("success");
		Double score = (Double) map.get("score");

		new VerifyRecaptcha().logger.info("success : " + success);
		new VerifyRecaptcha().logger.info("score : " + score);
		// result should be sucessfull and spam score above 0.5
		return (success && score >= 0.5);
	}
}
