package com.example.workmate.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.workmate.domain.Account;
import com.example.workmate.domain.Task;
import com.example.workmate.dto.Command;
import com.example.workmate.dto.TaskSearchForm;
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
	public List<Task> searchAndSortTasks(TaskSearchForm form, String sortBy, String order) {
		Account loginUser = getLoginUser();
		
		// 送られてきたデータでSpecification組み合わせ
//		Specification<Task> spec = Specification.where(TaskSpecifications.userIdEquals(loginUser.getUserId()))
//												.and(TaskSpecifications.titleContains(task.getTitle()))
//												.and(TaskSpecifications.dueDateEquals(task.getDueDate()))
//												.and(TaskSpecifications.statusEquals(task.getStatus()))
//												.and(TaskSpecifications.categoryEquals(task.getCategory()))
//												.and(TaskSpecifications.priorityEquals(task.getPriority()));
		
		Specification<Task> spec = Specification.allOf();
		spec = spec.and(TaskSpecifications.userIdEquals(loginUser.getUserId()));
		spec = spec.and(TaskSpecifications.titleContains(form.getTitle()));
		spec = spec.and(TaskSpecifications.dueDateEquals(form.getDueDate()));
		spec = spec.and(TaskSpecifications.dueMonthEquals(form.getYearMonth()));
		spec = spec.and(TaskSpecifications.statusEquals(form.getStatus()));
		spec = spec.and(TaskSpecifications.categoryEquals(form.getCategory()));
		spec = spec.and(TaskSpecifications.priorityEquals(form.getPriority()));
		spec = spec.and(TaskSpecifications.dueDateBetween(form.getStartDate(), form.getEndDate()));
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
			TaskSearchForm filterTask = new TaskSearchForm();
			filterTask.setTitle(command.getOptions("title"));
			String strDue = command.getOptions("dueDate");
			
			if(strDue != null && !strDue.isEmpty()) {
				// 指定日
				if(strDue.matches("\\d{4}-\\d{2}-\\d{2}")) {
					LocalDate date = LocalDate.parse(strDue);
					filterTask.setDueDate(date);
				}
				// 月指定
				else if(strDue.matches("\\d{4}-\\d{2}")) {
					YearMonth ym = YearMonth.parse(strDue);
					filterTask.setYearMonth(ym);
				}
			}
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
	}
	
	public String commandHelpAction(Command command) {
		String target = command.getOptions().get("arg");
		
		//確認用
		System.out.println("DEBUG target = '" + target + "'"); 

	    //target = target.trim().toLowerCase();
		
		if(target == null) {
			// 全体ヘルプ
			return"""
				利用可能なコマンド一覧：
				/add /登録 など タスクを追加
				/list /リスト リストの一覧を表示
				/update /更新 タスクを更新
				/done, /完了 /番号 <番号>
				/doing, /進行中 /番号 <id番号>
				/todo, /未完了 /番号 <id番号>  
				/help /<コマンド名> そのコマンドの詳細表示
				/stats 全体の進捗を確認
				英語表記、日本語表記対応
				空白は半角スペースでお願いします
				""";
		}
		// コマンド別の詳細表示
		switch(target) {
		case"add":
			return"""
				/add /タイトル <タスク名> /内容 <内容> 
				/期限 <YYYY-MM-DD> /進捗 <未着手or進行中or完了> 
				/優先度 <高or中or低> /カテゴリ <仕事, 生活など>
				空白は半角スペース <>は必要なし
				全て書かなくても可
					""";
		case"list":
			return"""
				/list 全てのリストを表示
				/list /タイトル <ワード> ワードで絞り込み検索ができます
				絞り込みやソートが自由に組み合わせ可能
				例： /list /タイトル <タスク名> /期限 <YYYY-MM-DD> 
				/進捗 <進行中> /優先度 <高> /カテゴリ <仕事> /ソート <期限or登録> /順番 <昇順>
				空白は半角スペース <>は必要なし
					""";
		case"update":
			return"""
				/update /id <番号> /変更したい部分 <更新内容> 
				→指定されたタスクを入力された内容で更新します
				例： /update /番号 1 /タイトル <タスク名> /優先度 中 /カテゴリ 生活 など
				複数更新も可
				空白は半角スペース <>は必要なし
					""";
			default:
				return "指定されたコマンドのヘルプは存在しません：" + target;
		}
	}

	public String commandStatsAction(Command command) {
		Account loginUser = getLoginUser();
		
		List<Task> tasks = taskRepository.findByUser(loginUser);
		// 全体の進捗集計
		int statusTodo = 0;
		int statusDoing = 0;
		int statusDone = 0;
		for(Task task : tasks) {
			if(task.getStatus().equals("未着手")) {
				statusTodo++;
			}
			else if(task.getStatus().equals("進行中")) {
				statusDoing++;
			}
			else if(task.getStatus().equals("完了")) {
				statusDone++;
			}
		}
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("""
			[全体の進捗状況]
			未着手:%d件
			進行中:%d件
			完了:%d件
			""", statusTodo, statusDoing, statusDone));
		
		// カテゴリ別集計
		Map<String, Map<String, Integer>> categoryStats = new HashMap<>();
		for(Task task : tasks) {
			String category = (task.getCategory() != null) ? task.getCategory() : "未分類";
			String status = (task.getStatus() != null) ? task.getStatus() : "未着手";
			
			Map<String, Integer> statusCounts = categoryStats.get(category);
			if(statusCounts == null) {
				statusCounts = new HashMap<>();
				categoryStats.put(category, statusCounts);
			}
			statusCounts.put(status, statusCounts.getOrDefault(status, 0) + 1);
		}
		List<String> categoryOrder = List.of("仕事", "趣味", "勉強", "生活", "その他");
		sb.append("[カテゴリ別]\n");
		for(String category : categoryOrder) {
			Map<String, Integer> statusResult = categoryStats.getOrDefault(category, new HashMap<>());
			int total = 0;
		    for(int value : statusResult.values()) {
		    	total += value;
		    }
		    
			sb.append(String.format("%s: %d件(未着手 %d, 進行中 %d, 完了 %d)\n",
					category,
					total,
					statusResult.getOrDefault("未着手", 0),
					statusResult.getOrDefault("進行中", 0),
					statusResult.getOrDefault("完了", 0)
					));
		}
		
		return sb.toString();
	}
	
}


