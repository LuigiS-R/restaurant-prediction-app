package com.example.TechWeek25.repository;

import com.example.TechWeek25.dto.PopulationCount;
import com.example.TechWeek25.entity.PopulationStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PopulationStatRepo extends JpaRepository<PopulationStat, Integer> {
    List<PopulationCount> findPopulationCountByGenderAndDongCodeOrderByAgeGroup(String gender, String dongCode);
}
