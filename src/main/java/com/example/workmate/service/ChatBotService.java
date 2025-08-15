package com.example.workmate.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.workmate.domain.Account;
import com.example.workmate.domain.ChatMessage;
import com.example.workmate.repository.ChatMessageRepository;

@Service
public class ChatBotService {
	
	private final ChatMessageRepository chatMessageRepository;
	
	private final Map<String, String> chitChatResponses = Map.ofEntries(
			Map.entry("おはよう", "おはようございます！今日も一緒に頑張りましょう"),
			Map.entry("こんにちは", "こんにちは！今日はどんな一日になりそうですか？"),
			Map.entry("おやすみ", "おやすみなさい　明日も頑張りましょう"),
			Map.entry("疲れた", "お疲れ様です。ちょっと休憩して、深呼吸しましょう"),
			Map.entry("やる気がない", "無理せず、小さなことから始めてみませんか？"),
			Map.entry("天気がいい", "そうですね！こんな日に気分も上がりますね！"),
			Map.entry("雨", "雨の日は室内作業に集中するチャンスですよ"),
			Map.entry("暑い", "水分補給を忘れずに！冷たい飲み物でもどうぞ"),
			Map.entry("寒い", "温かい飲み物や厚着をして体を温めましょう"),
			Map.entry("お腹すいた", "作業の合間にちょっと軽食をどうぞ")
	);
	
	public ChatBotService(ChatMessageRepository chatRepository) {
		this.chatMessageRepository = chatRepository;
	}
	
	public void handleUserMessage(Account user, String messageText) {
		//ユーザーメッセージ保存
		ChatMessage userMsg = new ChatMessage();
		userMsg.setUser(user);
		userMsg.setSenderType(ChatMessage.SenderType.USER);
		userMsg.setMessageText(messageText);
		chatMessageRepository.save(userMsg);
		
		//Botの応答の生成
		String botReply = generateReply(messageText);
		
		//Botメッセージ保存
		ChatMessage botMsg = new ChatMessage();
		botMsg.setUser(user);
		botMsg.setSenderType(ChatMessage.SenderType.BOT);
		botMsg.setMessageText(botReply);
		chatMessageRepository.save(botMsg);
	}
	
	private String generateReply(String userInput) {
		//雑談パターン判定
		for(Map.Entry<String, String>  entry : chitChatResponses.entrySet()) {
			if(userInput.contains(entry.getKey())) {
				return entry.getValue();
			}
		}
		
		//デフォルト応答(今は簡易)
		return "なるほど！その件についてもう少し教えてください";
	}

}
