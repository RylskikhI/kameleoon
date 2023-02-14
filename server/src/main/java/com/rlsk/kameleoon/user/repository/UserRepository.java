package com.rlsk.kameleoon.user.repository;

import com.rlsk.kameleoon.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
