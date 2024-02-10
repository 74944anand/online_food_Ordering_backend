package com.foodapp.repository;


import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.foodapp.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.profilePhoto = ?1 WHERE u.id = ?2")
    void updateProfilePhoto(byte[] profilePhoto, Long userId);

}