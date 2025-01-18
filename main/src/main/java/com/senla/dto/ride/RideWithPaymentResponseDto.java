package com.senla.dto.ride;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.senla.dto.customerrating.CustomerRatingResponseDto;
import com.senla.dto.payment.PaymentResponseDto;
import com.senla.dto.promocode.PromocodeResponseDto;
import com.senla.dto.rate.RateResponseDto;
import com.senla.dto.shift.ShiftResponseIdDto;
import com.senla.dto.user.UserResponseIdDto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.n52.jackson.datatype.jts.GeometryDeserializer;
import org.n52.jackson.datatype.jts.GeometrySerializer;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RideWithPaymentResponseDto {

    @NotNull
    @JsonProperty("id")
    private Long rideId;


    @JsonProperty("ride_tip")
    private BigDecimal rideTip;



    @JsonProperty("start_point")
    @JsonSerialize(using = GeometrySerializer.class)
    @JsonDeserialize(using = GeometryDeserializer.class)
    private Point startPoint;

    @JsonProperty("end_point")
    @JsonSerialize(using = GeometrySerializer.class)
    @JsonDeserialize(using = GeometryDeserializer.class)
    private Point endPoint;



    @JsonProperty("created_at")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonProperty("ride_accepted_at")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime rideAcceptedAt;

    @JsonProperty("ride_driver_waiting")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime rideDriverWaiting;

    @JsonProperty("ride_start_time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime rideStartTime;

    @JsonProperty("ride_end_time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime rideEndTime;



    @JsonProperty("ride_expected_price")
    private BigDecimal rideExpectedPrice;

    @JsonProperty("ride_actual_price")
    private BigDecimal rideActualPrice;

    @JsonProperty("ride_distance_expected_meters")
    private BigDecimal rideDistanceExpectedMeters;

    @JsonProperty("ride_distance_actual_meters")
    private BigDecimal rideDistanceActualMeters;


    private String status;


    @JsonProperty("payment_info")
    private PaymentResponseDto payment;


}
