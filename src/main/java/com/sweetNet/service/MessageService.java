package com.sweetNet.service;

import com.sweetNet.model.Message;

public interface MessageService {

	Iterable<Message> findAll();

	void save(Message contact);

	Message findByMsgId(String messageId);
}
