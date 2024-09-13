package com.blockchain.api.domain.service.transfer;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.p2p.solanaj.ws.listeners.NotificationEventListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransferService {

  private final TransferClient transferClient;

  private final AccountLoaderService accountLoaderService;

  public TransferResponse transfer(TransferRequest request) {
    log.info("Transferring {} lamports to destination address {}", request.amount(), request.to());

    NotificationEventListener listener = data -> log.info("Notification received: {}", data);

    transferClient
        .transferFunds(
            accountLoaderService.loadSenderKeypair(), request.to(), request.amount(), listener)
        .orTimeout(10, TimeUnit.SECONDS)
        .thenApply(
            transactionSignature -> {
              log.info(
                  "Transfer completed successfully. Transaction signature: {}",
                  transactionSignature);
              return TransferResponse.builder().transactionHash(transactionSignature).build();
            })
        .exceptionally(
            ex -> {
              log.error("Transfer failed", ex);
              return null;
            });
    return null;
  }
}
