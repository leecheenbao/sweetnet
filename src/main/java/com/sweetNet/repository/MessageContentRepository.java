package com.sweetNet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sweetNet.model.MessageContent;

public interface MessageContentRepository extends JpaRepository<MessageContent, String> {
	List<MessageContent> findByMessageId(String messageId);

}