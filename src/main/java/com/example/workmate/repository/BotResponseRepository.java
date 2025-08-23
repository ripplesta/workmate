package com.example.workmate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.workmate.domain.BotResponse;


public interface BotResponseRepository extends JpaRepository<BotResponse, Long> {
	List<BotResponse> findByKeywordContainingIgnoreCase(String keyword);
	@Query("SELECT res FROM BotResponse res WHERE :input LIKE CONCAT('%', res.keyword, '%')")
	List<BotResponse> findByInputMatchesKeyword(@Param("input") String input);
	// タグで検索できるように
	List<BotResponse> findByTag_Name(String tagName);

}
