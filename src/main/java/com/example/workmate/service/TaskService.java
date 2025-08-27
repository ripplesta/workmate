package com.example.workmate.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.workmate.domain.Account;
import com.example.workmate.domain.Task;
import com.example.workmate.dto.Command;
import com.example.workmate.repository.TaskRepository;
import com.example.workmate.security.AccountUserDetails;
import com.example.workmate.spec.TaskSpecifications;
import com.example.workmate.util.CommandParser;


@Service
public class TaskService {
	
	private final TaskRepository taskRepository;
	
	public TaskService(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}
	// セッション情報の取得をメソッド化
	private Account getLoginUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		AccountUserDetails userDetails = (AccountUserDetails) auth.getPrincipal();
		return userDetails.getAccount();
	}
	
	// タスクの一覧
	public List<Task> showTaskList() {
		// セッション情報を取得
		Account loginUser = getLoginUser();
		List<Task> taskList = taskRepository.findByUser(loginUser);
		
		return taskList;
		
	}
	
	// タスクの登録
	public void createTask(Task task) {
		Account loginUser = getLoginUser();
		
		// userIdは現在のログインユーザーから取得
		task.setUser(loginUser);
		// DBに保存
		taskRepository.save(task);
		
	}
	// タスクの編集
	public void updateTask(Task task) {
		Account loginUser = getLoginUser();
		//　編集するときタスクがログインユーザーのタスクかどうかチェック
		Task updateTask = taskRepository.findById(task.getId())
			.orElseThrow(() -> new IllegalArgumentException("タスクが見つかりませんでした"));
		
		// 違うなら編集させないようにする
		if(!updateTask.getUser().equals(loginUser)) {
			throw new SecurityException("他人のタスクは編集できません");
		}
		task.setUser(loginUser);
		taskRepository.save(task);
	}
	
	// タスクの削除
	public void deleteTask(Long id) {
		Task task = taskRepository.findById(id).orElseThrow();
		taskRepository.delete(task);
	}
	
	// ソート＋検索
	public List<Task> searchAndSortTasks(Task task, String sortBy, String order) {
		Account loginUser = getLoginUser();
		
		// 送られてきたデータでSpecification組み合わせ
		Specification<Task> spec = Specification.where(TaskSpecifications.userIdEquals(loginUser.getUserId()))
												.and(TaskSpecifications.titleContains(task.getTitle()))
												.and(TaskSpecifications.statusEquals(task.getStatus()))
												.and(TaskSpecifications.categoryEquals(task.getCategory()))
												.and(TaskSpecifications.priorityEquals(task.getPriority()));
		// ソートの設定
		Sort sort = order.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
		
		// 実行
		List<Task> tasks = taskRepository.findAll(spec, sort);
		
		return tasks;
	}
	
	public void comandAction(String userInput) {
		CommandParser parser = new CommandParser();
		Command command = parser.parse(userInput);
		
		if(command.getAction().equals("add")) {
			Task task = new Task();
			task.setTitle(command.getOptions("title"));
			task.setDescription(command.getOptions("説明"));
			
			String strDue = command.getOptions("期限");
			if(strDue != null) {
				LocalDate dueDate = LocalDate.parse(strDue);
				task.setDueDate(dueDate);
			}
			task.setStatus(command.getOptions("進捗"));
			
	}

}
