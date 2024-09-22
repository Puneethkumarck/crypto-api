package com.blockchain.api.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder(toBuilder = true)
public record EthTransferRequestDto(
    @NotBlank(message = "To address is required") String to,
    @NotBlank(message = "Amount is required") String amount) {}
