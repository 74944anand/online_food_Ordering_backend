package com.foodapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.foodapp.dto.LoginResponseDTO;
import com.foodapp.model.User;
import com.foodapp.security.JwtTokenUtil;
import com.foodapp.service.UserService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            // Load user details by email
            UserDetails userDetails = userService.loadUserByUsername(user.getEmail());
            
            // Check if the provided password matches the encoded password
            if (!passwordEncoder.matches(user.getPassword(), userDetails.getPassword())) {
                throw new BadCredentialsException("Invalid password");
            }
            
            // Generate JWT token
            String jwtToken = jwtTokenUtil.generateToken(userDetails);
            
            // Return JWT token and user email
            LoginResponseDTO response = new LoginResponseDTO(jwtToken, userDetails.getUsername(),userDetails.getAuthorities());
            
            // Set authentication in SecurityContextHolder
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println("Inside Login");
            // Return the JWT token and user email
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
    }

    @PostMapping("user/logout")
    public ResponseEntity<?> logout() {
        SecurityContextHolder.clearContext();
        System.out.println("inside logout");
        return ResponseEntity.ok("Logout successful");
    }
    
    @GetMapping("/user")
    public ResponseEntity<?> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            System.out.println("Inside get user details");
            return ResponseEntity.ok("Currently logged-in user: " + userDetails.getUsername());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No user logged in");
    }
}
