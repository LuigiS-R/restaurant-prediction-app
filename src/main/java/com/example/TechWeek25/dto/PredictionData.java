package com.example.TechWeek25.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PredictionData {
    private double predictionValue;
    private Map<String, Object> data;
}