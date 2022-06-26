package com.sweetNet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sweetNet.model.MessageContent;

public interface MessageContentRepository extends JpaRepository<MessageContent, String> {
	MessageContent findByMessageId(String messageId);

}