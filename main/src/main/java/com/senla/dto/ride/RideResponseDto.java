package com.senla.dto.ride;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.locationtech.jts.geom.Point;
import org.n52.jackson.datatype.jts.GeometryDeserializer;
import org.n52.jackson.datatype.jts.GeometrySerializer;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RideResponseDto {

    @NotNull
    @JsonProperty("id")
    private Long rideId;


    @JsonProperty("ride_tip")
    private BigDecimal rideTip;



    @JsonProperty("start_point_geo")
    @JsonSerialize(using = GeometrySerializer.class)
    @JsonDeserialize(using = GeometryDeserializer.class)
    private Point startPoint;


    @JsonProperty("end_point_geo")
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

    private String status;

//
//

    @JsonProperty("ride_expected_price")
    private BigDecimal rideExpectedPrice;

    @JsonProperty("ride_actual_price")
    private BigDecimal rideActualPrice;

    @JsonProperty("ride_distance_expected_meters")
    private BigDecimal rideDistanceExpectedMeters;

    @JsonProperty("ride_distance_actual_meters")
    private BigDecimal rideDistanceActualMeters;

}
