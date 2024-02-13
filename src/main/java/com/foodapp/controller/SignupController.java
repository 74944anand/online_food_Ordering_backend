package com.foodapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
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

    @Transactional
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user, BindingResult bindingResult) {
        try {
            System.out.println(user);
            // Validate user input
            if (bindingResult.hasErrors()) {
                return ResponseEntity.badRequest().body("Invalid input data");
            }

            // Check if the email is already registered
            if (userService.loadUserByUsername(user.getEmail()) != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");
            }

            // Encrypt password before saving
            String encodedPassword = passwordEncoder.encode(user.getPassword()); // Encode password
            user.setPassword(encodedPassword);

            // Set role
            String role = user.getRole(); // Assuming the role is provided in the User object
            // Perform any necessary validation on the role before setting it

            // Save the user
            userService.saveUser(user);

            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            // Log error
            e.printStackTrace();
            // Return internal server error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register user");
        }
    }
}
