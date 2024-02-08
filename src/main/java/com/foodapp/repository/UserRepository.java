package com.foodapp.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.foodapp.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

	void updateProfilePhoto(byte[] bytes, Long id);

}