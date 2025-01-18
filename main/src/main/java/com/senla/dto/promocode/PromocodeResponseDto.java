package com.senla.dto.promocode;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromocodeResponseDto {

    @JsonProperty("id")
    private Long promocodeId;

    @JsonProperty("code")
    private String promocodeCode;

    @JsonProperty("discount")
    private BigDecimal discountValue;

    @JsonProperty("start_date")
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate startDate;

    @JsonProperty("end_date")
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate endDate;

    private String description;
}
