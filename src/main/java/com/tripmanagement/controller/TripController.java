package com.tripmanagement.controller;

import com.tripmanagement.dto.ResponseDto;
import com.tripmanagement.dto.TripDto;
import com.tripmanagement.dto.TripSummaryDto;
import com.tripmanagement.entity.Trip;
import com.tripmanagement.enums.TripStatus;
import com.tripmanagement.mapper.TripMapper;
import com.tripmanagement.service.TripService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(
        name = "CRUD APIs for Trip Management",
        description = "REST api of trip management like GET, POST, PUT, DELETE"
)
@RequiredArgsConstructor
public class TripController {

    @Autowired
    private final TripService tripService;
    @Autowired
    private TripMapper tripMapper;

    @Operation(summary = "Create a new trip", description = "Creates a new trip with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Trip created successfully",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "Trip already exists with same details",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @PostMapping("/trips")
    public ResponseEntity<ResponseDto<TripDto>> addTrip(@Valid @RequestBody TripDto tripDto){
        TripDto savedTripDto = tripService.saveTrip(tripDto);
        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatusCode(HttpStatus.valueOf(Integer.parseInt("201")));
        responseDto.setMessage("Trip created successfully");
        responseDto.setData(savedTripDto);
        return new ResponseEntity<>(responseDto, responseDto.getStatusCode());
    }

    @Operation(summary = "Get all trips", description = "Retrieves a list of all trips in the system")
    @ApiResponse(responseCode = "201", description = "All trips fetched successfully",
            content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    @GetMapping("/trips/all")
    public ResponseEntity<ResponseDto<List<TripDto>>> getAllTrip(){
        List<TripDto> tripDtoList = tripService.getAllTrips();
        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatusCode(HttpStatus.valueOf(Integer.parseInt("201")));
        responseDto.setMessage("All trips fetched successfully");
        responseDto.setData(tripDtoList);
        return new ResponseEntity<>(responseDto, responseDto.getStatusCode());
    }

    @Operation(summary = "Get paginated trips", description = "Retrieves a paginated list of trips with optional sorting")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Trips found successfully",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
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

    @Operation(summary = "Get trip by ID", description = "Retrieves a specific trip by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Trip fetched successfully",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid ID or trip not found",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @GetMapping("/trips/{id}")
    public ResponseEntity<ResponseDto<TripDto>> getTripById(@PathVariable String id){
        TripDto tripDto = tripService.getTripById(id);
        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatusCode(HttpStatus.valueOf(Integer.parseInt("201")));
        responseDto.setMessage("Trip Id "+id+" fetched successfully");
        responseDto.setData(tripDto);
        return new ResponseEntity<>(responseDto, responseDto.getStatusCode());
    }

    @Operation(summary = "Update a trip", description = "Updates an existing trip with new details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trip updated successfully",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Trip not found",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
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

    @Operation(summary = "Delete a trip", description = "Deletes a trip by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trip deleted successfully",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Trip not found",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @DeleteMapping("/trips/{id}")
    public ResponseEntity<ResponseDto<TripDto>> deleteTripByTd(@PathVariable int id){
        TripDto deleteTrip = tripService.deleteTripById(id);
        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatusCode(HttpStatus.OK);
        responseDto.setMessage("Trip deleted successfully");
        responseDto.setData(deleteTrip);
        return new ResponseEntity<>(responseDto, responseDto.getStatusCode());
    }

    @Operation(summary = "Search trips by destination", description = "Finds trips matching the specified destination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trips found successfully",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "No trips found with given destination",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @GetMapping("/trips/search")
    public ResponseEntity<ResponseDto<List<TripDto>>> getTripsByDestination(@RequestParam String destination){
        List<TripDto> tripsByDestination = tripService.getTripsByDestination(destination);
        ResponseDto<List<TripDto>> responseDto = new ResponseDto<>();
        responseDto.setStatusCode(HttpStatus.OK);
        responseDto.setMessage("Trips with destination "+destination+" found successfully");
        responseDto.setData(tripsByDestination);
        return new ResponseEntity<>(responseDto, responseDto.getStatusCode());
    }

    @Operation(summary = "Filter trips by status", description = "Finds trips with the specified status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trips found successfully",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "No trips found with given status",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @GetMapping("/trips/filter")
    public ResponseEntity<ResponseDto<List<TripDto>>> getTripsByStatus(@RequestParam TripStatus status){
        List<TripDto> filterTripByStatus = tripService.getTripByStatus(status);
        ResponseDto<List<TripDto>> responseDto = new ResponseDto<>();
        responseDto.setStatusCode(HttpStatus.OK);
        responseDto.setMessage("Trips with status "+status+" found successfully");
        responseDto.setData(filterTripByStatus);
        return new ResponseEntity<>(responseDto, responseDto.getStatusCode());
    }

    @Operation(summary = "Get trips by date range", description = "Finds trips between specified start and end dates")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trips found successfully",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @GetMapping("/trips/daterange")
    public ResponseEntity<ResponseDto<List<TripDto>>> getTripsByDateRange(@RequestParam String start, @RequestParam String end){
        List<TripDto> tripsBetweenDates = tripService.getTripsBetweenDates(start, end);
        ResponseDto<List<TripDto>> responseDto = new ResponseDto<>();
        responseDto.setStatusCode(HttpStatus.OK);
        responseDto.setMessage("Trips between dates found successfully");
        responseDto.setData(tripsBetweenDates);
        return new ResponseEntity<>(responseDto, responseDto.getStatusCode());
    }

    @Operation(summary = "Get trip statistics", description = "Retrieves a summary of all trips including count, min/max/average prices")
    @ApiResponse(responseCode = "200", description = "Trip summary found successfully",
            content = @Content(schema = @Schema(implementation = ResponseDto.class))
    )
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
