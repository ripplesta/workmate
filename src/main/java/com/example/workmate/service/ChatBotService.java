package com.example.workmate.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.example.workmate.domain.Account;
import com.example.workmate.domain.BotResponse;
import com.example.workmate.domain.ChatMessage;
import com.example.workmate.repository.BotResponseRepository;
import com.example.workmate.repository.ChatMessageRepository;

@Service
public class ChatBotService {
	
	private final ChatMessageRepository chatMessageRepository;
	
	private final BotResponseRepository botResponseRepository;
	
//	private final Map<String, String> chitChatResponses = Map.ofEntries(
//			Map.entry("おはよう", "おはようございます！今日も一緒に頑張りましょう"),
//			Map.entry("こんにちは", "こんにちは！今日はどんな一日になりそうですか？"),
//			Map.entry("おやすみ", "おやすみなさい　明日も頑張りましょう"),
//			Map.entry("疲れた", "お疲れ様です。ちょっと休憩して、深呼吸しましょう"),
//			Map.entry("やる気がない", "無理せず、小さなことから始めてみませんか？"),
//			Map.entry("天気がいい", "そうですね！こんな日に気分も上がりますね！"),
//			Map.entry("雨", "雨の日は室内作業に集中するチャンスですよ"),
//			Map.entry("暑い", "水分補給を忘れずに！冷たい飲み物でもどうぞ"),
//			Map.entry("寒い", "温かい飲み物や厚着をして体を温めましょう"),
//			Map.entry("お腹すいた", "作業の合間にちょっと軽食をどうぞ")
//	);
	
	public ChatBotService(ChatMessageRepository chatRepository, BotResponseRepository botResRepository) {
		this.chatMessageRepository = chatRepository;
		this.botResponseRepository = botResRepository;
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
		// キーワードがユーザー入力を含む場合
		List<BotResponse> matchByKeyword = botResponseRepository.findByKeywordContainingIgnoreCase(userInput);
		// ユーザー入力がキーワードを含む場合
		List<BotResponse> matchByInput = botResponseRepository.findByInputMatchesKeyword(userInput);
		
		// 検索結果を合わせる(重複は除去)
		List<BotResponse> generateReply = new ArrayList<>();
		generateReply.addAll(matchByInput);
		for(BotResponse res : matchByKeyword) {
			if(!generateReply.contains(res)) {
				generateReply.add(res);
			}
		}
		
		//キーワードで出た定型文の優先度の合計
		int totalWeight = generateReply.stream()
				.mapToInt(BotResponse::getPriority)
				.sum();
		
		//0～合計値‐1からランダムに数字を出す
		int randomValue = new Random().nextInt(totalWeight);
		
		//優先度を一つずつ出して合計しランダムで出した数字を超えたらその定型文を返す
		int current = 0;
		for(BotResponse res : generateReply) {
			current += res.getPriority();
			if(randomValue < current) {
				return res.getTemplateText();
			}
		}
		
		if(generateReply.isEmpty()) {
			return "なるほど！その件についてもう少し教えてください";
		}
		
		//デフォルト応答(理論上使わない)
		return generateReply.get(0).getTemplateText();
	}

}
