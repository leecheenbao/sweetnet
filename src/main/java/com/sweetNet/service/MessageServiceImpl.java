package com.sweetNet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
		return messageRepository.findByMessageId(messageId);
	}

	@Override
	public List<Message> findByRecId(String redId) {
		return messageRepository.findByRecId(redId);
	}

	@Override
	public List<Message> findBySendId(String sendId) {
		return messageRepository.findBySendId(sendId);
	}

}