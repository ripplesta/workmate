package com.example.workmate.spec;

import org.springframework.data.jpa.domain.Specification;

import com.example.workmate.domain.Task;

public class TaskSpecifications {
	
	public static Specification<Task> userIdEquals(Long userId) {
		return (root, query, cb) -> {
			if (userId == null) return null;
			return cb.equal(root.get("user").get("userId"), userId);
		};
	}
	
	public static Specification<Task> titleContains(String title) {
		return (root, query, cb) ->
			(title == null || title.isEmpty()) ? null :
			cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
			
	}
	
	public static Specification<Task> statusEquals(String status) {
		return (root, query, cb) ->
		(status == null || status.isEmpty()) ? null :
		cb.equal(root.get("status"), status);
	}
	
	public static Specification<Task> categoryEquals(String category) {
		return (root, query, cb) ->
		(category == null || category.isEmpty()) ? null :
		cb.equal(root.get("category"), category);
	}
	
	public static Specification<Task> priorityEquals(String priority) {
		return (root, query, cb) ->
		(priority == null || priority.isEmpty()) ? null :
		cb.equal(root.get("priority"), priority);
	}

}
