package com.sweetNet.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.sweetNet.model.Message;
import com.sweetNet.repository.MessageRepository;

@Service
public class MessageServiceImpl implements MessageService {
	@Autowired
	private MessageRepository messageRepository;

	@Override
	public List<Message> findAll() {
		return messageRepository.findAll();
	}

	@Override
	public void save(Message contact) {
		messageRepository.save(contact);
	}

	@Override
	public Message findByMsgId(String messageId) {

		Message m = new Message();
		m.setMessageId(messageId);

		Example<Message> example = Example.of(m);
		Optional<Message> mOptional = messageRepository.findOne(example);
		if (mOptional.isPresent())
			m = mOptional.orElseThrow(null);

		return m;
	}

}