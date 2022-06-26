package com.sweetNet.dto;

public class Message {

	private Integer id;

	private String messageId;

	private String sendId;

	private String recId;

	private Integer states;

	public Message() {
		super();
	}

	public Message(Integer id, String messageId, String sendId, String recId, Integer states) {
		super();

		this.id = id;
		this.messageId = messageId;
		this.sendId = sendId;
		this.recId = recId;
		this.states = states;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getSendId() {
		return sendId;
	}

	public void setSendId(String sendId) {
		this.sendId = sendId;
	}

	public String getRecId() {
		return recId;
	}

	public void setRecId(String recId) {
		this.recId = recId;
	}

	public Integer getStates() {
		return states;
	}

	public void setStates(Integer states) {
		this.states = states;
	}

}