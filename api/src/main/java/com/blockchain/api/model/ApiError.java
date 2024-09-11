package com.blockchain.api.model;

import lombok.Builder;

@Builder(toBuilder = true)
public record ApiError(String status, String message) {}
