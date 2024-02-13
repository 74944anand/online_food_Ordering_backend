package com.foodapp.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foodapp.model.Food;
import com.foodapp.repository.FoodRepository;

import java.util.List;

@Service
public class FoodService {

    @Autowired
    private FoodRepository foodRepository;

    public List<Food> getAllFoods() {
        return foodRepository.findAll();
    }
    
    public Food createFood(Food food) {
        return foodRepository.save(food);
    }
}
