package com.example.TechWeek25.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.locationtech.jts.geom.Geometry;

@Entity
public class SeoulSubwayStation {
    @Id
    private String subwayStationId;
    private String dongCode;
    @Column(columnDefinition = "geometry(Point, 4326)")
    private Geometry location;
}
