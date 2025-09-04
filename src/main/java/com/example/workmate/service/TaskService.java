package com.example.workmate.service;

import java.time.LocalDate;
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
import com.example.workmate.util.CommandAlias;


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
	public void saveTask(Task task) {
		Account loginUser = getLoginUser();
		Task updateTask = taskRepository.findById(task.getId())
			.orElseThrow(() -> new IllegalArgumentException("タスクが見つかりませんでした"));
		
		// 確認用
		System.out.println("DEBUG loginUser id=" + loginUser.getUserId());
		System.out.println("DEBUG updateTask user id=" + updateTask.getUser().getUserId());
		System.out.println("DEBUG loginUser equals updateTask.getUser()? " + loginUser.equals(updateTask.getUser()));
		
		//　編集するときタスクがログインユーザーのタスクかどうかチェック
		// 違うなら編集させないようにする
		if(!updateTask.getUser().getUserId().equals(loginUser.getUserId())) {
			throw new SecurityException("他人のタスクは編集できません");
		}
		// 渡されたデータでそれぞれ上書き
		if(task.getTitle() != null) updateTask.setTitle(task.getTitle());
		if(task.getDescription() != null) updateTask.setDescription(task.getDescription());
		if(task.getDueDate() != null) updateTask.setDueDate(task.getDueDate());
		if(task.getStatus() != null) updateTask.setStatus(task.getStatus());
		if(task.getPriority() != null) updateTask.setPriority(task.getPriority());
		if(task.getCategory() != null) updateTask.setCategory(task.getCategory());
		
		
		updateTask.setUser(loginUser);
		taskRepository.save(updateTask);
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
	
	// チャットコマンドのタスク登録処理
	public String commandCreateAction(Command command) {
		Task task = new Task();
		
		task.setTitle(command.getOptions("title"));
		task.setDescription(command.getOptions("description"));
			
		String strDue = command.getOptions("dueDate");
			//LocalDate型なので型変換が必要
			if(strDue != null) {
			LocalDate dueDate = LocalDate.parse(strDue);
			task.setDueDate(dueDate);
		}
		task.setStatus(command.getOptions("status"));
		task.setPriority(command.getOptions("priority"));
		task.setCategory(command.getOptions("category"));
			
		createTask(task);
		return "タスク登録が完了しました";
			 
	}

	public List<Task> commandListAction(Command command) {
		if(!command.getOptions().isEmpty()) {
			Task filterTask = new Task();
			filterTask.setTitle(command.getOptions("title"));
			String rowStatus = command.getOptions("status");
			filterTask.setStatus(CommandAlias.normalizeStatus(rowStatus));
			// filterTask.setStatus(command.getOptions("status"));
			filterTask.setPriority(command.getOptions("priority"));
			filterTask.setCategory(command.getOptions("category"));
			
			String sortBy = command.getOptions("sort") != null ? command.getOptions("sort") : "dueDate"; 
			String order = command.getOptions("order") != null ? command.getOptions("order") : "asc";
		
			List<Task> searchTasks = searchAndSortTasks(filterTask, sortBy, order);
			System.out.println("確認01：" + command.getOptions("進捗"));

			return searchTasks;
		}
		else {
			List<Task> userTasks = showTaskList();
			return userTasks;
		}
		
	}
		
	public String formatResponse(List<Task> tasks) {
		StringBuilder sb = new StringBuilder();
				
		for(Task resTask : tasks) {
			sb.append("タスク番号：")
			  .append(resTask.getId())
			  .append(" タスク名：")
			  .append(resTask.getTitle())
			  .append(" 期限：")
			  .append(resTask.getDueDate())
			  .append(" 進捗：")
			  .append(resTask.getStatus())
			  .append(" 優先度：")
			  .append(resTask.getPriority())
			  .append(" カテゴリ：")
			  .append(resTask.getCategory())
			  .append("\n");
		}
		
		return sb.toString();
		
	}
	
	public String commandUpdateAction(Command command) {
		Task newTaskData = new Task();
		Long num = Long.parseLong(command.getOptions("id"));
		newTaskData.setId(num);
		newTaskData.setTitle(command.getOptions("title"));
		newTaskData.setDescription(command.getOptions("description"));
		String strDue = command.getOptions("dueDate");
			//LocalDate型なので型変換が必要
			if(strDue != null) {
				LocalDate dueDate = LocalDate.parse(strDue);
				newTaskData.setDueDate(dueDate);
			}
		newTaskData.setStatus(command.getOptions("status"));
		newTaskData.setPriority(command.getOptions("priority"));
		newTaskData.setCategory(command.getOptions("category"));
		saveTask(newTaskData);
		
		return "タスクが更新できました";
	}

	public String commandDoneAction(Command command) {
		command.getOptions().put("status", "完了");
		return commandUpdateAction(command);
	}

	public String commandDoingAction(Command command) {
		command.getOptions().put("status", "進行中");
		return commandUpdateAction(command);
	}

	public String commandTodoAction(Command command) {
		command.getOptions().put("status", "未着手");
		return commandUpdateAction(command);
