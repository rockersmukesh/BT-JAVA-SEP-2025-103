package com.tripmanagement.service;

import com.tripmanagement.dto.TripDto;
import com.tripmanagement.dto.TripSummaryDto;
import com.tripmanagement.entity.Trip;
import com.tripmanagement.enums.TripStatus;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TripService {
    TripDto saveTrip(TripDto tripDto);

    List<TripDto> getAllTrips();

    Page<TripDto> getPaginatedTrips(int page,int size,String sort);

    TripDto getTripById(String tripId);

    TripDto updateTripById(int id, Trip trip);

    TripDto deleteTripById(int id);

    List<TripDto> getTripsByDestination(String destination);

    List<TripDto> getTripByStatus(TripStatus status);

    List<TripDto> getTripsBetweenDates(String startDate, String endDate);

    TripSummaryDto getTripSummary();
}
