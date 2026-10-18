package com.example.TechWeek25.controller;

import com.example.TechWeek25.dto.*;
import com.example.TechWeek25.entity.Neighborhood;
import com.example.TechWeek25.service.AnalysisService;
import com.example.TechWeek25.util.exceptions.InvalidInputException;
import com.example.TechWeek25.util.exceptions.NeighborhoodNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@AllArgsConstructor
@RequestMapping("/api/v1")
public class AnalysisController {
    private final AnalysisService analysisService;

    @PostMapping("/locations/analysis")
    public ResponseEntity<Object> getNeighborhoodData(@RequestBody UserInput input){
        try{
            PredictionData response = analysisService.analyze(input);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch(NeighborhoodNotFoundException e){
            ErrorResponse response = new ErrorResponse("NeighborhoodDTO Not Found Error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
        }
        catch (InvalidInputException e){
            ErrorResponse response = new ErrorResponse("Invalid Input Error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }


    }

    @PostMapping("/location/dongName")
    public ResponseEntity<Object> getNeighborhood(@RequestBody Coordinates location){
        try{
            NeighborhoodDTO neighborHoodName = analysisService.getNeighborhood(analysisService.getLocationPoint(location));
            return new ResponseEntity<>(neighborHoodName, HttpStatus.OK);
        }
        catch(NeighborhoodNotFoundException e){
            ErrorResponse response = new ErrorResponse("NeighborhoodDTO Not Found Error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
        }
        catch (InvalidInputException e){
            ErrorResponse response = new ErrorResponse("Invalid Input Error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

}
