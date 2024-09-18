package com.blockchain.api.infrastructure.client.transfer;

import com.blockchain.api.application.exception.SolanaTransactionException;
import com.blockchain.api.domain.service.transfer.TransferClient;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.p2p.solanaj.core.Account;
import org.p2p.solanaj.core.PublicKey;
import org.p2p.solanaj.core.Transaction;
import org.p2p.solanaj.programs.SystemProgram;
import org.p2p.solanaj.rpc.RpcClient;
import org.p2p.solanaj.rpc.RpcException;
import org.p2p.solanaj.ws.SubscriptionWebSocketClient;
import org.p2p.solanaj.ws.listeners.NotificationEventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransferAdaptor implements TransferClient {

  private final RpcClient solanaNodeClient;
  private final Executor taskExecutor;

  @Override
  public CompletableFuture<String> transferFunds(
      Account senderAccount,
      String recipientAddress,
      long lamports,
      String latestBlockhash,
      NotificationEventListener listener) {
    return CompletableFuture.supplyAsync(
        () -> {
          try {
            PublicKey recipientPublicKey = new PublicKey(recipientAddress);

            Transaction transaction =
                new Transaction()
                    .addInstruction(
                        SystemProgram.transfer(
                            senderAccount.getPublicKey(), recipientPublicKey, lamports));

            List<Account> signers = Collections.singletonList(senderAccount);

            log.info("Sending transaction...");
            String transactionSignature =
                solanaNodeClient.getApi().sendTransaction(transaction, signers, latestBlockhash);
            log.info("Transaction sent successfully. Signature: {}", transactionSignature);
            SubscriptionWebSocketClient subClient =
                SubscriptionWebSocketClient.getInstance(solanaNodeClient.getEndpoint());
            subClient.signatureSubscribe(transactionSignature, listener);
            return transactionSignature;
          } catch (RpcException e) {
            log.error("RPC Exception during transaction: {}", e.getMessage(), e);
            throw SolanaTransactionException.withRpcException(recipientAddress, e);
          } catch (Exception e) {
            log.error("Transaction initiation failed: {}", e.getMessage(), e);
            throw SolanaTransactionException.withException(
                "Error initiating transfer for recipient address: %s".formatted(recipientAddress),
                e);
          }
        },
        taskExecutor);
  }
}
