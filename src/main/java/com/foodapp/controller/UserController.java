package com.foodapp.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.foodapp.dto.UpdateNameRequest;
import com.foodapp.dto.UpdatePasswordRequest;
import com.foodapp.model.User;
import com.foodapp.repository.UserRepository;
import com.foodapp.security.JwtTokenUtil;
import com.foodapp.service.UserService;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PutMapping("/updateName")
    public ResponseEntity<?> updateUserName(@RequestHeader("Authorization") String token, @RequestBody UpdateNameRequest updateNameRequest) {
        String username = jwtTokenUtil.extractUsername(token.substring(7)); // Remove "Bearer " prefix
        User user = userRepository.findByEmail(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }

        // Update first name and last name
        user.setFirstName(updateNameRequest.getFirstName());
        user.setLastName(updateNameRequest.getLastName());

        userRepository.save(user);

        return ResponseEntity.ok(user);
    }
    
    @PostMapping("/updatePassword")
    public ResponseEntity<?> updatePassword(@RequestBody UpdatePasswordRequest updatePasswordRequest) {
        // Get authenticated user details from security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Load user details by username
        UserDetails userDetails = userService.loadUserByUsername(username);

        // Check if the provided password matches the encoded password
        if (!passwordEncoder.matches(updatePasswordRequest.getCurrentPassword(), userDetails.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Current password is incorrect");
        }

        // Update the user's password
        User user = userRepository.findByEmail(username);
        user.setPassword(passwordEncoder.encode(updatePasswordRequest.getNewPassword()));
        userRepository.save(user);

        return ResponseEntity.ok("Password updated successfully");
    }
    
    @PostMapping("/updateProfilePhoto")
    public ResponseEntity<?> updateProfilePhoto(@RequestParam("file") MultipartFile file) {
        String username = getUsername(); // Implement your method to get the username
        User user = userRepository.findByEmail(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        try {
            if (file.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty");
            }

            byte[] bytes = file.getBytes();
            userRepository.updateProfilePhoto(bytes, user.getId());

            return ResponseEntity.ok("Profile photo updated successfully");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update profile photo");
        }
    }
 // Example method to get username from JWT token or security context
    private String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        } else {
            // Handle the case where the user is not authenticated
            return null;
        }
    }
}
