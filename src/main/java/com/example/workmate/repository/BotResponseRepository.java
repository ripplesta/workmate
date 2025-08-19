package com.example.workmate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.workmate.domain.BotResponse;

public interface BotResponseRepository extends JpaRepository<BotResponse, Long> {
	List<BotResponse> findByKeywordContainingIgnoreCase(String keyword);

}
