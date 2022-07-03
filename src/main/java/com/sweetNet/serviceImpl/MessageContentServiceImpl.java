package com.sweetNet.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sweetNet.model.MessageContent;
import com.sweetNet.repository.MessageContentRepository;
import com.sweetNet.service.MessageContentService;

@Service
public class MessageContentServiceImpl implements MessageContentService {
	@Autowired
	private MessageContentRepository messageContentRepository;

	@Override
	public List<MessageContent> findAll() {
		List<MessageContent> messageContents = messageContentRepository.findAll();
		return messageContents;
	}

	@Override
	public void save(MessageContent contact) {
		messageContentRepository.save(contact);

	}

	@Override
	public MessageContent findByMessageId(String messageId) {
		return messageContentRepository.findByMessageId(messageId);
	}

}