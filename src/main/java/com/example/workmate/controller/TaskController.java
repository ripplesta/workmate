package com.example.workmate.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.workmate.domain.Account;
import com.example.workmate.domain.Task;
import com.example.workmate.repository.TaskRepository;

@Controller
public class TaskController {
	
	@Autowired
	private TaskRepository taskRepository;
	
	@GetMapping("/tasks")
	public String showTaskList(HttpSession session, Model model) {
		
		Account loginUser = (Account) session.getAttribute("loginUser");
		
		if(loginUser == null) {
			return "redirect:/home";
		}
		
		Long userId = loginUser.getUserId();
		
		List<Task> taskList = taskRepository.findByUserId(userId);
		
		model.addAttribute("taskList", taskList);
		
		return "tasks";
	}

}
