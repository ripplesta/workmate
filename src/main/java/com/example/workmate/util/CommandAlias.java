package com.example.workmate.util;

import java.util.Map;

public class CommandAlias {
//	private static final Map<String, String> ACTION_ALIASES = Map.of(
//			"list", "list",
//			"一覧", "list",
//			"show", "list",
//			"add", "add",
//			"追加", "add",
//			"登録", "add"		
//			
//			);
//	
//	private static final Map<String, String> TITLE_ALIASES = Map.of(
//			"title", "title",
//			"タイトル", "title",
//			"題名", "title",
//			"タスク名", "title"
//			
//			);
//	
//	private static final Map<String, String> DUEDATE_ALIASES = Map.of(
//			"dueDate", "dueDate",
//			"due", "dueDate",
//			"duedate", "dueDate",
//			"期限", "dueDate",
//			"期限日", "dueDate",
//			"期日", "dueDate",
//			"締切", "dueDate",
//			"締め切り", "dueDate",
//			"deadline", "dueDate",
//			"limit", "dueDate"
//			);
//	
//	private static final Map<String, String> STATUS_ALIASES = Map.of(
//			"status", "status",
//			"進捗", "status",
//			"作業", "status",
//			"完了", "done",
//			"済み", "done",
//			"未完了", "todo"
//			
//			);
//	
//	private static final Map<String, String> PRIORITY_ALIASES = Map.of(
//			"priority", "priority",
//			"優先度", "priority",
//			"順番", "proiority",
//			"高", "high",
//			"中", "middle",
//			"低", "low"
//			);
//	
//	private static final Map<String, String> CATEGORY_ALIASES = Map.of(
//			"category", "category",
//			"カテゴリ", "category",
//			"分類", "category",
//			"ジャンル", "category"
//			);
//	
//	private static final Map<String, String> SORT_ALIASES = Map.of(
//			"sort", "sort",
//			"ソート", "sort",
//			"order", "order",
//			"整列", "order",
//			"順番", "order",
//			"昇順", "asc",
//			"降順", "desc"
//			);
	
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
			Map.entry("登録", "add")		
	
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
		System.out.println(key);
		return FIELD_ALIASES.getOrDefault(key, key);
	}
	
//	public static String normalizeDueDate(String input) {
//		String key = normalizeKey(input);
//		return DUEDATE_ALIASES.getOrDefault(key, key);
//	}
	
	public static String normalizeStatus(String input) {
		String key = normalizeKey(input);
		return STATUS_VALUE_ALIASES.getOrDefault(key, key);
	}
	
	public static String normalizePriority(String input) {
		String key = normalizeKey(input);
		return PRIORITY_VALUE_ALIASES.getOrDefault(key, key);
	}
	
//	public static String normalizeCategory(String input) {
//		String key = normalizeKey(input);
//		return CATEGORY_ALIASES.getOrDefault(key, key);
//	}
	
	public static String normalizeSortOption(String input) {
		String key = normalizeKey(input);
		return SORT_VALUE_ALIASES.getOrDefault(key, key);
	}

}
