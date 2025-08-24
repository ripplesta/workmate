package com.example.workmate.service;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.workmate.domain.Account;
import com.example.workmate.domain.Task;
import com.example.workmate.repository.TaskRepository;
import com.example.workmate.security.AccountUserDetails;


@Service
public class TaskService {
	
	private TaskRepository taskRepository;
	
	public List<Task> showTaskList() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		AccountUserDetails userDetails = (AccountUserDetails) auth.getPrincipal();
		
		Account loginUser = userDetails.getAccount();
		List<Task> taskList = taskRepository.findByUser(loginUser);
		
		return taskList;
	}

}
