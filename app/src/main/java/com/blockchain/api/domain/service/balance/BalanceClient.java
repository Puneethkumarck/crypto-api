package com.blockchain.api.domain.service.balance;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

public interface BalanceClient {
  CompletableFuture<BigDecimal> getBalance(String address);

  CompletableFuture<BigDecimal> getMinimumBalanceForRentExemption();

  CompletableFuture<BigDecimal> getBalanceLamports(String address);
}
