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
	
	private final TaskRepository taskRepository;
	
	public TaskService(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}
	
	public List<Task> showTaskList() {
		// セッション情報を取得
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		AccountUserDetails userDetails = (AccountUserDetails) auth.getPrincipal();
		Account loginUser = userDetails.getAccount();
		
		List<Task> taskList = taskRepository.findByUser(loginUser);
		
		return taskList;
		
	}
	
	public void createTask(Task task) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		AccountUserDetails userDetails = (AccountUserDetails) auth.getPrincipal();
		Account loginUser = userDetails.getAccount();
		
		// 送られてきた情報を格納
		Task createTask = new Task();
		// userIdは現在のログインユーザーから取得
		createTask.setUser(loginUser);
		createTask.setTitle(task.getTitle());
		createTask.setDescription(task.getDescription());
		createTask.setDueDate(task.getDueDate());
		createTask.setStatus(task.getStatus());
		createTask.setPriority(task.getPriority());
		createTask.setCategory(task.getCategory());
		// DBに保存
		taskRepository.save(createTask);
		
	}
	
	public void updateTask(Task task) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		AccountUserDetails userDetails = (AccountUserDetails) auth.getPrincipal();
		Account loginUser = userDetails.getAccount();
		
		task.setUser(loginUser);
		taskRepository.save(task);
	}
	
	public void deleteTask(Long id) {
		Task task = taskRepository.findById(id).orElseThrow();
		taskRepository.delete(task);
	}

}
