package com.example.workmate.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.workmate.domain.Account;
import com.example.workmate.domain.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
	List<Task> findByUser(Account account);

}
