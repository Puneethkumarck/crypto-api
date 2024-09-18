package com.blockchain.api.infrastructure.client.balance;

import com.blockchain.api.domain.service.balance.BalanceClient;
import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;

@Slf4j
@Component
@RequiredArgsConstructor
public class BalanceAdaptor implements BalanceClient {

  private final Web3j rpcClient;

  @Override
  public CompletableFuture<BigInteger> getBalance(String address) {
    return rpcClient
        .ethGetBalance(address, DefaultBlockParameterName.LATEST)
        .sendAsync()
        .thenApply(
            balance -> {
              log.info("Balance request completed successfully for address: {}", address);
              return balance.getBalance();
            });
  }
}
