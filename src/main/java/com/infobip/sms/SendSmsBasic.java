package com.infobip.sms;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Send an SMS message directly by calling HTTP endpoint.
 *
 * THIS CODE EXAMPLE IS READY BY DEFAULT. HIT RUN TO SEND THE MESSAGE!
 *
 * Send SMS API reference:
 * https://www.infobip.com/docs/api#channels/sms/send-sms-message See Readme
 * file for details.
 */
public class SendSmsBasic {

	private static final String BASE_URL = "https://4mwm9m.api.infobip.com";
	private static final String API_KEY = "App b0b5721ba401bbb11dbf88a43e836d78-0dcc4e31-69c1-4efd-b029-b9d60bc7b357";
	private static final String MEDIA_TYPE = "application/json";

	private static final String SENDER = "InfoSMS";
	private static final String RECIPIENT = "886919268790";
	private static final String MESSAGE_TEXT = "This is a sample message test";

	public static void main(String[] args) throws IOException {
		OkHttpClient client = new OkHttpClient().newBuilder().build();

		String bodyJson = String.format(
				"{\"messages\":[{\"from\":\"%s\",\"destinations\":[{\"to\":\"%s\"}],\"text\":\"%s\"}]}", SENDER,
				RECIPIENT, MESSAGE_TEXT);

		MediaType mediaType = MediaType.parse(MEDIA_TYPE);
		RequestBody body = RequestBody.create(mediaType, bodyJson);

		Request request = prepareHttpRequest(body);
		Response response = client.newCall(request).execute();

		System.out.println("HTTP status code: " + response.code());
		System.out.println("Response body: " + response.body().string());

		SendSmsBasic ssb = new SendSmsBasic();
		ssb.sendSMS(RECIPIENT, MESSAGE_TEXT);
	}

	private static Request prepareHttpRequest(RequestBody body) {
		return new Request.Builder().url(String.format("%s/sms/2/text/advanced", BASE_URL)).method("POST", body)
				.addHeader("Authorization", API_KEY).addHeader("Content-Type", MEDIA_TYPE)
				.addHeader("Accept", MEDIA_TYPE).build();
	}

	public static void sendSMS(String recipient, String messageText) {
		OkHttpClient client = new OkHttpClient().newBuilder().build();
		try {
			String bodyJson = String.format(
					"{\"messages\":[{\"from\":\"%s\",\"destinations\":[{\"to\":\"%s\"}],\"text\":\"%s\"}]}", SENDER,
					recipient, messageText);

			MediaType mediaType = MediaType.parse(MEDIA_TYPE);
			RequestBody body = RequestBody.create(mediaType, bodyJson);

			Request request = prepareHttpRequest(body);
			Response response = client.newCall(request).execute();
			System.out.println("HTTP status code: " + response.code());
			System.out.println("Response body: " + response.body().string());

			response = client.newCall(request).execute();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
