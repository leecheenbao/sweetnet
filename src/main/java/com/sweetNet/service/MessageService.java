package com.sweetNet.service;

import java.util.List;
import java.util.Set;

import com.sweetNet.model.Message;

public interface MessageService {

	List<Message> findAll();

	void save(Message message);

	void delete(String id);

	List<Message> findBySendIdAndRecId(String sendId, String recId, Integer states);

	Set<String> findByRecId(String recId);
}
