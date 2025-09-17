package com.tripmanagement.service;

import com.tripmanagement.dto.TripDto;
import com.tripmanagement.dto.TripSummaryDto;
import com.tripmanagement.entity.Trip;
import com.tripmanagement.enums.TripStatus;
import com.tripmanagement.exception.TripNotFoundException;
import com.tripmanagement.mapper.TripMapper;
import com.tripmanagement.repository.TripRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TripServiceImplTest {

    @Mock
    private TripRepository tripRepository;

    @Mock
    private TripMapper tripMapper;

    @InjectMocks
    private TripServiceImpl tripService;

    private Trip trip;
    private TripDto tripDto;
    private List<Trip> tripList;
    private List<TripDto> tripDtoList;

    @BeforeEach
    void setUp() {
        trip = new Trip(1, "Paris", LocalDate.of(2025, 6, 1),
                LocalDate.of(2025, 6, 10), 1500.0, TripStatus.PLANNED);

        tripDto = new TripDto(1, "Paris", LocalDate.of(2025, 6, 1),
                LocalDate.of(2025, 6, 10), 1500.0, TripStatus.PLANNED);

        tripList = Arrays.asList(
                trip,
                new Trip(2, "London", LocalDate.of(2025, 7, 1),
                        LocalDate.of(2025, 7, 10), 2000.0, TripStatus.PLANNED)
        );

        tripDtoList = Arrays.asList(
                tripDto,
                new TripDto(2, "London", LocalDate.of(2025, 7, 1),
                        LocalDate.of(2025, 7, 10), 2000.0, TripStatus.PLANNED)
        );
    }

    @Test
    void saveTrip_ShouldReturnSavedTripDto() {
        when(tripMapper.mapTripDtoToTrip(tripDto)).thenReturn(trip);
        when(tripRepository.save(trip)).thenReturn(trip);
        when(tripMapper.mapTripToTripDto(trip)).thenReturn(tripDto);

        TripDto result = tripService.saveTrip(tripDto);

        assertNotNull(result);
        assertEquals(tripDto, result);
        verify(tripRepository).save(trip);
        System.out.println("saved successfully");
    }

    @Test
    void getAllTrips_ShouldReturnAllTrips() {
        when(tripRepository.findAll()).thenReturn(tripList);
        when(tripMapper.mapTripToTripDto(any(Trip.class)))
                .thenReturn(tripDtoList.get(0), tripDtoList.get(1));

        List<TripDto> result = tripService.getAllTrips();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(tripRepository).findAll();
        System.out.println(result);
    }

    @Test
    void getPaginatedTrips_ShouldReturnPageOfTrips(){
        Pageable pageable = PageRequest.of(0, 10, Sort.unsorted());
        Page<Trip> tripPage = new PageImpl<>(tripList, pageable, tripList.size());

        when(tripRepository.findAll(any(Pageable.class))).thenReturn((tripPage));
        when(tripMapper.mapTripToTripDto(any(Trip.class)))
                .thenReturn(tripDtoList.get(0), tripDtoList.get(1));

        Page<TripDto> result = tripService.getPaginatedTrips(0, 10, null);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        verify(tripRepository).findAll(any(Pageable.class));
    }

    @Test
    void getTripById_WithValidId_ShouldReturnTrip() {
        when(tripRepository.findById(1)).thenReturn(Optional.of(trip));
        when(tripMapper.mapTripToTripDto(trip)).thenReturn(tripDto);

        TripDto result = tripService.getTripById("1");

        assertNotNull(result);
        assertEquals(tripDto, result);
        verify(tripRepository).findById(1);
    }

    @Test
    void getTripById_WithInvalidId_ShouldThrowException(){
        when(tripRepository.findById(99)).thenReturn(Optional.empty());
        assertThrows(TripNotFoundException.class, ()-> tripService.getTripById("99"));
        verify(tripRepository).findById(99);
    }

    @Test
    void updateTripById_WithValidId_ShouldReturUpdatedTrip(){
        Trip updatedTrip = new Trip(1,"Update Paris",LocalDate.of(2025,6,1),
                LocalDate.of(2025,6,10), 1600.0, TripStatus.PLANNED);
        TripDto updatedTripDto = new TripDto(1, "Updated Paris", LocalDate.of(2025, 6, 1),
                LocalDate.of(2025, 6, 10), 1600.0, TripStatus.PLANNED);

        when(tripRepository.findById(1)).thenReturn(Optional.of(trip));
        when(tripRepository.save(any(Trip.class))).thenReturn(updatedTrip);
        when(tripMapper.mapTripToTripDto(updatedTrip)).thenReturn(updatedTripDto);

        TripDto result = tripService.updateTripById(1, updatedTrip);

        assertNotNull(result);
        assertEquals(updatedTripDto, result);
        verify(tripRepository).findById(1);
        verify(tripRepository).save(any(Trip.class));
    }

    @Test
    void deleteTripById_WithValidId_ShouldReturnDeletedTrip(){
        when(tripRepository.findById(1)).thenReturn(Optional.of(trip));
        when(tripMapper.mapTripToTripDto(trip)).thenReturn(tripDto);

        TripDto result = tripService.deleteTripById(1);

        assertNotNull(result);
        assertEquals(tripDto, result);
        verify(tripRepository).findById(1);
        verify(tripRepository).delete(trip);
    }

    @Test
    void getTripsByDestination_ShouldReturnMatchingTrips() {
        when(tripRepository.findByDestinationContainingIgnoreCase("Paris")).thenReturn(Collections.singletonList(trip));
        when(tripMapper.mapTripToTripDto(trip)).thenReturn(tripDto);

        List<TripDto> result = tripService.getTripsByDestination("Paris");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(tripDto, result.get(0));
        verify(tripRepository).findByDestinationContainingIgnoreCase("Paris");
    }

    @Test
    void getTripByStatus_ShouldReturnMatchingTrips() {
        when(tripRepository.findByStatus(TripStatus.PLANNED)).thenReturn(tripList);
        when(tripMapper.mapTripToTripDto(any(Trip.class)))
                .thenReturn(tripDtoList.get(0), tripDtoList.get(1));

        List<TripDto> result = tripService.getTripByStatus(TripStatus.PLANNED);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(tripRepository).findByStatus(TripStatus.PLANNED);
    }

    @Test
    void getTripsBetweenDates_ShouldReturnMatchingTrips() {
        LocalDate start = LocalDate.of(2025, 6, 1);
        LocalDate end = LocalDate.of(2025, 7, 30);

        when(tripRepository.findByStartDateGreaterThanEqualAndEndDateLessThan(start, end))
                .thenReturn(tripList);
        when(tripMapper.mapTripToTripDto(any(Trip.class)))
                .thenReturn(tripDtoList.get(0), tripDtoList.get(1));

        List<TripDto> result = tripService.getTripsBetweenDates("2025-06-01", "2025-07-30");

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(tripRepository).findByStartDateGreaterThanEqualAndEndDateLessThan(start, end);
    }

    @Test
    void getTripSummary_ShouldReturnCorrectSummary() {
        when(tripRepository.findAll()).thenReturn(tripList);

        TripSummaryDto result = tripService.getTripSummary();

        assertNotNull(result);
        assertEquals(2, result.getTotalTrips());
        assertEquals(1500.0, result.getMinPrice());
        assertEquals(2000.0, result.getMaxPrice());
        assertEquals(1750.0, result.getAveragePrice());
        verify(tripRepository).findAll();
    }

}