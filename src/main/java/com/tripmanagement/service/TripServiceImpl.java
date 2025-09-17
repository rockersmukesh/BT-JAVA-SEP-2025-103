package com.tripmanagement.service;

import com.tripmanagement.dto.TripDto;
import com.tripmanagement.dto.TripSummaryDto;
import com.tripmanagement.entity.Trip;
import com.tripmanagement.enums.TripStatus;
import com.tripmanagement.exception.TripAlreadyExistsException;
import com.tripmanagement.exception.TripNotFoundException;
import com.tripmanagement.mapper.TripMapper;
import com.tripmanagement.repository.TripRepository;
import com.tripmanagement.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TripServiceImpl implements TripService {

    private final TripRepository tripRepository;
    private final TripMapper tripMapper;

    @Override
    public TripDto saveTrip(TripDto tripDto) {
        Trip trip = tripMapper.mapTripDtoToTrip(tripDto);
        List<Trip> existingTrips = tripRepository.findByDestinationContainingIgnoreCase(trip.getDestination());

        for (Trip existingTrip : existingTrips) {
            if (existingTrip.getDestination().equalsIgnoreCase(trip.getDestination()) &&
                    ((trip.getStartDate().isBefore(existingTrip.getEndDate()) || trip.getStartDate().isEqual(existingTrip.getEndDate())) &&
                            (trip.getEndDate().isAfter(existingTrip.getStartDate()) || trip.getEndDate().isEqual(existingTrip.getStartDate())))) {

                throw new TripAlreadyExistsException(
                        "Trip to " + trip.getDestination() + " with overlapping dates already exists",
                        HttpStatus.CONFLICT
                );
            }
        }

        Trip savedTrip = tripRepository.save(trip);
        return tripMapper.mapTripToTripDto(savedTrip);
    }

    @Override
    public List<TripDto> getAllTrips() {
        List<Trip> trips = tripRepository.findAll();

        List<TripDto> tripDtos = trips
                .stream()
                .map(trip -> tripMapper.mapTripToTripDto(trip))
                .toList();

        return tripDtos;
    }

    @Override
    public Page<TripDto> getPaginatedTrips(int page, int size, String sort) {
        Sort sorting = Sort.unsorted();
        if(sort != null && !sort.isEmpty()) {
            String[] sortParams = sort.split(",");
            String sortField = sortParams[0];
            Sort.Direction direction = sortParams.length > 1 && sortParams[1].equalsIgnoreCase("desc")? Sort.Direction.DESC : Sort.Direction.ASC;
            sorting = Sort.by(direction, sortField);
        }

        Pageable pageable = PageRequest.of(page,size,sorting);
        Page<Trip> tripsPage = tripRepository.findAll(pageable);
        return tripsPage.map(tripMapper::mapTripToTripDto);
    }

    @Override
    public TripDto getTripById(String tripId) {
        if (Pattern.matches("^\\d+$", tripId)) {
            int tripIdInt = Integer.parseInt(tripId);
            Trip trip = tripRepository
                    .findById(tripIdInt)
                    .orElseThrow(() -> new TripNotFoundException(
                            "Trip Not Found",
                            HttpStatus.valueOf(Integer.parseInt("400"))
                    ));

            return tripMapper.mapTripToTripDto(trip);
        }else {
            throw new TripNotFoundException("Incorrect Trip Id", HttpStatus.valueOf(Integer.parseInt("400")));
        }
    }

    @Override
    public TripDto updateTripById(int id, Trip trip) {
        Trip existingTrip = tripRepository
                .findById(id)
                .orElseThrow(() -> new TripNotFoundException(
                        "Trip with ID " + id + " not found",
                        HttpStatus.NOT_FOUND
                ));

        if(trip.getDestination() != null && !trip.getDestination().isEmpty()) {
            existingTrip.setDestination(trip.getDestination());
        }
        if(trip.getStartDate() != null) {
            existingTrip.setStartDate(trip.getStartDate());
        }
        if(trip.getEndDate() != null) {
            existingTrip.setEndDate(trip.getEndDate());
        }
        if(trip.getPrice() > 0) {
            existingTrip.setPrice(trip.getPrice());
        }
        if(trip.getStatus() != null) {
            existingTrip.setStatus(trip.getStatus());
        }

        Trip updatedTrip = tripRepository.save(existingTrip);
        return tripMapper.mapTripToTripDto(updatedTrip);
    }

    @Override
    public TripDto deleteTripById(int id) {
        Trip trip = tripRepository
                .findById(id)
                .orElseThrow(() -> new TripNotFoundException(
                        "Trip with ID "+ id + " not found",
                        HttpStatus.NOT_FOUND
        ));
        TripDto tripDto = tripMapper.mapTripToTripDto(trip);
        tripRepository.delete(trip);
        return tripDto;
    }

    @Override
    public List<TripDto> getTripsByDestination(String destination) {
        List<Trip> trips = tripRepository.findByDestinationContainingIgnoreCase(destination);
        if(trips.isEmpty()){
            throw new TripNotFoundException(
                    "No trips found with destination " + destination,
                    HttpStatus.NOT_FOUND
            );
        }

        return trips.stream()
                .map(tripMapper::mapTripToTripDto)
                .toList();
    }

    @Override
    public List<TripDto> getTripByStatus(TripStatus status) {
        List<Trip> trip = tripRepository.findByStatus(status);
        if(trip.isEmpty()){
            throw new TripNotFoundException(
                    "No trips found with status " + status,
                    HttpStatus.NOT_FOUND
            );
        }

        return trip.stream()
                .map(tripMapper::mapTripToTripDto)
                .toList();
    }

    public List<TripDto> getTripsBetweenDates(String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        List<Trip> trips = tripRepository.findByStartDateGreaterThanEqualAndEndDateLessThan(start, end);
        return trips.stream()
                .map(tripMapper::mapTripToTripDto)
                .collect(Collectors.toList());
    }

    public TripSummaryDto getTripSummary() {
        List<Trip> allTrips = tripRepository.findAll();
        TripSummaryDto tripSummary = new TripSummaryDto();
        tripSummary.setTotalTrips(allTrips.size());

        if(!allTrips.isEmpty()){
            DoubleSummaryStatistics stats = allTrips.stream()
                    .mapToDouble(Trip::getPrice)
                    .summaryStatistics();

            tripSummary.setMinPrice(stats.getMin());
            tripSummary.setMaxPrice(stats.getMax());
            tripSummary.setAveragePrice(stats.getAverage());
        } else{
            tripSummary.setMinPrice(0.0);
            tripSummary.setMaxPrice(0.0);
            tripSummary.setAveragePrice(0.0);
        }
        return tripSummary;
    }
}
