package com.foodapp.controller;

import com.foodapp.model.Category;
import com.foodapp.model.User;
import com.foodapp.repository.UserRepository;
import com.foodapp.security.JwtTokenUtil;
import com.foodapp.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@GetMapping
	public ResponseEntity<List<Category>> getAllCategories() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()) {
			List<Category> categories = categoryService.getAllCategories();
			return new ResponseEntity<>(categories, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
	}

	@PostMapping
	public ResponseEntity<Category> addCategory(@RequestBody Category category) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		System.out.println(authentication);
		 if (authentication != null && authentication.isAuthenticated()) {
			 Category savedCategory = categoryService.addCategory(category);
				return new ResponseEntity<>(savedCategory, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}  
			 
		 }

}
