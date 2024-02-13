package com.foodapp.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FoodDTO {
    private String description;
    private byte[] image1;
    private byte[] image2;
    private byte[] image3;
    private String name;
    private double price;
    private String status;
    private Long categoryId;
    private Long restaurantUserId;
    private String ingredients1;
    private String ingredients2;
    private String ingredients3;

    // Getters and setters
}