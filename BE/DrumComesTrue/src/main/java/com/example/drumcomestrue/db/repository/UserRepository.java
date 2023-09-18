package com.example.drumcomestrue.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.drumcomestrue.db.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	boolean existsByUserId(String userId);
}
