package com.sweetNet.until;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendMail {

	private final String user = ConfigInfo.MAIL_USER;
	private final String pwd = ConfigInfo.MAIL_PASSWORD;
	private final String from = ConfigInfo.MAIL_USER;

	public void sendMail_SugarDaddy(String action, String memUuid, String mailTo) throws Exception {
		try {

			Session mailSession = Session.getInstance(setMailProperties(), new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(user, pwd);
				}
			});

			// 開啟Session的debug模式，這樣就可以查看到程序發送Email的運行狀態
//			mailSession.setDebug(true);

			// 產生整封 email 的主體 message
			MimeMessage message = new MimeMessage(mailSession);

			// 文字部份，注意 img src 部份要用 cid:接下面附檔的header
			MimeBodyPart textPart = new MimeBodyPart();
			StringBuffer html = new StringBuffer();
			if (action.equals("apiuser")) {
				message.setSubject(ConfigInfo.MAIL_SUBTITLE_SINGN);
				html = mailContent_signUp(memUuid);
			}
			if (action.equals("forget")) {
				message.setSubject(ConfigInfo.MAIL_SUBTITLE_FORGET);
				html = mailContent_Forget(memUuid);
			}

			if (action.equals("report_send")) {
				message.setSubject(ConfigInfo.MAIL_SUBTITLE_REPORT_SEND);
				html = mailContent_Report_Send(memUuid);
			}

			if (action.equals("report_rec")) {
				message.setSubject(ConfigInfo.MAIL_SUBTITLE_REPORT_REC);
				html = mailContent_Report_Rec(memUuid);
			}

			textPart.setContent(html.toString(), "text/html; charset=UTF-8");

			// 圖檔部份，注意 html 用 cid:image，則header要設<image>
			MimeBodyPart picturePart = new MimeBodyPart();
			FileDataSource fds = new FileDataSource(ConfigInfo.IMAGE_PATH + "/logo.png");
			picturePart.setDataHandler(new DataHandler(fds));
			picturePart.setFileName(fds.getName());
			picturePart.setHeader("Content-ID", "<image>");
			Multipart email = new MimeMultipart();
			email.addBodyPart(textPart);
			email.addBodyPart(picturePart);
			message.setContent(email);
			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from, ConfigInfo.MAIL_USERNAME));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(mailTo));
			// Send message
			Transport.send(message);

		} catch (AddressException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	public void sendMailToAdmin(String name, String mail, String content) {
		// 設定傳送基本資訊
		String mailTo = ConfigInfo.MAIL_USER;
		try {

			Session mailSession = Session.getInstance(setMailProperties(), new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(user, pwd);
				}
			});

			// 開啟Session的debug模式，這樣就可以查看到程序發送Email的運行狀態
//			mailSession.setDebug(true);

			// 產生整封 email 的主體 message
			MimeMessage message = new MimeMessage(mailSession);

			// 文字部份，注意 img src 部份要用 cid:接下面附檔的header
			MimeBodyPart textPart = new MimeBodyPart();
			StringBuffer html = new StringBuffer();
			message.setSubject(ConfigInfo.MAIL_SUBTITLE_CONNECT);
			html = mailContent_Connect(name, mail, content);
			textPart.setContent(html.toString(), "text/html; charset=UTF-8");

			// 圖檔部份，注意 html 用 cid:image，則header要設<image>
			Multipart email = new MimeMultipart();
			email.addBodyPart(textPart);
			message.setContent(email);
			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from, ConfigInfo.MAIL_USERNAME));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(mailTo));
			// Send message
			Transport.send(message);

		} catch (AddressException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public Properties setMailProperties() {
		Properties props = new Properties();
		props.put("mail.transport.protocol", "smtps");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.timeout", "30000");
		props.put("mail.smtp.connectiontimeout", "10000");
		props.put("mail.smtp.ssl.trust", "*");
		props.put("mail.smtp.debug", "true");
		props.put("mail.smtp.socketFactory.fallback", "false");
		return props;
	}

	public StringBuffer mailContent_Forget(String memUuid) {
		StringBuffer html = new StringBuffer();
		html.append("<h2>忘記密碼</h2><br>");
		html.append("<h3>請盡速修改你的密碼</h3><br>");
		html.append("<h3><a href='" + ConfigInfo.REAL_PATH + "/sweetNet/forget/" + memUuid + "'>啟用請點擊連結</a></h3><br>");
		html.append("<img src='cid:image'/><br>");

		return html;
	}

	public StringBuffer mailContent_signUp(String memUuid) {
		StringBuffer html = new StringBuffer();
		html.append("<h2>歡迎加入SugarDaddy</h2><br>");
		html.append("<h3>這是一封啟用郵件</h3><br>");
		html.append(
				"<h3><a href='" + ConfigInfo.REAL_PATH + "/sweetNet/api/user/" + memUuid + "'>啟用請點擊連結</a></h3><br>");
		html.append("<img src='cid:image'/><br>");
		return html;
	}

	public StringBuffer mailContent_Connect(String name, String mail, String content) {
		StringBuffer html = new StringBuffer();
		html.append("<h3>稱呼：" + name + "</h3><br>");
		html.append("<h3>聯絡信箱：" + mail + "</h3><br>");
		html.append("<h3>聯繫內容：</h3><br>");
		html.append("<div>" + content + "</div><br>");
		return html;
	}

	public StringBuffer mailContent_Report_Send(String memUuid) {
		StringBuffer html = new StringBuffer();
		html.append("<h2>您檢舉的案件已經成立</h2><br>");
		html.append("<h3>版主會盡速處理</h3><br>");
		html.append("<img src='cid:image'/><br>");

		return html;
	}

	public StringBuffer mailContent_Report_Rec(String memUuid) {
		StringBuffer html = new StringBuffer();
		html.append("<h2>您的行為可能有些不妥</h2><br>");
		html.append("<h3>請檢查是否有不當留言，或是不當的照片</h3><br>");
		html.append("<img src='cid:image'/><br>");

		return html;
	}

}