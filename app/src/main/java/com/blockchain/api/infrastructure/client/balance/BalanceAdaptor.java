package com.blockchain.api.infrastructure.client.balance;

import static com.blockchain.api.application.validator.SolanaAddressValidator.isValidAddressOrThrow;
import static com.blockchain.api.infrastructure.common.SolanaUtil.lamportsToSol;

import com.blockchain.api.domain.service.balance.BalanceClient;
import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.p2p.solanaj.core.PublicKey;
import org.p2p.solanaj.rpc.RpcClient;
import org.p2p.solanaj.rpc.RpcException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BalanceAdaptor implements BalanceClient {

  private final RpcClient solanaNodeClient;

  private final Executor taskExecutor;

  @Override
  public CompletableFuture<BigDecimal> getBalance(String address, boolean isLamports) {
    return CompletableFuture.supplyAsync(() -> isValidAddressOrThrow(address), taskExecutor)
        .thenCompose(
            validAddress ->
                CompletableFuture.supplyAsync(
                    () -> fetchBalance(address, isLamports), taskExecutor))
        .thenApply(
            result -> {
              log.info("Balance request completed successfully for address: {}", address);
              return result;
            });
  }

  @Override
  public CompletableFuture<BigDecimal> getMinimumBalanceForRentExemption() {
    return CompletableFuture.supplyAsync(this::fetchMinimumBalanceForRentExemption, taskExecutor)
        .thenApply(
            result -> {
              log.info("Minimum balance for rent exemption request completed successfully");
              return result;
            });
  }

  private BigDecimal fetchBalance(String address, Boolean isLamports) {
    try {
      var balanceResult = solanaNodeClient.getApi().getBalance(PublicKey.valueOf(address));
      log.info("Received balance in lamports: {}", balanceResult);
      return isLamports ? new BigDecimal(balanceResult) : lamportsToSol(balanceResult);
    } catch (RpcException e) {
      log.error("RPC error while fetching balance for address: {}", address, e);
      return BigDecimal.ZERO;
    }
  }

  private BigDecimal fetchMinimumBalanceForRentExemption() {
    try {
      var minimumBalance = solanaNodeClient.getApi().getMinimumBalanceForRentExemption(0);
      log.info("Received minimum balance for rent exemption in lamports: {}", minimumBalance);
      return lamportsToSol(minimumBalance);
    } catch (RpcException e) {
      log.error("RPC error while fetching minimum balance for rent exemption", e);
      return BigDecimal.ZERO;
    }
  }
}
