package com.foodapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.foodapp.model.User;
import com.foodapp.service.UserService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class SignupController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder; // Inject PasswordEncoder

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user) {
        try {
        	System.out.println(user);
            // Check if the email is already registered
            if (userService.loadUserByUsername(user.getEmail()) != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");
            }
            
            System.out.println(user);
            // Encrypt password before saving
            String encodedPassword = passwordEncoder.encode(user.getPassword()); // Encode password
            user.setPassword(encodedPassword);
            System.out.println(encodedPassword);
            // Save the user
            System.out.println("before");
            userService.saveUser(user);
            System.out.println("after saving user");

            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
        	System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register user");
        }
    }
}
