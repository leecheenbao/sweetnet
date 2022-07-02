package com.sweetNet.until;

import java.io.FileOutputStream;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMail {

	public static void main(String[] args) {
		SendMail smd = new SendMail();
		try {
			smd.sendMail_SugarDaddy("test", "test", "leecheenbao@gmail.com");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param subject 主旨
	 * @param content 內容
	 * @param mailTo  收件人
	 * @throws Exception
	 */
	public void sendMail_SugarDaddy(String subject, String content, String mailTo) throws Exception {
		// 設定傳送基本資訊

		String to = mailTo;

		String from = ConfigInfo.MAIL_USER;
		final String user = ConfigInfo.MAIL_USER;
		final String pwd = ConfigInfo.MAIL_PASSWORD;

		Properties props = new Properties();
		props.put("mail.transport.protocol", "smtps");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.timeout", "30000");
		props.put("mail.smtp.connectiontimeout", "10000");
		props.put("mail.smtp.ssl.trust", "*");
		props.put("mail.smtp.debug", "true");
		props.put("mail.smtp.socketFactory.fallback", "false");

		Session mailSession = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, pwd);
			}
		});

		// 開啟Session的debug模式，這樣就可以查看到程序發送Email的運行狀態
		mailSession.setDebug(true);

		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(mailSession);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from, "SugarDaddy"));

			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setHeader("Content-Type", "text/plain; charset=UTF-8");
			message.setSubject(subject, "utf-8");
			message.setContent(content, "text/plain;charset=UTF-8");
			message.saveChanges();
			message.writeTo(new FileOutputStream("htmlMail.eml"));

			// Send message
			Transport.send(message);

		} catch (MessagingException mex) {
			mex.printStackTrace();
			System.out.println("* sweetNet * 寄送失敗，MessagingException = " + mex);

		}
	}

}