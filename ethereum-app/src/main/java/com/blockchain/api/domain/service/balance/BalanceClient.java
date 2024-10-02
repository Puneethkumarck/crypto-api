package com.blockchain.api.domain.service.balance;

import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;

public interface BalanceClient {
  CompletableFuture<BigInteger> getBalance(String address);

  CompletableFuture<BigInteger> getNonEthBalance(String address);
}
