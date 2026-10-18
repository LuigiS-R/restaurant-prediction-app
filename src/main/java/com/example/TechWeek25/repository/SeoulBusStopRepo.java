package com.example.TechWeek25.repository;

import com.example.TechWeek25.entity.SeoulBusStop;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SeoulBusStopRepo extends JpaRepository<SeoulBusStop, String> {
    @Query(value = "SELECT count(*) FROM seoul_bus_stop WHERE ST_DWithin(seoul_bus_stop.location::geography, :location ::geography, :distance) AND seoul_bus_stop.dong_code = :dong", nativeQuery = true)
    Integer countByLocationWithin(@Param("location") Point location, @Param("distance") int distance, @Param("dong") String dongCode);

    @Query(value = """
    SELECT ST_DistanceSphere(sbs.location, :location) 
    FROM seoul_bus_stop sbs 
    ORDER BY sbs.location <-> :location 
    LIMIT 1
    """, nativeQuery = true)
    Double getClosestDistance(@Param("location") Point location);
}
