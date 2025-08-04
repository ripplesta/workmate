package com.example.workmate.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.example.workmate.domain.Task;

public class TaskForm {
	
	private Long id;
	@NotBlank
	private String title;
	
	@Size(max = 255)
	private String description;
	
	@NotNull
	private LocalDate dueDate;

	private String status;
	private String priority;
	private String category;
	
	//デフォルトコンストラクタ
	public TaskForm() {
		
	}
	
	//引数ありのコンストラクタ
	//TaskFormにデータを渡すために必要
	public TaskForm(Task task) {
		this.id = task.getId();
		this.title = task.getTitle();
		this.description = task.getDescription();
		this.dueDate = task.getDueDate();
		this.status = task.getStatus();
		this.priority = task.getPriority();
		this.category = task.getCategory();

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

}
