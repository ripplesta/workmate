package com.example.workmate.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.workmate.domain.Account;
import com.example.workmate.domain.Task;
import com.example.workmate.dto.TaskForm;
import com.example.workmate.repository.TaskRepository;

@Controller
@RequestMapping("/tasks")
public class TaskController {
	
	@Autowired
	private TaskRepository taskRepository;
	
	@GetMapping
	public String showTaskList(HttpSession session, Model model) {
		
		Account loginUser = (Account) session.getAttribute("loginUser");
		
		//ログイン情報がなければホーム画面にリダイレクト
		if(loginUser == null) {
			return "redirect:/home";
		}
		
		//Long userId = loginUser.getUserId();
		
		List<Task> taskList = taskRepository.findByUser(loginUser);
		
		
		model.addAttribute("tasks", taskList);
		
		return "tasklist";
	}
	
	@GetMapping("/new")
	public String newTaskForm(Model model) {
		model.addAttribute("taskForm", new TaskForm());
		return "createtask";
	}
	
	@PostMapping("/create")
	public String createTask(@ModelAttribute TaskForm taskForm, HttpSession session, Model model) {
		
		Account loginUser = (Account)session.getAttribute("loginUser");
		
		//フォームから送られてきたデータをDBに保存
		Task createTask = new Task();
		//userIdは現在のセッションから取得し保存
		createTask.setUser(loginUser);
		createTask.setTitle(taskForm.getTitle());
		createTask.setDescription(taskForm.getDescription());
		createTask.setDueDate(taskForm.getDueDate());
		
		taskRepository.save(createTask);
		
		return "tasklist";
	}
	
	@GetMapping("/edit/{id}")
	public String editTaskForm(@PathVariable Long id, Model model){
		Task task = taskRepository.findById(id);
		model.addAttribute("editTaskForm", Task);
		return "edittask";
	}
}

	
