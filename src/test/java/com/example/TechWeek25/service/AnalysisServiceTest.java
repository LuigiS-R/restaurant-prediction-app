/*package com.example.TechWeek25.service;

import com.example.TechWeek25.dto.CategoryCount;
import com.example.TechWeek25.dto.Coordinates;
import com.example.TechWeek25.dto.PredictionData;
import com.example.TechWeek25.dto.PopulationCount;
import com.example.TechWeek25.entity.NeighborhoodDTO;
import com.example.TechWeek25.repository.BusinessStatRepo;
import com.example.TechWeek25.repository.NeighborhoodRepository;
import com.example.TechWeek25.repository.PopulationStatRepo;
import com.example.TechWeek25.util.exceptions.InvalidInputException;
import com.example.TechWeek25.util.exceptions.NeighborhoodNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.locationtech.jts.geom.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AnalysisServiceTest {
   @Mock
   NeighborhoodRepository neighborhoodRepository;
   @Mock
   GeometryFactory geometryFactory;
   @Mock
   BusinessStatRepo businessStatRepo;
   @Mock
   PopulationStatRepo populationStatRepo;

   @InjectMocks
   AnalysisService analysisService;

   @Test
   public void AnalysisService_Analysis_ReturnsNeighborhoodData() throws Exception{
       Point point = new Point(new Coordinate(129.0594829, 35.1573174), new PrecisionModel(), 4326); //Point located at Seomyeon

      NeighborhoodDTO neighborhood = new NeighborhoodDTO("21050520",
               "부산광역시",
               "부산진구",
               "부전2동",
               15997.3,
               40.0,
               0,
               Mockito.mock(Geometry.class));

       List<CategoryCount> categoryCounts = new ArrayList<>(); categoryCounts.add(Mockito.mock(CategoryCount.class));
       List<PopulationCount> populationCounts = new ArrayList<>(); populationCounts.add(Mockito.mock(PopulationCount.class));

       Mockito.when(geometryFactory.createPoint(Mockito.any(Coordinate.class)))
               .thenReturn(point);

       Mockito.when(neighborhoodRepository.findByDongBoundariesContains(Mockito.any(Point.class)))
               .thenReturn(Optional.of(neighborhood));

       Mockito.when(businessStatRepo.findBusinessCounts(Mockito.any(String.class), Mockito.any(String.class)))
               .thenReturn(categoryCounts);

       Mockito.when(populationStatRepo.findPopulationCountByGenderAndDongCodeOrderByAgeGroup(Mockito.any(String.class), Mockito.any(String.class)))
               .thenReturn(populationCounts);

       Assertions.assertThat(analysisService.analyze(new Coordinates("35.1573174", "129.0594829")).getClass())
               .isEqualTo(PredictionData.class);
       Assertions.assertThat(analysisService.analyze(new Coordinates("35.1573174", "129.0594829")).getDongName()).isEqualTo("부전2동");
   }

   @ParameterizedTest
   @CsvSource({
           "aaa, aaa",
           "35.1573, aaa",
           ",",
           "45. 1 , "
   })
   public void AnalysisService_Analysis_RaisesNumberFormatException(String latitude, String longitude){
       Assertions.assertThatThrownBy(()->{
           analysisService.analyze(new Coordinates(latitude, longitude));
       }).isInstanceOf(InvalidInputException.class);
   }

   @Test
   public void AnalysisService_Analysis_RaisesNeighborhoodNotFoundException(){
       Point point = Mockito.mock(Point.class);

       Mockito.when(geometryFactory.createPoint(Mockito.any(Coordinate.class)))
               .thenReturn(point);

       Mockito.when(neighborhoodRepository.findByDongBoundariesContains(Mockito.any(Point.class)))
               .thenReturn(Optional.empty());

       Assertions.assertThatThrownBy(()->{
                   analysisService.analyze(new Coordinates("1.0", "1.0"));
               })
               .isInstanceOf(NeighborhoodNotFoundException.class);
   }
}*/