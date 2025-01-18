package com.senla.dto.ride;

import com.senla.types.RideStatus;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RideFilterDto {

    @Positive
    private BigDecimal rideTipMin;
    @Positive
    private BigDecimal rideTipMax;

    @Positive
    private BigDecimal rideDistanceExpectedMin;
    @Positive
    private BigDecimal rideDistanceExpectedMax;

    @Positive
    private BigDecimal rideDistanceActualMin;
    @Positive
    private BigDecimal rideDistanceActualMax;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @PastOrPresent
    private LocalDateTime createdAtMin;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @PastOrPresent
    private LocalDateTime createdAtMax;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @PastOrPresent
    private LocalDateTime rideAcceptedAtMin;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @PastOrPresent
    private LocalDateTime rideAcceptedAtMax;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @PastOrPresent
    private LocalDateTime rideDriverWaitingMin;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @PastOrPresent
    private LocalDateTime rideDriverWaitingMax;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @PastOrPresent
    private LocalDateTime rideStartTimeMin;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @PastOrPresent
    private LocalDateTime rideStartTimeMax;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @PastOrPresent
    private LocalDateTime rideEndTimeMin;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @PastOrPresent
    private LocalDateTime rideEndTimeMax;

    private RideStatus rideStatus;

    @Positive
    private BigDecimal rideActualPriceMin;
    @Positive
    private BigDecimal rideActualPriceMax;
}
