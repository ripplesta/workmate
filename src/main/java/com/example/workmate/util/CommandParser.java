package com.example.workmate.util;

import java.util.HashMap;
import java.util.Map;

import com.example.workmate.dto.Command;

public class CommandParser {
	
	public Command parse(String userInput) {
		String[] parts = userInput.split(" /"); // 空白と/で分割処理
		String action = parts[0].substring(1); // 例えば/addならaddを抽出
		
		Map<String, String> options = new HashMap<>();
		//options.put("title", parts[0].substring(parts[0].indexOf(" ") + 1));
		
		// " /"で分割して" "でさらに2つに分けてセットでMapに登録
		//(例: /進捗 完了ならkeyが進捗 valueが完了になる)
		for(int i = 1; i < parts.length; i++) {
			String[] value = parts[i].split(" ", 2);
			if(value.length == 2) {
				String normalizedKey = CommandAlias.normalizeAction(value[0]);
				normalizedKey = CommandAlias.normalizeTitle(value[0]);
				normalizedKey = CommandAlias.normalizeDueDate(value[0]);
				normalizedKey = CommandAlias.normalizeStatus(value[0]);
				normalizedKey = CommandAlias.normalizePriority(value[0]);
				normalizedKey = CommandAlias.normalizeCategory(value[0]);
				options.put(normalizedKey, value[1]);
			}
		}
		
		return new Command(action, options);
	}
	
}
