package com.example.TechWeek25.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NeighborhoodDTO {
    private String dongCode;
    private String cityName;
    private String guName;
    private String dongName;
    private Double populationDensity;
    private Double avgAge;
    private Double averagePricePerSqrMeter;
}
