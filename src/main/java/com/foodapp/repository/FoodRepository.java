package com.foodapp.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foodapp.model.Food;

public interface FoodRepository extends JpaRepository<Food, Long> {

	List<Food> findByRestaurantUserEmail(String restaurantEmail);
}
