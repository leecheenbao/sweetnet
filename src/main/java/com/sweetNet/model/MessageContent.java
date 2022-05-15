package com.sweetNet.model;

import java.io.Serializable;
import java.sql.Blob;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "MessageContent")
public class MessageContent implements Serializable {

	private static long serialVersionUID = 1L;

	@Id
	@Column(name = "messageId")
	private String messageId;

	@Column(name = "content")
	private Blob content;

	@Column(name = "pdate")
	private String pdate;

	public MessageContent() {
		super();
	}

	public MessageContent(String messageId, Blob content, String pdate) {
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