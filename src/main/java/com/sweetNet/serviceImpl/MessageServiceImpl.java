package com.sweetNet.serviceImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.sweetNet.model.Message;
import com.sweetNet.repository.MessageRepository;
import com.sweetNet.service.MessageService;

@Service
public class MessageServiceImpl implements MessageService {
	@Autowired
	private MessageRepository messageRepository;

	@Override
	public List<Message> findAll() {
		return messageRepository.findAll();
	}

	@Override
	public void save(Message message) {
		messageRepository.save(message);
	}

	@Override
	public List<Message> findBySendIdAndRecId(String sendId, String recId, Integer states) {
		Integer total = this.findAll().size();
		Pageable pageable = PageRequest.of(0, total, Sort.Direction.ASC, "pdate");
		return messageRepository.findBySendIdAndRecIdAndStatesOrderByPdate(sendId, recId, states, pageable);
	}

	@Override
	public void delete(String id) {
		messageRepository.deleteById(id);

	}

	@Override
	public Set<String> findByRecId(String recId) {
		List<Message> messages = messageRepository.findByRecIdAndStates(recId, 0);
		Set set = new HashSet();
		for (Message message : messages) {
			String sendId = message.getSendId();
			set.add(sendId);
		}
		System.out.println(set);
		return set;
	}

}