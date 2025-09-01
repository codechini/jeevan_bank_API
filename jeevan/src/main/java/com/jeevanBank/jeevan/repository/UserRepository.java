package com.jeevanBank.jeevan.repository;

import com.jeevanBank.jeevan.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {
}
