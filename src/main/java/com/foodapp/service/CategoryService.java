package com.foodapp.service;
import java.util.List;

import com.foodapp.model.Category;

public interface CategoryService {
    List<Category> getAllCategories();
    Category addCategory(Category category);
}
