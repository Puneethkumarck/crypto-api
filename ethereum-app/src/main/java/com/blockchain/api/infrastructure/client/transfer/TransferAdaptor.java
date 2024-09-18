package com.blockchain.api.infrastructure.client.transfer;

import com.blockchain.api.application.exception.GasPriceRetrievalException;
import com.blockchain.api.application.exception.NonceRetrievalException;
import com.blockchain.api.domain.service.transfer.TransferClient;
import com.blockchain.api.domain.service.transfer.TransferRequest;
import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;

@RequiredArgsConstructor
@Component
@Slf4j
public class TransferAdaptor implements TransferClient {

  private final Web3j rpcClient;

  @Override
  public void transfer(TransferRequest transferRequest) {
    // TODO: Implement transfer logic
  }

  @Override
  public CompletableFuture<BigInteger> getNonce(String address) {
    return rpcClient
        .ethGetTransactionCount(address, DefaultBlockParameterName.LATEST)
        .sendAsync()
        .thenApply(
            nonce -> {
              log.info("Nonce request completed successfully for address: {}", address);
              return nonce.getTransactionCount();
            })
        .exceptionally(
            throwable -> {
              log.error("Error occurred while fetching nonce for address: {}", address, throwable);
              throw NonceRetrievalException.withAddress(address);
            });
  }

  @Override
  public CompletableFuture<BigInteger> getGasPrice() {
    return rpcClient
        .ethGasPrice()
        .sendAsync()
        .thenApply(
            gasPrice -> {
              log.info("Gas price request completed successfully");
              return gasPrice.getGasPrice();
            })
        .exceptionally(
            throwable -> {
              log.error("Error occurred while fetching gas price", throwable);
              throw GasPriceRetrievalException.build(throwable);
            });
  }
}
