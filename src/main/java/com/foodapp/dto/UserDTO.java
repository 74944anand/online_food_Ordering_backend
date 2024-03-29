package com.foodapp.dto;

import com.foodapp.model.Address;
import com.foodapp.model.RestaurantTable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String contactNo;
    private String role;
    private String status;
    private Long addressId;
    private Long restaurantId;
    private String profilePhoto;
	

}