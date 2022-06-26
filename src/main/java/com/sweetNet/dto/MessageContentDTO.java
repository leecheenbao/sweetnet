package com.sweetNet.dto;

import java.sql.Blob;

public class MessageContentDTO {

	private String messageId;

	private Blob content;

	private String pdate;

	public MessageContentDTO() {
		super();
	}

	public MessageContentDTO(String messageId, Blob content, String pdate) {
		super();

		this.messageId = messageId;
		this.content = content;
		this.pdate = pdate;

	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public Blob getContent() {
		return content;
	}

	public void setContent(Blob content) {
		this.content = content;
	}

	public String getPdate() {
		return pdate;
	}

	public void setPdate(String pdate) {
		this.pdate = pdate;
	}

}