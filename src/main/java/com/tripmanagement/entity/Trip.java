package com.tripmanagement.entity;

import com.tripmanagement.enums.TripStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "trips")
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Destination cannot be blank")
    @Column(nullable = false)
    private String destination;

    @NotNull(message = "Start date cannot be null")
    @Column(nullable = false)
    private LocalDate startDate;

    @NotNull(message = "End date cannot be null")
    @Column(nullable = false)
    private LocalDate endDate;

    @Positive(message = "Price must be positive")
    @Column(nullable = false)
    private double price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TripStatus status;
}
