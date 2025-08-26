package com.example.workmate.dto;

import java.util.Map;

public class Command {
	private String action;
	private Map<String, String> options;
	
	public Command(String action, Map<String, String> options) {
		this.action = action;
		this.options = options;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Map<String, String> getOptions() {
		return options;
	}

	public void setOptions(Map<String, String> options) {
		this.options = options;
	}
	
	public String getOptions(String key) {
		return options.get(key);
	}

}
