package com.example.TechWeek25.repository;


import com.example.TechWeek25.dto.NeighborhoodDTO;
import com.example.TechWeek25.entity.Neighborhood;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NeighborhoodRepository extends JpaRepository<Neighborhood, String> {
    @Query(value = """
    SELECT 
        dong_code,
        city_name,
        gu_name, 
        dong_name, 
        CAST(population_density AS DOUBLE PRECISION) as population_density, 
        CAST(avg_age AS DOUBLE PRECISION) as avg_age, 
        CAST(average_price_per_sqr_meter AS DOUBLE PRECISION) as average_price_per_sqr_meter 
    FROM neighborhoods 
    WHERE ST_Contains(dong_boundaries, :point)
    """, nativeQuery = true)
    Optional<NeighborhoodDTO> findByDongBoundariesContains(@Param("point") Point location);
}