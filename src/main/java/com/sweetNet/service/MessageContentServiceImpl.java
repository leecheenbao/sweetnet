package com.sweetNet.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.sweetNet.model.MessageContent;
import com.sweetNet.repository.MessageContentRepository;

@Service
public class MessageContentServiceImpl implements MessageContentService {
	@Autowired
	private MessageContentRepository messageContentRepository;

	@Override
	public List<MessageContent> findAll() {
		return messageContentRepository.findAll();
	}

	@Override
	public void save(MessageContent contact) {
		messageContentRepository.save(contact);

	}

	@Override
	public MessageContent findByMsgId(String messageId) {
		MessageContent mc = new MessageContent();
		mc.setMessageId(messageId);

		Example<MessageContent> example = Example.of(mc);
		Optional<MessageContent> mcOptional = messageContentRepository.findOne(example);
		if (mcOptional.isPresent())
			mc = mcOptional.orElseThrow(null);

		return mc;
	}

}