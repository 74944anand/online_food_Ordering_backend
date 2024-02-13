package com.foodapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.foodapp.dto.FoodDTO;
import com.foodapp.model.Category;
import com.foodapp.model.Food;
import com.foodapp.model.User;
import com.foodapp.repository.FoodRepository;
import com.foodapp.repository.UserRepository;
import com.foodapp.service.FoodService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/foods")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class FoodController {

    @Autowired
    private FoodService foodService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FoodRepository foodRepository;

    @PostMapping("/fil")
    public ResponseEntity<String> addFood(@RequestParam("name") String name,
                                          @RequestParam("description") String description,
                                          @RequestParam("price") double price,
                                          @RequestParam("categoryId") Long categoryId,
                                          @RequestParam("image1") MultipartFile image1,
                                          @RequestParam("image2") MultipartFile image2,
                                          @RequestParam("image3") MultipartFile image3) {
        try {
            System.out.println("Inside Add Food");
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
            }
            
            String username = authentication.getName();
            User user = userRepository.findByEmail(username);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
            
            // Create food object
            Food food = new Food();
            food.setName(name);
            food.setDescription(description);
            food.setPrice(price);
            // Set Category
            Category category = new Category();
            category.setId(categoryId);
            food.setCategory(category);
            // Set images and other fields
            food.setImage1(image1.getBytes());
            food.setImage2(image2.getBytes());
            food.setImage3(image3.getBytes());
            food.setRestaurantUser(user); // Set restaurant
            
            // Add food
            foodService.createFood(food);
            
            return ResponseEntity.ok("Food added successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding food");
        }
    }

        @PostMapping ("/add")
        public ResponseEntity<?> addFood(@RequestBody Food food) {
            try {
                Food newFood = foodRepository.save(food);
                return new ResponseEntity<>(newFood, HttpStatus.CREATED);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    
    
    @GetMapping("/searchByLoggedInRestaurant")
    public ResponseEntity<List<Food>> searchByLoggedInRestaurant() {
        // Get the currently authenticated user's email (assuming it's the restaurant's email)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String restaurantEmail = authentication.getName();

        // Retrieve foods added by the logged-in restaurant
        List<Food> foods = foodRepository.findByRestaurantUserEmail(restaurantEmail);
        if (foods.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Or return an appropriate message
        }
        return ResponseEntity.ok(foods);
    }

    @GetMapping
    public ResponseEntity<List<Food>> getAllFoods() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<Food> foods = null;
        if (authentication != null && authentication.isAuthenticated()) {
            foods = foodService.getAllFoods();
        }
        return new ResponseEntity<>(foods, HttpStatus.OK);
    }

    @PutMapping("/{foodId}/setStatus")
    public ResponseEntity<?> setStatus(@PathVariable Long foodId, @RequestParam String status) {
        Food food = foodRepository.findById(foodId).orElse(null);
        if (food == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Food not found");
        }

        // Update the status of the food
        food.setStatus(status);
        foodRepository.save(food);

        return ResponseEntity.ok("Status updated successfully");
    }
}
