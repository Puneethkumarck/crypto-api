package com.blockchain.api.domain.service.airdrop;

import java.util.concurrent.CompletableFuture;

public interface AirDropClient {
  CompletableFuture<String> requestAirDrop(String address, long amount);
}
