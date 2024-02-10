package com.foodapp.controller;

import java.util.Base64;

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
import com.foodapp.dto.UserDTO;
import com.foodapp.model.User;
import com.foodapp.repository.UserRepository;
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
	private UserRepository userRepository; // Autowire UserRepository

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
			LoginResponseDTO response = new LoginResponseDTO(jwtToken, userDetails.getUsername(),
					userDetails.getAuthorities());

			// Set authentication in SecurityContextHolder
			Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
					userDetails.getAuthorities());
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
    public ResponseEntity<?> getUserDetails() {
        // Get the authentication object from SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if the authentication object exists and if the user is authenticated
        if (authentication != null && authentication.isAuthenticated()) {
            // Get the username from the authentication object
            String username = authentication.getName();

            // Load user details by username
            User user = userRepository.findByEmail(username);

            // Create UserDTO from User
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setFirstName(user.getFirstName());
            userDTO.setLastName(user.getLastName());
            userDTO.setEmail(user.getEmail());
            userDTO.setContactNo(user.getContactNo());
            userDTO.setRole(user.getRole());
            userDTO.setStatus(user.getStatus());
            userDTO.setAddressId(user.getAddressId());
            userDTO.setRestaurantId(user.getRestaurantId());
            
            // Convert profile photo byte array to base64 string
            if (user.getProfilePhoto() != null) {
                String base64EncodedProfilePhoto = Base64.getEncoder().encodeToString(user.getProfilePhoto());
                System.out.println(user.getProfilePhoto());
                userDTO.setProfilePhoto(base64EncodedProfilePhoto);
                System.out.println(base64EncodedProfilePhoto);
            }

            // Return the user details as a response
            return ResponseEntity.ok(userDTO);
        } else {
            // If the user is not authenticated, return an unauthorized status
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
    }
}

