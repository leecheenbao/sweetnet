package com.sweetNet.service;

import com.sweetNet.model.MessageContent;

public interface MessageContentService {

	Iterable<MessageContent> findAll();

	void save(MessageContent contact);

	MessageContent findByMsgId(String messageId);
}
