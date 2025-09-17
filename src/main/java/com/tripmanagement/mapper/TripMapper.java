package com.tripmanagement.mapper;

import com.tripmanagement.dto.TripDto;
import com.tripmanagement.entity.Trip;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface TripMapper {
    Trip mapTripDtoToTrip(TripDto tripDto);
    TripDto mapTripToTripDto(Trip trip);
}
