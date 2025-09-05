package com.example.workmate.util;

import java.util.HashMap;
import java.util.Map;

import com.example.workmate.dto.Command;

public class CommandParser {
	
	public Command parse(String userInput) {
		String[] parts = userInput.split(" /"); // 空白と/で分割処理
		String action = parts[0].substring(1); // 例えば/addならaddを抽出
		
//		// 最初のアクションを抽出
//		String[] firstSplit = userInput.trim().split(" ", 2); // a bcdみたいに分ける 
//		String action = firstSplit[0].substring(1); // 例えば/addならaddを抽出
		
		Map<String, String> options = new HashMap<>();
		//options.put("title", parts[0].substring(parts[0].indexOf(" ") + 1));
		
		// " /"で分割して" "でさらに2つに分けてセットでMapに登録
		//(例: /進捗 完了ならkeyが進捗 valueが完了になる)
		for(int i = 1; i < parts.length; i++) {
			String[] value = parts[i].split(" ", 2);
			if(value.length == 2 && value[0] != null &&!value[0].isBlank()) {
				String nomalizedKey = CommandAlias.normalizeField(value[0].trim());
				options.put(nomalizedKey, value[1].trim());
			}
			else if(value.length != 2 && value[0] != null &&!value[0].isBlank()) {
				String nomalizedAction = CommandAlias.normalizeAction(value[0].trim());
				options.put("arg", nomalizedAction);
				
			}
			// 確認用
			System.out.println("DEBUG parts[" + i + "] = " + parts[i]);
			System.out.println("DEBUG value[0] = " + value[0]);
			System.out.println("DEBUG value[1] = " + (value.length > 1 ? value[1] : "なし"));
			System.out.println("normalizeKey(value[0]) = " + CommandAlias.normalizeField(value[0]));
		}
		
		
		return new Command(action, options);
	}
	
}
