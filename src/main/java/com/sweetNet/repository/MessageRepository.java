package com.sweetNet.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sweetNet.model.Message;

public interface MessageRepository extends JpaRepository<Message, String> {
	List<Message> findByRecId(String redId);

	List<Message> findByRecIdAndStates(String redId, Integer states);

	List<Message> findBySendIdAndRecIdAndStatesOrderByPdate(String sendId, String recId, Integer states,
			Pageable pageable);

	@Query(value = "SELECT DISTINCT SENDID FROM MESSAGE m WHERE RECID =?1 ", nativeQuery = true)
	List<Message> getMemberList(String memUuid);
}