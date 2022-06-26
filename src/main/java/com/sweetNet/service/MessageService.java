package com.sweetNet.service;

import java.util.List;

import com.sweetNet.model.Message;

public interface MessageService {

	List<Message> findAll();

	void save(Message contact);

	Message findByMsgId(String messageId);

	List<Message> findByRecId(String redId);

	List<Message> findBySendId(String sendId);
}
