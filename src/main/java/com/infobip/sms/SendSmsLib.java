package com.infobip.sms;

import java.util.Collections;

import com.infobip.ApiClient;
import com.infobip.ApiException;
import com.infobip.api.SendSmsApi;
import com.infobip.model.SmsAdvancedTextualRequest;
import com.infobip.model.SmsDestination;
import com.infobip.model.SmsResponse;
import com.infobip.model.SmsTextualMessage;

/**
 * Send an SMS message by using Infobip API Java Client.
 *
 * THIS CODE EXAMPLE IS READY BY DEFAULT. HIT RUN TO SEND THE MESSAGE!
 *
 * Send SMS API reference:
 * https://www.infobip.com/docs/api#channels/sms/send-sms-message See Readme
 * file for details.
 */
public class SendSmsLib {

	private static final String BASE_URL = "https://4mwm9m.api.infobip.com";
	private static final String API_KEY = "b0b5721ba401bbb11dbf88a43e836d78-0dcc4e31-69c1-4efd-b029-b9d60bc7b357";

	private static final String SENDER = "InfoSMS";
	private static final String RECIPIENT = "886919268790";
	private static final String MESSAGE_TEXT = "This is a sample message";

	public static void main(String[] args) {
		ApiClient client = initApiClient();

		SendSmsApi sendSmsApi = new SendSmsApi(client);

		SmsTextualMessage smsMessage = new SmsTextualMessage().from(SENDER)
				.addDestinationsItem(new SmsDestination().to(RECIPIENT)).text(MESSAGE_TEXT);

		SmsAdvancedTextualRequest smsMessageRequest = new SmsAdvancedTextualRequest()
				.messages(Collections.singletonList(smsMessage));

		try {
			SmsResponse smsResponse = sendSmsApi.sendSmsMessage(smsMessageRequest);
			System.out.println("Response body: " + smsResponse);
		} catch (ApiException e) {
			System.out.println("HTTP status code: " + e.getCode());
			System.out.println("Response body: " + e.getResponseBody());
		}
	}

	private static ApiClient initApiClient() {
		ApiClient client = new ApiClient();

		client.setApiKeyPrefix("App");
		client.setApiKey(API_KEY);
		client.setBasePath(BASE_URL);

		return client;
	}

	
}
