package com.tripmanagement.controller;

import com.tripmanagement.dto.ResponseDto;
import com.tripmanagement.dto.TripDto;
import com.tripmanagement.dto.TripSummaryDto;
import com.tripmanagement.entity.Trip;
import com.tripmanagement.enums.TripStatus;
import com.tripmanagement.mapper.TripMapper;
import com.tripmanagement.service.TripService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/api")
@RequiredArgsConstructor
public class TripController {

    @Autowired
    private final TripService tripService;
    @Autowired
    private TripMapper tripMapper;

    @PostMapping("/trips")
    public ResponseEntity<ResponseDto<TripDto>> addTrip(@Valid @RequestBody TripDto tripDto){
        TripDto savedTripDto = tripService.saveTrip(tripDto);
        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatusCode(HttpStatus.valueOf(Integer.parseInt("201")));
        responseDto.setMessage("Trip created successfully");
        responseDto.setData(savedTripDto);
        return new ResponseEntity<>(responseDto, responseDto.getStatusCode());
    }

    @GetMapping("/trips/all")
    public ResponseEntity<ResponseDto<List<TripDto>>> getAllTrip(){
        List<TripDto> tripDtoList = tripService.getAllTrips();
        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatusCode(HttpStatus.valueOf(Integer.parseInt("201")));
        responseDto.setMessage("All trips fetched successfully");
        responseDto.setData(tripDtoList);
        return new ResponseEntity<>(responseDto, responseDto.getStatusCode());
    }

    @GetMapping("/trips")
    public ResponseEntity<ResponseDto<Page<TripDto>>> getTrips(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sort){

        Page<TripDto> tripDtoPage = tripService.getPaginatedTrips(page, size, sort);

        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatusCode(HttpStatus.valueOf(Integer.parseInt("201")));
        responseDto.setMessage("Trips found successfully");
        responseDto.setData(tripDtoPage);
        return new ResponseEntity<>(responseDto, responseDto.getStatusCode());
    }

    @GetMapping("/trips/{id}")
    public ResponseEntity<ResponseDto<TripDto>> getTripById(@PathVariable String id){
        TripDto tripDto = tripService.getTripById(id);
        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatusCode(HttpStatus.valueOf(Integer.parseInt("201")));
        responseDto.setMessage("Trip Id "+id+" fetched successfully");
        responseDto.setData(tripDto);
        return new ResponseEntity<>(responseDto, responseDto.getStatusCode());
    }

    @PutMapping("/trips/{id}")
    public ResponseEntity<ResponseDto<TripDto>> updateTripById(
            @PathVariable int id,
            @Valid @RequestBody TripDto tripDto){

        Trip trip = tripMapper.mapTripDtoToTrip(tripDto);
        TripDto updatedTrip = tripService.updateTripById(id, trip);
        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatusCode(HttpStatus.OK);
        responseDto.setMessage("Trip updated successfully");
        responseDto.setData(updatedTrip);

        return new ResponseEntity<>(responseDto, responseDto.getStatusCode());
    }

    @DeleteMapping("/trips/{id}")
    public ResponseEntity<ResponseDto<TripDto>> deleteTripByTd(@PathVariable int id){
        TripDto deleteTrip = tripService.deleteTripById(id);
        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatusCode(HttpStatus.OK);
        responseDto.setMessage("Trip deleted successfully");
        responseDto.setData(deleteTrip);
        return new ResponseEntity<>(responseDto, responseDto.getStatusCode());
    }

    @GetMapping("/trips/search")
    public ResponseEntity<ResponseDto<List<TripDto>>> getTripsByDestination(@RequestParam String destination){
        List<TripDto> tripsByDestination = tripService.getTripsByDestination(destination);
        ResponseDto<List<TripDto>> responseDto = new ResponseDto<>();
        responseDto.setStatusCode(HttpStatus.OK);
        responseDto.setMessage("Trips with destination "+destination+" found successfully");
        responseDto.setData(tripsByDestination);
        return new ResponseEntity<>(responseDto, responseDto.getStatusCode());
    }

    @GetMapping("/trips/filter")
    public ResponseEntity<ResponseDto<List<TripDto>>> getTripsByStatus(@RequestParam TripStatus status){
        List<TripDto> filterTripByStatus = tripService.getTripByStatus(status);
        ResponseDto<List<TripDto>> responseDto = new ResponseDto<>();
        responseDto.setStatusCode(HttpStatus.OK);
        responseDto.setMessage("Trips with status "+status+" found successfully");
        responseDto.setData(filterTripByStatus);
        return new ResponseEntity<>(responseDto, responseDto.getStatusCode());
    }

    @GetMapping("/trips/daterange")
    public ResponseEntity<ResponseDto<List<TripDto>>> getTripsByDateRange(@RequestParam String start, @RequestParam String end){
        List<TripDto> tripsBetweenDates = tripService.getTripsBetweenDates(start, end);
        ResponseDto<List<TripDto>> responseDto = new ResponseDto<>();
        responseDto.setStatusCode(HttpStatus.OK);
        responseDto.setMessage("Trips between dates found successfully");
        responseDto.setData(tripsBetweenDates);
        return new ResponseEntity<>(responseDto, responseDto.getStatusCode());
    }

    @GetMapping("/trips/summary")
    public ResponseEntity<ResponseDto<TripSummaryDto>> getTripSummary(){
        TripSummaryDto summary = tripService.getTripSummary();
        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatusCode(HttpStatus.OK);
        responseDto.setMessage("Trip summary found successfully");
        responseDto.setData(summary);
        return new ResponseEntity<>(responseDto, responseDto.getStatusCode());
    }

}
