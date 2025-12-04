package com.example.workmate.util;

import java.util.Map;

public class CommandAlias {
	
	private static final Map<String, String> FIELD_ALIASES = Map.ofEntries(
			Map.entry("title", "title"),
			Map.entry("タイトル", "title"),
			Map.entry("題名", "title"),
			Map.entry("タスク名", "title"),
			
			Map.entry("id", "id"),
			Map.entry("番号", "id"),
			Map.entry("No", "id"),
			
			Map.entry("description", "description"),
			Map.entry("説明", "description"),
			Map.entry("内容", "description"),
			
			Map.entry("dueDate", "dueDate"),
			Map.entry("due", "dueDate"),
			Map.entry("duedate", "dueDate"),
			Map.entry("期限", "dueDate"),
			Map.entry("期限日", "dueDate"),
			Map.entry("期日", "dueDate"),
			Map.entry("締切", "dueDate"),
			Map.entry("締め切り", "dueDate"),
			Map.entry("deadline", "dueDate"),
			Map.entry("limit", "dueDate"),
			
			Map.entry("status", "status"),
			Map.entry("状態", "status"),
			Map.entry("進捗", "status"),
			Map.entry("作業", "status"),
			
			Map.entry("priority", "priority"),
			Map.entry("優先度", "priority"),
			
			Map.entry("category", "category"),
			Map.entry("カテゴリ", "category"),
			Map.entry("分類", "category"),
			Map.entry("ジャンル", "category"),
			
			Map.entry("sort", "sort"),
			Map.entry("ソート", "sort"),
			
			Map.entry("order", "order"),
			Map.entry("整列", "order"),
			Map.entry("順番", "order")
			
			);
	
	private static final Map<String, String> ACTION_ALIASES = Map.ofEntries(
			Map.entry("list", "list"),
			Map.entry("リスト", "list"),
			Map.entry("一覧", "list"),
			Map.entry("show", "list"),
			
			Map.entry("add", "add"),
			Map.entry("追加", "add"),
			Map.entry("登録", "add"),
			
			Map.entry("update", "update"),
			Map.entry("更新", "update"),
			Map.entry("上書き", "update"),
			Map.entry("save", "update"),
			Map.entry("保存", "update"),

			Map.entry("done", "done"),
			Map.entry("完了", "done"),
			Map.entry("doing", "doing"),
			Map.entry("進行中", "doing"),
			Map.entry("todo", "todo"),
			Map.entry("未着手", "todo"),
			
			Map.entry("stats", "stats"),
			Map.entry("統計", "stats"),
			Map.entry("データ", "stats")
	
			);
	
	private static final Map<String, String> STATUS_VALUE_ALIASES = Map.of(
		    "完了", "done",
		    "済み", "done",
		    "未完了", "todo",
		    "未", "todo"
			);
	
	private static final Map<String, String> PRIORITY_VALUE_ALIASES = Map.of(
			"高", "high",
			"中", "middle",
			"低", "low"
			);
	
	private static final Map<String, String> SORT_VALUE_ALIASES = Map.of(
			"昇順", "asc",
			"降順", "desc"
			);
	
	private static final Map<String, String> TIMERANGE_ALIASES = Map.of(
			"today", "today",
			"今日", "today",
			"tomorrow", "tomorrow",
			"明日", "tomorrow",
			"week", "week",
			"週間", "week",
			"週", "week"
			);
	
	public static String normalizeKey(String key) {
		if(key == null) {
			return "";
		}
		return key.trim().toLowerCase();
	}
	
	public static String normalizeAction(String input) {
		String key = normalizeKey(input);
		return ACTION_ALIASES.getOrDefault(key, key);
	}
	
	public static String normalizeField(String input) {
		String key = normalizeKey(input);
		return FIELD_ALIASES.getOrDefault(key, key);
	}
	
	public static String normalizeStatus(String input) {
		String key = normalizeKey(input);
		return STATUS_VALUE_ALIASES.getOrDefault(key, key);
	}
	
	public static String normalizePriority(String input) {
		String key = normalizeKey(input);
		return PRIORITY_VALUE_ALIASES.getOrDefault(key, key);
	}
	
	public static String normalizeSortOption(String input) {
		String key = normalizeKey(input);
		return SORT_VALUE_ALIASES.getOrDefault(key, key);
	}
	
	public static String normalizeTimeRange(String input) {
		String key = normalizeKey(input);
		return TIMERANGE_ALIASES.getOrDefault(key, key);
	}

}
