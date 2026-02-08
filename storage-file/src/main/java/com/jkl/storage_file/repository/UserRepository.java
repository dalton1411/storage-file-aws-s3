package com.jkl.storage_file.repository;

import com.jkl.storage_file.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
