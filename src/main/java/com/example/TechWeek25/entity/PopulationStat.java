package com.example.TechWeek25.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "population_stats")
public class PopulationStat {
    @Id
    private Integer statId;
    private String dongCode;
    private String gender;
    private String ageGroup;
    private Integer populationCount;
}
