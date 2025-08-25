package com.example.workmate.service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.workmate.domain.Account;
import com.example.workmate.domain.BotResponse;
import com.example.workmate.domain.BotResponse.TimeRange;
import com.example.workmate.domain.ChatMessage;
import com.example.workmate.domain.Tag;
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
	

	// メッセージのやりとりをDBに保存する
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
	
	// メッセージの時間帯によってフィルタリング
	public List<BotResponse> filterByTimeRange(List<BotResponse> responses) {
		// 現在時間の時間帯を判定
		LocalTime now = LocalTime.now();
		TimeRange currentTime = getTimeRange(now);
		
		// 時間帯が一致するかANYのものだけに絞る
		return responses.stream()
				.filter(r -> r.getTimeRange() == TimeRange.ANY || r.getTimeRange() == currentTime)
				.collect(Collectors.toList());
	}
	
	// 時間帯でフィルタを決める
	private TimeRange getTimeRange(LocalTime now) {
		if(now.isAfter(LocalTime.of(5, 0)) && now.isBefore(LocalTime.of(12, 0))) {
			return TimeRange.MORNING;
		}
		else if(now.isBefore(LocalTime.of(18, 0))) {
			return TimeRange.AFTERNOON;
		}
		else if(now.isBefore(LocalTime.of(24, 0))) {
			return TimeRange.NIGHT;
		}
		else {
			return TimeRange.ANY;
		}
	}
	
	private static final Map<String, String> keywordToTag = Map.ofEntries(
			Map.entry("おはよう", "挨拶"),
			Map.entry("こんにちは", "挨拶"),
			Map.entry("こんばんは", "挨拶"),
			Map.entry("進捗", "進捗"),
			Map.entry("タスク", "タスク管理"),
			Map.entry("締切", "締切"),
			Map.entry("優先度" , "優先度")
		);
	
	private List<String> detectTagsFromMessage(String userInput) {
		List<String> detectedTags = new ArrayList<>();
		for(Map.Entry<String, String> entry :keywordToTag.entrySet()) {
			if(userInput.contains(entry.getKey())) {
				detectedTags.add(entry.getValue());
			}
		}
		return detectedTags;
	}
	
	// タグに合うものを返答に使う
	private List<BotResponse> filterByTag(List<BotResponse> responses, List<String> tags) {
		List<BotResponse> filtered = new ArrayList<>();
		for(BotResponse res: responses) {
			for(Tag tag: res.getTags()) {
				//if(tag.getName().equalsIgnoreCase(tagName)) {
				if(tags.contains(tag.getName())) {
					filtered.add(res);
					break;
				}
			}
		}
		return filtered;
	}
	
	
	// 優先度で定型文をランダムに返す
	public String chooseByPriority(List<BotResponse> responses) {
		
		//キーワードで出た定型文の優先度の合計
		int totalWeight = responses.stream()
				.mapToInt(BotResponse::getPriority)
				.sum();
		
		//0～合計値‐1からランダムに数字を出す
		int randomValue = new Random().nextInt(totalWeight);
		
		//優先度を一つずつ出して合計しランダムで出した数字を超えたらその定型文を返す
		int current = 0;
		for(BotResponse res : responses) {
			current += res.getPriority();
			if(randomValue < current) {
				return res.getTemplateText();
			}
		}
		// デフォルト応答(理論上使わない)
		return responses.get(0).getTemplateText();
	}
	
//	private String taskManegement(String userInput) {
//		
//		// タスク管理コマンド判定
//		// あとでコントローラーにかいたCRUD処理をサービスに切り分けサービスを適用する
//		if(userInput.contains("タスク追加")) {
//			return taskService.addTask(userInput);
//		}
//		else if(userInput.contains("タスク一覧")) {
//			return taskService.listTasks();
//		}
//		else if(userInput.contains("タスク削除")) {
//			return taskService.deleteTask(userInput);
//		}
//		
//		return generateReply(userInput);
	
	private String generateReply(String userInput) {
		// タスク管理コマンド系でなければ雑談を返す
		// キーワードがユーザー入力を含む場合
		List<BotResponse> matchByKeyword = botResponseRepository.findByKeywordContainingIgnoreCase(userInput);
		// ユーザー入力がキーワードを含む場合
		List<BotResponse> matchByInput = botResponseRepository.findByInputMatchesKeyword(userInput);
		
		// 検索結果を合わせる(重複は除去)
		List<BotResponse> candidates = new ArrayList<>();
		candidates.addAll(matchByInput);
		for(BotResponse res : matchByKeyword) {
			if(!candidates.contains(res)) {
				candidates.add(res);
			}
		}
		// フィルタリングを通す
		candidates = filterByTimeRange(candidates);
		List<String> detectedTags = detectTagsFromMessage(userInput);
		candidates = filterByTag(candidates, detectedTags);
		
		if(candidates.isEmpty()) {
			return "なるほど！その件についてもう少し教えてください";
		}
		
		String generateReply = chooseByPriority(candidates);
		
		return generateReply;
	}
		

}
