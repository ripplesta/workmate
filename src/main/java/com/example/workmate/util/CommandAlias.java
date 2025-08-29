package com.example.workmate.util;

import java.util.Map;

public class CommandAlias {
	private static final Map<String, String> ACTION_ALIASES = Map.of(
			"list", "list",
			"一覧", "list",
			"show", "list",
			"add", "add",
			"追加", "add",
			"登録", "add"		
			
			);
	
	private static final Map<String, String> TITLE_ALIASES = Map.of(
			"title", "title",
			"タイトル", "title",
			"題名", "title",
			"タスク名", "title"
			
			);
	
	private static final Map<String, String> DUEDATE_ALIASES = Map.of(
			"dueDate", "dueDate",
			"due", "dueDate",
			"duedate", "dueDate",
			"期限", "dueDate",
			"期限日", "dueDate",
			"期日", "dueDate",
			"締切", "dueDate",
			"締め切り", "dueDate",
			"deadline", "dueDate",
			"limit", "dueDate"
			);
	
	private static final Map<String, String> STATUS_ALIASES = Map.of(
			"status", "status",
			"進捗", "status",
			"作業", "status",
			"完了", "done",
			"済み", "done",
			"未完了", "todo"
			
			);
	
	private static final Map<String, String> PRIORITY_ALIASES = Map.of(
			"priority", "priority",
			"優先度", "priority",
			"順番", "proiority",
			"高", "high",
			"中", "middle",
			"低", "low"
			);
	
	private static final Map<String, String> CATEGORY_ALIASES = Map.of(
			"category", "category",
			"カテゴリ", "category",
			"分類", "category",
			"ジャンル", "category"
			);
	
	private static final Map<String, String> SORT_ALIASES = Map.of(
			"sort", "sort",
			"ソート", "sort",
			"order", "order",
			"整列", "order",
			"順番", "order",
			"昇順", "asc",
			"降順", "desc"
			);
	
	public static String normalizeAction(String input) {
		return ACTION_ALIASES.getOrDefault(input, input);
	}
	
	public static String normalizeTitle(String input) {
		return TITLE_ALIASES.getOrDefault(input, input);
	}
	
	public static String normalizeDueDate(String input) {
		return DUEDATE_ALIASES.getOrDefault(input, input);
	}
	
	public static String normalizeSTATUS(String input) {
		return STATUS_ALIASES.getOrDefault(input, input);
	}
	
	public static String normalizePRIORITY
	

}
