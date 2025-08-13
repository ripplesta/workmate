package com.example.workmate.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.workmate.domain.Account;
import com.example.workmate.domain.ChatMessage;
import com.example.workmate.repository.ChatMessageRepository;
import com.example.workmate.security.AccountUserDetails;
import com.example.workmate.service.ChatBotService;

@Controller
@RequestMapping("/chat")
public class ChatController {
	
	private final ChatBotService chatBotService;
	private final ChatMessageRepository chatMessageRepository;
	
	public ChatController(ChatBotService chatBotService, ChatMessageRepository chatMessageRepository) {
		this.chatBotService = chatBotService;
		this.chatMessageRepository = chatMessageRepository;
	}
	
	@GetMapping
	public String chatPage(@AuthenticationPrincipal AccountUserDetails userDetails, Model model) {
		Account loginUser = userDetails.getAccount();
		List<ChatMessage> message = chatMessageRepository.findByUserOrderByCreatedAtAsc(loginUser);
		model.addAttribute("message", message);
		return "chat";
	}

}
