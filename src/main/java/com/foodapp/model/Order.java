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
@Table(name = "`order`")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Date deliveryDate;
    private String deliveryStatus;
    private String deliveryTime;
    private Long orderId;
    private Date orderTime;
    private int quantity;
    private String status;
    
    @ManyToOne
    @JoinColumn(name = "delivery_person_id")
    private User deliveryPerson;
    
    @ManyToOne
    @JoinColumn(name = "food_id")
    private Food food;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Getters and setters
}
