package com.blockchain.api.model;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Builder(toBuilder = true)
@Jacksonized
public record TransferResponseDto(String transactionHash) {}
