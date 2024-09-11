package com.blockchain.api.model;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
public record BalanceDto(String address, String balance) {}
