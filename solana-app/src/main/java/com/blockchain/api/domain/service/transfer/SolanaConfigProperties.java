package com.blockchain.api.domain.service.transfer;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "solana.secret")
public record SolanaConfigProperties(String keypair) {}
