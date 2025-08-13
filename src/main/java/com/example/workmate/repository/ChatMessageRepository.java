package com.example.workmate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.workmate.domain.Account;
import com.example.workmate.domain.ChatMessage;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
	List<ChatMessage> findByUserOrderByCreatedAtAsc(Account user);

}
