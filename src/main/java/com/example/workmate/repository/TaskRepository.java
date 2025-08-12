package com.example.workmate.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.workmate.domain.Account;
import com.example.workmate.domain.Task;

public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
	List<Task> findByUser(Account account);
	// すでにJpaRepositoryで継承しているため明示的に書かなくてもよい
    // Optional<Task> findById(Long id);
	@Query("SELECT t FROM Task t WHERE " +
			"t.title LIKE %:keyword% OR " +
			"t.description LIKE %:keyword% OR " +
			"t.status LIKE %:keyword% OR " +
			"t.priority LIKE %:keyword% OR " +
			"t.category LIKE %:keyword%")
	List<Task> searchAllField(@Param("keyword")String keyword);
	

}
