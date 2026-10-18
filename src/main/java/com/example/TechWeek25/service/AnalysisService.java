package com.example.TechWeek25.service;

import com.example.TechWeek25.dto.Coordinates;
import com.example.TechWeek25.dto.NeighborhoodDTO;
import com.example.TechWeek25.dto.PredictionData;
import com.example.TechWeek25.dto.UserInput;
import com.example.TechWeek25.entity.Neighborhood;
import com.example.TechWeek25.repository.BusinessRepo;
import com.example.TechWeek25.repository.NeighborhoodRepository;
import com.example.TechWeek25.repository.SeoulBusStopRepo;
import com.example.TechWeek25.repository.SeoulSubwayStationRepo;
import com.example.TechWeek25.service.provider.NeighborhoodStatisticsProvider;
import com.example.TechWeek25.util.exceptions.InvalidInputException;
import com.example.TechWeek25.util.exceptions.NeighborhoodNotFoundException;
import lombok.AllArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class AnalysisService {
    private final NeighborhoodRepository neighborhoodRepository;
    private final GeometryFactory geometryFactory;
    private final NeighborhoodStatisticsProvider neighborhoodStatisticsProvider;
    private final CacheInspectionService cacheInspectionService;
    private final BusinessFeasibilityPredictionService predictionService;
    private final BusinessRepo businessRepo;
    private final SeoulBusStopRepo seoulBusStopRepo;
    private final SeoulSubwayStationRepo seoulSubwayStationRepo;

    private Map<String, Object> getNeighborhoodData(NeighborhoodDTO neighborhood){
        Map<String, Object> neighborHoodData = new HashMap<>();
        String dongCode = neighborhood.getDongCode();

        neighborHoodData.put("dongName", neighborhood.getDongName());
        neighborHoodData.put("avgPricePerSqrMeter", neighborhood.getAveragePricePerSqrMeter());
        neighborHoodData.put("malePopulationCounts", neighborhoodStatisticsProvider.buildMalePopulation(dongCode));
        neighborHoodData.put("femalePopulationCounts", neighborhoodStatisticsProvider.buildFemalePopulation(dongCode));

        return neighborHoodData;
    }

    /*public Neighborhood getNeighborhoodName(Coordinates location) throws InvalidInputException, NeighborhoodNotFoundException{
        Neighborhood neighborhood = getNeighborhood(getLocationPoint(location));
        neighborhood.setDongBoundaries(null);
        return neighborhood;
    }*/

    private Map<String, Object> getLocationRelativeData(Point location, String dongCode, String restaurantCategory){
        Map<String, Object> locationRelativeData = new HashMap<>();

        final int restaurantsIn500M = businessRepo.countByLocationWithin(location, 500, dongCode);
        final int competitionIn500M = businessRepo.countByLocationAndCategoryWithin(location, 500, dongCode, restaurantCategory);
        final double marketShare = (double)competitionIn500M / restaurantsIn500M;
        final double closestBusStop = seoulBusStopRepo.getClosestDistance(location);
        final double closestSubwayStation = seoulSubwayStationRepo.getClosestDistance(location);
        final double closestTransportationHub = Math.min(closestBusStop, closestSubwayStation);
        final int busStopsIn500M = seoulBusStopRepo.countByLocationWithin(location, 500, dongCode);
        final int subwayStationsIn500M = seoulSubwayStationRepo.countByLocationWithin(location, 500, dongCode);
        final int transportationHubsIn500M = busStopsIn500M + subwayStationsIn500M;

        locationRelativeData.put("restaurantsIn500M", restaurantsIn500M);
        locationRelativeData.put("competitionIn500M", competitionIn500M);
        locationRelativeData.put("marketShare", marketShare);
        locationRelativeData.put("distanceToClosestTransportationHub", closestTransportationHub);
        locationRelativeData.put("transportationHubsIn500M", transportationHubsIn500M);

        return locationRelativeData;
    }

    private Map<String, Object> getDataToFeedPredictionService(NeighborhoodDTO targetNeighborhood, Point userLocation, String category){
        Map<String, Object> neighborhoodData = getNeighborhoodData(targetNeighborhood);
        Map<String, Object> locationRelativeData = getLocationRelativeData(userLocation, targetNeighborhood.getDongCode(), category);

        Map<String, Object> dataToFeedPredictionService = new HashMap<>();
        dataToFeedPredictionService.putAll(neighborhoodData);
        dataToFeedPredictionService.putAll(locationRelativeData);

        return dataToFeedPredictionService;
    }

    public PredictionData analyze(UserInput input) throws NeighborhoodNotFoundException, InvalidInputException{
        Coordinates location = input.getCoordinates();
        String category = input.getCategory();

        final Point locationPoint = getLocationPoint(location);
        final NeighborhoodDTO neighborhood = getNeighborhood(locationPoint);

        Map<String, Object> dataToFeedPredictionService = getDataToFeedPredictionService(neighborhood, locationPoint, category);

        float prediction = test(dataToFeedPredictionService, category, input.getRestaurantArea());

        PredictionData predictionData = new PredictionData();
        predictionData.setData(dataToFeedPredictionService);
        predictionData.setPredictionValue(prediction);

        return predictionData;
    }

    public NeighborhoodDTO getNeighborhood(Point locationPoint) throws NeighborhoodNotFoundException {
        Optional<NeighborhoodDTO> neighborhood = neighborhoodRepository.findByDongBoundariesContains(locationPoint);
        if (neighborhood.isEmpty()){
            throw new NeighborhoodNotFoundException("Please make sure to enter coordinates suited for South Korea");
        }
        return neighborhood.get();
    }

    public Point getLocationPoint(Coordinates location) throws InvalidInputException{
        try{
            double longitude = Double.parseDouble(location.getLongitude());
            double latitude = Double.parseDouble(location.getLatitude());
            return geometryFactory.createPoint(new Coordinate(longitude, latitude));
        }
        catch (NumberFormatException | NullPointerException e){
            throw new InvalidInputException("Please make sure to enter a valid set of coordinates");
        }
    }

    private float test(Map<String, Object> data, String category, Double requestedRestaurantArea) throws InvalidInputException {
        if (requestedRestaurantArea == null
                || !Double.isFinite(requestedRestaurantArea)
                || requestedRestaurantArea < 0) {
            throw new InvalidInputException("A non-negative restaurantArea is required for prediction");
        }

        float restaurantArea = requestedRestaurantArea.floatValue();
        int businessType = encodeBusinessType(category);
        int restaurants500m = (int)data.get("restaurantsIn500M");
        int competition500m = (int)data.get("competitionIn500M");
        int transportHubs500m = (int)data.get("transportationHubsIn500M");
        double closestHubDist = (double)data.get("distanceToClosestTransportationHub");
        double pricePerSqm = (double)data.get("avgPricePerSqrMeter");
        double marketShare = (double)data.get("marketShare");
        try {
            float successProbability = predictionService.predictSuccess(
                    restaurantArea,
                    businessType,
                    restaurants500m,
                    competition500m,
                    transportHubs500m,
                    closestHubDist,
                    pricePerSqm,
                    marketShare
            );
            return successProbability * 100;

        } catch (Exception e) {
            System.err.println("Prediction failed: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    private int encodeBusinessType(String category) throws InvalidInputException {
        if (category == null || category.isBlank()) {
            throw new InvalidInputException("A business category is required for prediction");
        }

        List<String> categories = new ArrayList<>(businessRepo.findDistinctCategories());
        categories.sort(Comparator.naturalOrder());
        int encodedCategory = categories.indexOf(category);
        if (encodedCategory < 0) {
            throw new InvalidInputException("Unknown business category: " + category);
        }
        return encodedCategory;
    }
}
