package com.tripmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TripSummaryDto {
    private long totalTrips;
    private double minPrice;
    private double maxPrice;
    private double averagePrice;
}
