package com.blockchain.api.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder(toBuilder = true)
public record TransferRequestDto(
    @NotBlank(message = "To address is required") String to,
    @NotNull(message = "Amount is required") Long amount) {}
