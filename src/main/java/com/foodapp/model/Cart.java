package com.foodapp.model;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Date addedTime;
    private int quantity;
    
    @ManyToOne
    @JoinColumn(name = "food_id")
    private Food food;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Getters and setters
}
