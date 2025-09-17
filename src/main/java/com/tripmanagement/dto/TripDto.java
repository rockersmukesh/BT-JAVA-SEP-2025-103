package com.tripmanagement.dto;

import com.tripmanagement.enums.TripStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TripDto {

    private int id;
    private String destination;
    private LocalDate startDate;
    private LocalDate endDate;
    private double price;
    private TripStatus status;
}
