package com.blockchain.api.infrastructure.client.transfer;

import com.blockchain.api.application.exception.SolanaTransactionException;
import com.blockchain.api.application.validator.SolanaAddressValidator;
import com.blockchain.api.domain.service.airdrop.AirDropClient;
import com.blockchain.api.domain.service.balance.BalanceClient;
import com.blockchain.api.domain.service.blockhash.BlockhashClient;
import com.blockchain.api.domain.service.transfer.TransferClient;
import java.math.BigDecimal;
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
  private final BalanceClient balanceClient;
  private final AirDropClient airDropClient;
  private final Executor taskExecutor;
  private final BlockhashClient blockhashClient;

  @Override
  public CompletableFuture<String> transferFunds(
      Account senderAccount,
      String recipientAddress,
      long lamports,
      NotificationEventListener listener) {
    return CompletableFuture.supplyAsync(
        () -> {
          try {
            SolanaAddressValidator.isValidAddressOrThrow(senderAccount.getPublicKey().toBase58());
            SolanaAddressValidator.isValidAddressOrThrow(recipientAddress);

            // Ensure the balance is sufficient
            BigDecimal balance = ensureSufficientBalance(senderAccount, lamports).get();
            log.info("Sufficient balance found: {} lamports", balance);

            // Initiate the transfer process
            return initiateTransfer(senderAccount, recipientAddress, lamports, listener);
          } catch (Exception e) {
            log.error("Transfer failed: {}", e.getMessage());
            throw new SolanaTransactionException("Transaction failed", e);
          }
        },
        taskExecutor);
  }

  private CompletableFuture<BigDecimal> ensureSufficientBalance(
      Account senderAccount, long lamports) {
    return CompletableFuture.supplyAsync(
        () -> {
          try {
            BigDecimal balance =
                balanceClient.getBalance(senderAccount.getPublicKey().toBase58(), true).get();
            BigDecimal minRentExemption = balanceClient.getMinimumBalanceForRentExemption().get();
            BigDecimal totalRequired = BigDecimal.valueOf(lamports).add(minRentExemption);

            if (balance.compareTo(totalRequired) < 0) {
              log.info(
                  "Insufficient balance. Airdropping 1 SOL to {}",
                  senderAccount.getPublicKey().toBase58());
              airDropClient
                  .requestAirDrop(senderAccount.getPublicKey().toBase58(), 1_000_000_000L)
                  .get(); // 1 SOL
              log.info("Airdrop successful.");
              return balanceClient.getBalance(senderAccount.getPublicKey().toBase58(), true).get();
            }

            return balance;
          } catch (Exception e) {
            log.error("Error ensuring sufficient balance: {}", e.getMessage());
            throw new SolanaTransactionException("Failed to ensure sufficient balance", e);
          }
        },
        taskExecutor);
  }

  private String initiateTransfer(
      Account senderAccount,
      String recipientAddress,
      long lamports,
      NotificationEventListener listener) {
    try {
      PublicKey senderPublicKey = senderAccount.getPublicKey();
      PublicKey recipientPublicKey = new PublicKey(recipientAddress);

      String recentBlockhash = blockhashClient.getLatestBlockhash().get();

      Transaction transaction = new Transaction();
      transaction.addInstruction(
          SystemProgram.transfer(senderPublicKey, recipientPublicKey, lamports));

      List<Account> signers = Collections.singletonList(senderAccount);

      log.info("Sending transaction...");
      String transactionSignature =
          solanaNodeClient.getApi().sendTransaction(transaction, signers, recentBlockhash);
      log.info("Transaction sent successfully. Signature: {}", transactionSignature);

      SubscriptionWebSocketClient subClient =
          SubscriptionWebSocketClient.getInstance(solanaNodeClient.getEndpoint());
      subClient.signatureSubscribe(transactionSignature, listener);

      return transactionSignature;
    } catch (RpcException e) {
      log.error("Error during transaction: {}", e.getMessage());
      throw SolanaTransactionException.withRpcException(
          "Failed to send transaction for recipent address: %s".formatted(recipientAddress), e);
    } catch (Exception e) {
      log.error("Transaction initiation failed {}", e.getMessage());
      throw SolanaTransactionException.withException(
          "Error initiating transfer  for recipent address: %s:".formatted(recipientAddress), e);
    }
  }
}
