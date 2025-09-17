package com.tripmanagement.repository;

import com.tripmanagement.entity.Trip;
import com.tripmanagement.enums.TripStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip,Integer> {

    List<Trip> findByDestinationContainingIgnoreCase(String destination);

    List<Trip> findByStatus(TripStatus status);

    List<Trip> findByStartDateGreaterThanEqualAndEndDateLessThan(LocalDate startDateIsGreaterThan, LocalDate endDateIsLessThan);

    boolean findByDestinationAndStartDateAndEndDateAndPrice(String destination, LocalDate startDate, LocalDate endDate, double price);
}
