package com.blockchain.api.infrastructure.client.airdrop;

import static com.blockchain.api.application.validator.SolanaAddressValidator.isValidAddressOrThrow;

import com.blockchain.api.application.exception.RequestAirDropException;
import com.blockchain.api.domain.service.airdrop.AirDropClient;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.p2p.solanaj.core.PublicKey;
import org.p2p.solanaj.rpc.RpcClient;
import org.p2p.solanaj.rpc.RpcException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AirDropAdaptor implements AirDropClient {

  private final RpcClient solanaNodeClient;

  private final Executor taskExecutor;

  @Override
  @Async("taskExecutor")
  public CompletableFuture<String> requestAirDrop(String address, long amount) {
    return CompletableFuture.supplyAsync(() -> isValidAddressOrThrow(address), taskExecutor)
        .thenCompose(
            validAddress ->
                CompletableFuture.supplyAsync(
                    () -> sendAirDrop(validAddress, amount), taskExecutor))
        .thenApply(
            result -> {
              log.info("Airdrop request completed successfully for address: {}", address);
              return result;
            });
  }

  @SneakyThrows
  private String sendAirDrop(String address, long amount) {
    try {
      log.info("Sending airdrop of {} lamports to address: {}", amount, address);
      return solanaNodeClient.getApi().requestAirdrop(PublicKey.valueOf(address), amount);
    } catch (RpcException e) {
      log.error("Error while sending airdrop to address: {}, amount: {}", address, amount, e);
      throw RequestAirDropException.withAddress("Failed to send airdrop to address: " + address, e);
    }
  }
}
