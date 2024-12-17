package com.sr.Ziply.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private User customer;
    private Long total;
    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
    private List<CartItems> cartItems=new ArrayList<>();
}