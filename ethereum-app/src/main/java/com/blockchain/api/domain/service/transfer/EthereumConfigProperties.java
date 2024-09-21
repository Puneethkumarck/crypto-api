package com.blockchain.api.domain.service.transfer;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ethereum.secret")
public record EthereumConfigProperties(String keypair) {}
