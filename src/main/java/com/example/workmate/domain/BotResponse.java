package com.example.workmate.domain;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "bot_responses")
public class BotResponse {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String keyword;
	private String templateText;
	private int priority;
	
	@Enumerated(EnumType.STRING)
	private TimeRange timeRange;
	
	public enum TimeRange {
		MORNING,
		AFTERNOON,
		NIGHT,
		ANY
	}
	
	@ManyToMany
	@JoinTable(
			name = "bot_response_tag",
			joinColumns = @JoinColumn(name = "bot_response_id"),
			inverseJoinColumns = @JoinColumn(name = "tag_id")
			)
	private Set<Tag> tags = new HashSet<>();
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getKeyword() {
		return keyword;
	}
	
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	
	public String getTemplateText() {
		return templateText;
	}
	
	public void setTemplateText(String templateText) {
		this.templateText = templateText;
	}
	
	public int getPriority() {
		return priority;
	}
	
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	public TimeRange getTimeRange() {
		return timeRange;
	}
	
	public void setTimeRange(TimeRange timeRange) {
		this.timeRange = timeRange;
	}

	public Set<Tag> getTags() {
		return tags;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}

}
