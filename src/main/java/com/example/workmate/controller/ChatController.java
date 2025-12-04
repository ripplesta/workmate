package com.example.workmate.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.workmate.domain.Account;
import com.example.workmate.domain.ChatMessage;
import com.example.workmate.domain.Task;
import com.example.workmate.dto.Command;
import com.example.workmate.repository.BotResponseRepository;
import com.example.workmate.repository.ChatMessageRepository;
import com.example.workmate.security.AccountUserDetails;
import com.example.workmate.service.ChatBotService;
import com.example.workmate.service.TaskService;
import com.example.workmate.util.CommandAlias;
import com.example.workmate.util.CommandParser;

@Controller
@RequestMapping("/chat")
public class ChatController {
	
	private final ChatBotService chatBotService;
	private final ChatMessageRepository chatMessageRepository;
	private final BotResponseRepository botResponseRepository;
	private final TaskService taskService;
	
	public ChatController(ChatBotService chatBotService, ChatMessageRepository chatMessageRepository, BotResponseRepository botResRepository, TaskService taskService) {
		this.chatBotService = chatBotService;
		this.chatMessageRepository = chatMessageRepository;
		this.botResponseRepository = botResRepository;
		this.taskService = taskService;
	}
	
	@GetMapping
	public String chatPage(@AuthenticationPrincipal AccountUserDetails userDetails, Model model) {
		Account loginUser = userDetails.getAccount();
		List<ChatMessage> message = chatMessageRepository.findByUserOrderByCreatedAtAsc(loginUser);
		
		for(ChatMessage msg : message) {
			// メッセージに改行したいというのを認識させる
			msg.setMessageText(msg.getMessageText().replace("\n", "<br>"));
		}
		model.addAttribute("message", message);
		return "chatbot/chat";
	}

	@PostMapping
	public String sendMessage(@AuthenticationPrincipal AccountUserDetails userDetails, @RequestParam String message) {
		Account loginUser = userDetails.getAccount();
		CommandParser parser =  new CommandParser();
		Command command = parser.parse(message);
		String action = CommandAlias.normalizeAction(command.getAction());
		
		// コマンドアクション
		if(action.equals("list")) {
			List<Task> tasks = taskService.commandListAction(command);
			String response = taskService.formatResponse(tasks);
			if(response.isEmpty()) {
				response = "タスクが見つかりませんでした";
			}
			chatBotService.handleUserMessage(loginUser, message, response);
		}
		else if(action.equals("add")) {
			String response = taskService.commandCreateAction(command);
			chatBotService.handleUserMessage(loginUser, message, response);
		}
		else if(action.equals("update")) {
			String response = taskService.commandUpdateAction(command);
			chatBotService.handleUserMessage(loginUser, message, response);
		}
		else if(action.equals("done")) {
			String response = taskService.commandDoneAction(command);
			chatBotService.handleUserMessage(loginUser, message, response);
		}
		else if(action.equals("doing")) {
			String response = taskService.commandDoingAction(command);
			chatBotService.handleUserMessage(loginUser, message, response);
		}

		else if(action.equals("todo")) {
			String response = taskService.commandTodoAction(command);
			chatBotService.handleUserMessage(loginUser, message, response);
		}
		else if(action.equals("help")) {
			String response = taskService.commandHelpAction(command);
			chatBotService.handleUserMessage(loginUser, message, response);
		}
		else if(action.equals("stats")) {
			String response = taskService.commandStatsAction(command);
			chatBotService.handleUserMessage(loginUser, message, response);
		}
		
		// コマンドじゃないなら雑談系に分岐
		else {
			chatBotService.handleUserMessage(loginUser, message);
		}
		return "redirect:/chat";
	}
}
