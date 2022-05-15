package com.sweetNet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sweetNet.model.Message;

public interface MessageRepository extends JpaRepository<Message, String> {
	List<Message> findByRecId(String redId);

}