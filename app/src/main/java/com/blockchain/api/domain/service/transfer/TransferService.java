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

  public void transfer(TransferRequest request) {
    log.info("Transferring {} lamports to destination address {}", request.amount(), request.to());

    // Create a NotificationEventListener to handle transaction confirmation
    NotificationEventListener listener = data -> log.info("Notification received: {}", data);

    // Perform the transfer using the TransferClient
    transferClient
        .transferFunds(
            accountLoaderService.loadSenderKeypair(), request.to(), request.amount(), listener)
        .orTimeout(10, TimeUnit.SECONDS)
        .thenAccept(
            transactionSignature ->
                log.info(
                    "Transfer completed successfully. Transaction signature: {}",
                    transactionSignature))
        .exceptionally(
            ex -> {
              log.error("Transfer failed", ex);
              return null;
            });
  }
}
