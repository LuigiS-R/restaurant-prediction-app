package com.example.TechWeek25.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "neighborhoods")
public class Neighborhood{
    @Id
    private String dongCode;
    private String cityName;
    private String guName;
    private String dongName;
    private Double populationDensity;
    private Double avgAge;
    private Integer totalCount;
    @Column(columnDefinition = "geometry(MultiPolygon, 4326)")
    private Geometry dongBoundaries;
    private Double averagePricePerSqrMeter;
}