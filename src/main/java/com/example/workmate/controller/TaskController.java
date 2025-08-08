package com.example.workmate.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.workmate.domain.Account;
import com.example.workmate.domain.Task;
import com.example.workmate.dto.TaskForm;
import com.example.workmate.repository.TaskRepository;
import com.example.workmate.security.AccountUserDetails;

@Controller
@RequestMapping("/tasks")
public class TaskController {
	
	@Autowired
	private TaskRepository taskRepository;
	
	//ここがタスク一覧でtasklistにアクセスするとここが実行される
	@GetMapping("/tasklist")
	public String showTaskList(@AuthenticationPrincipal AccountUserDetails userDetails, Model model) {
		
		Account loginUser = userDetails.getAccount();
		
		//ログイン情報がなければホーム画面にリダイレクト
		if(loginUser == null) {
			return "redirect:/home";
		}
		
		//Long userId = loginUser.getUserId();
		
		List<Task> taskList = taskRepository.findByUser(loginUser);
		
		
		model.addAttribute("tasks", taskList);
		
		return "tasks/tasklist";
	}
	
	//登録を押すとここが実行されて登録フォームに遷移する
	@GetMapping("/new")
	public String newTaskForm(Model model) {
		model.addAttribute("taskForm", new TaskForm());
		return "tasks/taskform";
	}
	
	//登録フォームで入力された情報が送られる
	@PostMapping("/create")
	public String createTask(@AuthenticationPrincipal AccountUserDetails userDetails, @ModelAttribute TaskForm taskForm, Model model) {
		
		Account loginUser = userDetails.getAccount();
		
		//フォームから送られてきたデータをDBに保存
		Task createTask = new Task();
		//userIdは現在のセッションから取得し保存
		createTask.setUser(loginUser);
		createTask.setTitle(taskForm.getTitle());
		createTask.setDescription(taskForm.getDescription());
		createTask.setDueDate(taskForm.getDueDate());
		createTask.setStatus(taskForm.getStatus());
		createTask.setPriority(taskForm.getPriority());
		createTask.setCategory(taskForm.getCategory());
		
		taskRepository.save(createTask);
		
		return "redirect:/tasks/tasklist";
	}
	
	//タスク各々にidが割り振られていて編集したいタスクのidを送って編集フォームに遷移する
	@GetMapping("/edit/{id}")
	public String editTaskForm(@PathVariable Long id, Model model){
		//タスクがあればtaskに格納、なくてもorElseThrow()で例外を投げる
		Task task = taskRepository.findById(id).orElseThrow();
		TaskForm taskForm = new TaskForm(task);
		model.addAttribute("taskForm", taskForm);
		return "tasks/taskform";
	}

	//編集フォームの更新データをDBに保存する
	@PostMapping("/edit/update")
	public String updateTask(@AuthenticationPrincipal AccountUserDetails userDetails, @ModelAttribute TaskForm taskForm){
		//送られてきたデータをTaskに格納
		Task task = Task.fromForm(taskForm);
		
		Account loginUser = userDetails.getAccount();
		task.setUser(loginUser);
		System.out.println(task.getId());
		System.out.println(task.getUser());
		taskRepository.save(task);
		return "redirect:/tasks/tasklist";
	}

	@PostMapping("/delete/{id}")
	public String deleteTask(@PathVariable Long id) {
		Task task = taskRepository.findById(id).orElseThrow();
		taskRepository.delete(task);
		return "redirect:/tasks/tasklist";
	}
}

	
