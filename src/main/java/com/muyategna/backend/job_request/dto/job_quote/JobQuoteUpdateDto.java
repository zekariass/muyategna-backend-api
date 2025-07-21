package com.muyategna.backend.job_request.dto.job_quote;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobQuoteUpdateDto {
    @NotNull(message = "ID cannot be null")
    private Long id;
    private Long claimId;
    private BigDecimal proposedPrice;
    private LocalDateTime proposedStartDate;

    @NotBlank(message = "Message is required")
    private String message;
}
