package com.sweetNet.service;

import java.util.List;

import com.sweetNet.model.MessageContent;

public interface MessageContentService {

	List<MessageContent> findAll();

	void save(MessageContent contact);

	MessageContent findByMessageId(String messageId);
}
