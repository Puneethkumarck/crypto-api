package com.blockchain.api.domain.service.transfer;

import java.util.concurrent.CompletableFuture;
import org.p2p.solanaj.core.Account;
import org.p2p.solanaj.ws.listeners.NotificationEventListener;

public interface TransferClient {
  CompletableFuture<String> transferFunds(
      Account senderAccount,
      String recipientAddress,
      long lamports,
      NotificationEventListener listener);
}
