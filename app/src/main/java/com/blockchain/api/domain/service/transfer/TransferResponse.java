package com.blockchain.api.domain.service.transfer;

import lombok.Builder;

@Builder(toBuilder = true)
public record TransferResponse(String transactionHash) {}
