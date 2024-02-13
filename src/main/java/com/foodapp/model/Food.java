package com.foodapp.model;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String description;
    
    @Lob
    private byte[] image1;
    
    @Lob
    private byte[] image2;
    
    @Lob
    private byte[] image3;
    
    private String name;
    private double price;
    private String status;
    
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    
    @ManyToOne
    @JoinColumn(name = "restaurant_user_id")
    private User restaurantUser;
    
    private String ingredients1;

    private String ingredients2;

    private String ingredients3;
}
