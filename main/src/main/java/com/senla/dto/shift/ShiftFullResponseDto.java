package com.senla.dto.shift;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.senla.dto.cab.CabResponseDto;
import com.senla.dto.city.CityResponseDto;
import com.senla.dto.driver.DriverResponseDto;
import com.senla.dto.rate.RateResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShiftFullResponseDto {

    @JsonProperty("shift_id")
    private Long shiftId;

    @JsonProperty("start_time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime starttime;

    @JsonProperty("end_time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endtime;

    @JsonProperty("rate_info")
    private RateResponseDto rate;


    @JsonProperty("cab_info")
    private CabResponseDto cab;
}
