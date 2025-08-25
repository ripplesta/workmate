package com.example.workmate.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.workmate.domain.Account;
import com.example.workmate.domain.Task;
import com.example.workmate.dto.TaskForm;
import com.example.workmate.repository.TaskRepository;
import com.example.workmate.security.AccountUserDetails;
import com.example.workmate.service.TaskService;

@Controller
@RequestMapping("/tasks")
public class TaskController {
	
	private final TaskRepository taskRepository;
	private final TaskService taskService;
	
	public TaskController(TaskRepository taskRepository, TaskService taskService) {
		this.taskRepository = taskRepository;
		this.taskService = taskService;
	}
	
	// ここがタスク一覧でtasklistにアクセスするとここが実行される
	@GetMapping("/tasklist")
	public String showTaskList(@AuthenticationPrincipal AccountUserDetails userDetails, Model model) {
		
		Account loginUser = userDetails.getAccount();
		
		// ログイン情報がなければホーム画面にリダイレクト
		if(loginUser == null) {
			return "redirect:/home";
		}
		
		//List<Task> taskList = taskRepository.findByUser(loginUser);
		model.addAttribute("tasks", taskService.showTaskList());
		
		return "tasks/tasklist";
	}
	
	// 登録を押すとここが実行されて登録フォームに遷移する
	@GetMapping("/new")
	public String newTaskForm(Model model) {
		model.addAttribute("taskForm", new TaskForm());
		return "tasks/taskform";
	}
	
	// 登録フォームで入力された情報が送られる
	@PostMapping("/create")
	public String create(@ModelAttribute TaskForm taskForm) {
		
		//Account loginUser = userDetails.getAccount();
		
//		// フォームから送られてきたデータを格納
//		Task createTask = new Task();
//		// userIdは現在のセッションから取得し保存
//		createTask.setUser(loginUser);
//		createTask.setTitle(taskForm.getTitle());
//		createTask.setDescription(taskForm.getDescription());
//		createTask.setDueDate(taskForm.getDueDate());
//		createTask.setStatus(taskForm.getStatus());
//		createTask.setPriority(taskForm.getPriority());
//		createTask.setCategory(taskForm.getCategory());
//		// DBに保存
//		taskRepository.save(createTask);
		Task task = Task.fromForm(taskForm);
		taskService.createTask(task);
		
		return "redirect:/tasks/tasklist";
	}
	
	// タスク各々にidが割り振られていて編集したいタスクのidを送って編集フォームに遷移する
	@GetMapping("/edit/{id}")
	public String editTaskForm(@PathVariable Long id, Model model){
		// タスクがあればtaskに格納、なくてもorElseThrow()で例外を投げる
		Task task = taskRepository.findById(id).orElseThrow();
		TaskForm taskForm = new TaskForm(task);
		model.addAttribute("taskForm", taskForm);
		return "tasks/taskform";
	}

	// 編集フォームの更新データをDBに保存する
	@PostMapping("/edit/update")
	public String updateTask(@AuthenticationPrincipal AccountUserDetails userDetails, @ModelAttribute TaskForm taskForm){
		// 送られてきたデータをtaskに格納
		Task task = Task.fromForm(taskForm);
		
		//Account loginUser = userDetails.getAccount();
		//task.setUser(loginUser);
		//taskRepository.save(task);
		taskService.updateTask(task);
		return "redirect:/tasks/tasklist";
	}

	// 削除したいタスクのidをURLパラメータで送って削除
	@PostMapping("/delete/{id}")
	public String deleteTask(@PathVariable Long id) {
		//Task task = taskRepository.findById(id).orElseThrow();
		//taskRepository.delete(task);
		taskService.deleteTask(id);
		return "redirect:/tasks/tasklist";
	}
	
	// 検索やソートの組み合わせを実行
	@GetMapping("/search")
	public String searchTasks(@RequestParam(required = false) String title,
							  @RequestParam(required = false) String status,
							  @RequestParam(required = false) String category,
							  @RequestParam(required = false) String priority,
							  @RequestParam(defaultValue = "dueDate") String sortBy,
							  @RequestParam(defaultValue = "asc") String order,
							  Model model) {
		
//		Account loginUser = userDetails.getAccount();
//		
//		// Specification組み合わせ
//		Specification<Task> spec = Specification.where(TaskSpecifications.userIdEquals(loginUser.getUserId()))
//												.and(TaskSpecifications.titleContains(title))
//												.and(TaskSpecifications.statusEquals(status))
//				                                .and(TaskSpecifications.categoryEquals(category))
//				                                .and(TaskSpecifications.priorityEquals(priority));
//		
//		// ソートの設定
//		Sort sort = order.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
//		
//		// 実行して格納
//		List<Task> tasks = taskRepository.findAll(spec, sort);
		
		// 情報をサービスに渡すために格納
		Task info = new Task();
		info.setTitle(title);
		info.setStatus(status);
		info.setCategory(category);
		info.setPriority(priority);
		List<Task> filterTasks = taskService.searchAndSortTasks(info, sortBy, order);
		
		model.addAttribute("tasks", filterTasks);
		model.addAttribute("title", title);
		model.addAttribute("status", status);
		model.addAttribute("category", category);
		model.addAttribute("priority", priority);
		model.addAttribute("sort", sortBy);
		model.addAttribute("order", order);
		
		return "tasks/tasklist";
				                                
	}
	
	// タスクを決められた条件でソート
	// 複雑なソートは難しいのでとりあえず簡単なものを作成
//	@GetMapping("/tasklist/sort")
//	public String getTasks(@RequestParam(defaultValue = "id") String sortBy, Model model) {
//		List<Task> sortTasks = taskRepository.findAll(Sort.by(sortBy).ascending());
//		model.addAttribute("tasks", sortTasks);
//		return "tasks/tasklist";
//	}
	
	// 検索したりソートされたものを初期化したいとき
	@GetMapping("/refresh")
	public String refreshTasks(Model model) {
		List<Task> taskList = taskService.showTaskList();
		model.addAttribute("tasks", taskList);
		return "redirect:/tasks/tasklist";
	}
}

	
