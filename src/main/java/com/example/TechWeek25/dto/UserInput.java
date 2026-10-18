package com.example.TechWeek25.dto;

import lombok.Data;

@Data
public class UserInput {
    private Coordinates coordinates;
    private String category;
    private Double restaurantArea;
}
