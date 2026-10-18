package com.example.TechWeek25.service.provider;

import com.example.TechWeek25.dto.CategoryCount;
import com.example.TechWeek25.dto.PopulationCount;
import com.example.TechWeek25.repository.BusinessStatRepo;
import com.example.TechWeek25.repository.PopulationStatRepo;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class NeighborhoodStatisticsProvider {
    private final BusinessStatRepo businessStatRepo;
    private final PopulationStatRepo populationStatRepo;

    @Cacheable(value = "generalCategories", key = "#dongCode")
    public Map<String, Integer> buildGeneralCategories(String dongCode){
        Map<String, Integer> categoryCountsMap = new HashMap<>();
        List<CategoryCount> categoryCounts = businessStatRepo.findBusinessCounts("general", dongCode);
        if (!categoryCounts.isEmpty()){
            for(CategoryCount count: categoryCounts){
                categoryCountsMap.put(count.getCategoryName(), count.getCount());
            }
        }
        else{
            System.out.println("Query result set is empty!");
        }
        return categoryCountsMap;
    }

    @Cacheable(value = "specificCategories", key = "#dongCode")
    public Map<String, Integer> buildSpecificCategories(String dongCode){
        Map<String, Integer> categoryCountsMap = new HashMap<>();
        List<CategoryCount> categoryCounts = businessStatRepo.findBusinessCounts("specific", dongCode);
        if (!categoryCounts.isEmpty()){
            for(CategoryCount count: categoryCounts){
                categoryCountsMap.put(count.getCategoryName(), count.getCount());
            }
        }
        else{
            System.out.println("Query result set is empty!");
        }
        return categoryCountsMap;
    }

    @Cacheable(value = "femalePopulation", key = "#dongCode")
    public Map<String, Integer> buildFemalePopulation(String dongCode){
        Map<String, Integer> populationCountsMap = new HashMap<>();
        List<PopulationCount> populationCounts = populationStatRepo.findPopulationCountByGenderAndDongCodeOrderByAgeGroup("F", dongCode);
        if (!populationCounts.isEmpty()){
            for(PopulationCount count: populationCounts){
                populationCountsMap.put(count.getAgeGroup(), count.getPopulationCount());
            }
        }
        else{
            System.out.println("Query result set is empty!");
        }
        return populationCountsMap;
    }

    @Cacheable(value = "malePopulation", key = "#dongCode")
    public Map<String, Integer> buildMalePopulation(String dongCode){
        Map<String, Integer> populationCountsMap = new HashMap<>();
        List<PopulationCount> populationCounts = populationStatRepo.findPopulationCountByGenderAndDongCodeOrderByAgeGroup("M", dongCode);
        if (!populationCounts.isEmpty()){
            for(PopulationCount count: populationCounts){
                populationCountsMap.put(count.getAgeGroup(), count.getPopulationCount());
            }
        }
        else{
            System.out.println("Query result set is empty!");
        }
        return populationCountsMap;
    }
}
