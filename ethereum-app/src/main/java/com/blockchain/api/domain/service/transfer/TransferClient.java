package com.blockchain.api.domain.service.transfer;

import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;

public interface TransferClient {
  void transfer(String transaction);

  CompletableFuture<BigInteger> getNonce(String address);

  CompletableFuture<BigInteger> getGasPrice();
}
