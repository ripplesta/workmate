package com.example.workmate.controller;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
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
import com.example.workmate.dto.TaskSearchForm;
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
		
		taskService.saveTask(task);
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
							  @RequestParam(required = false) String dueDate,
							  @RequestParam(required = false) String status,
							  @RequestParam(required = false) String category,
							  @RequestParam(required = false) String priority,
							  @RequestParam(defaultValue = "dueDate") String sortBy,
							  @RequestParam(defaultValue = "asc") String order,
							  @RequestParam(required = false) LocalDate startDate,
							  @RequestParam(required = false) LocalDate endDate,
							  Model model) {
				

		// 情報をサービスに渡すために格納
		TaskSearchForm info = new TaskSearchForm();
		info.setTitle(title);
		info.setStatus(status);
		info.setCategory(category);
		info.setPriority(priority);
		
		// 期限日検索の形によって分岐
		if(dueDate != null && !dueDate.isEmpty()) {
			// 指定日
			if(dueDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
				LocalDate date = LocalDate.parse(dueDate);
				info.setDueDate(date);
			}
			// 月指定
			else if(dueDate.matches("\\d{4}-\\d{2}")) {
				YearMonth ym = YearMonth.parse(dueDate);
				info.setYearMonth(ym);
			}
		}
		info.setStartDate(startDate);
		info.setEndDate(endDate);
		
		List<Task> filterTasks = taskService.searchAndSortTasks(info, sortBy, order);
		
		model.addAttribute("tasks", filterTasks);
		model.addAttribute("title", title);
		model.addAttribute("dueDate", dueDate);
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
	
	@GetMapping("/calendar")
	public String showCalendar(@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM")LocalDate month, 
			Model model) {
		LocalDate today = LocalDate.now();
		LocalDate firstDay = (month != null ? month.withDayOfMonth(1) : today.withDayOfMonth(1));
		LocalDate lastDay = firstDay.plusMonths(1).minusDays(1);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		AccountUserDetails userDetails = (AccountUserDetails) auth.getPrincipal();
		Account loginUser = userDetails.getAccount();
		
		// 1日から最終日までの日付リストを生成
		List<LocalDate> days = firstDay.datesUntil(lastDay.plusDays(1)).toList();
		
		// タスクを日付ごとにまとめる
		List<Task> tasks = taskRepository.findByUser(loginUser);
		Map<LocalDate, List<Task>> tasksByDate = tasks.stream()
				.filter(t -> t.getDueDate() != null)
				.collect(Collectors.groupingBy(Task::getDueDate));
		
		// 月の最初の週に空白を追加(前の月分)
		int dayOfWeekValue = firstDay.getDayOfWeek().getValue();
		int shift = dayOfWeekValue % 7;
		List<LocalDate> paddedDays = new ArrayList<>();
		for(int i = 0; i < shift; i++) {
			paddedDays.add(null);
		}
		paddedDays.addAll(days);
		
		// 1週間もリストで用意する
		List<List<LocalDate>> weeks = new ArrayList<>();
		for(int i = 0; i < paddedDays.size(); i += 7) {
			weeks.add(paddedDays.subList(i, Math.min(i + 7, paddedDays.size())));
		}
		
		// 進捗が完了済みや日付などでタスクの状況を判定するidのセットを用意
		Set<Long> overdueTaskIds = new HashSet<>();
		for(Task t : tasks) {
			if(t.getDueDate() != null && t.getDueDate().isBefore(today) && t.getStatus().equals("完了")) {
				overdueTaskIds.add(t.getId());
			}
		}
		
		Set<Long> completeTaskIds = new HashSet<>();
		for(Task t : tasks) {
			if(t.getStatus().equals("完了")) {
				completeTaskIds.add(t.getId());
			}
		}
		
		Set<Long> dueTodayTaskIds = new HashSet<>();
		for(Task t : tasks) {
			if(t.getDueDate() != null && t.getDueDate().isEqual(today)) {
				dueTodayTaskIds.add(t.getId());
			}
		}
	
		model.addAttribute("today", today);
		model.addAttribute("days", paddedDays);
		model.addAttribute("tasksByDate", tasksByDate);
		model.addAttribute("month", firstDay);
		model.addAttribute("weeks", weeks);
		model.addAttribute("overdueTaskIds", overdueTaskIds);
		model.addAttribute("completeTaskIds", completeTaskIds);
		model.addAttribute("dueTodayTaskIds", dueTodayTaskIds);
		
		return "tasks/calendar";
		}
}

	
