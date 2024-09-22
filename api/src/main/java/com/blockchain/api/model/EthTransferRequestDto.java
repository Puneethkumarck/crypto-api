package com.blockchain.api.model;

import lombok.Builder;

@Builder(toBuilder = true)
public record EthTransferRequestDto(String to, String amount) {}
