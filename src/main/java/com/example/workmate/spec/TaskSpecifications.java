package com.example.workmate.spec;

import java.time.LocalDate;
import java.time.YearMonth;

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
	
	public static Specification<Task> dueDateEquals(LocalDate date) {
		return (root, query, cb) ->
			(date == null) ? null :
			cb.equal(root.get("dueDate"), date);
	}
	
	public static Specification<Task> dueMonthEquals(YearMonth ym){
		return (root, query, cb) ->{
			if(ym == null) return null;
			LocalDate start = ym.atDay(1);
			LocalDate end = ym.atEndOfMonth();
			return cb.between(root.get("dueDate"), start, end);
		};
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
	
	public static Specification<Task> dueDateBetween(LocalDate start, LocalDate end) {
		return (root, query, cb) -> {
			if(start == null && end == null) return null;
			if(start != null && end != null) {
				return cb.between(root.get("dueDate"), start, end);
			}
			else if(start != null) {
				return cb.greaterThanOrEqualTo(root.get("dueDate"), start);
			}
			else {
				return cb.lessThanOrEqualTo(root.get("dueDate"), end);
			}
		};
	}

}
