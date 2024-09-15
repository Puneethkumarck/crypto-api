package com.blockchain.api.domain.service.transfer;

import static com.blockchain.api.application.validator.SolanaAddressValidator.isValidAddressOrThrow;

import com.blockchain.api.domain.service.balance.BalanceService;
import com.blockchain.api.domain.service.blockhash.BlockHashService;
import com.blockchain.api.domain.service.blockhash.BlockhashClient;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.p2p.solanaj.core.Account;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransferService {

  private final TransferClient transferClient;

  private final BlockHashService blockHashService;

  private final AccountLoaderService accountLoaderService;

  private final LogWebSocketNotification listener;

  private final BalanceService balanceService;

  @SneakyThrows
  public void transfer(TransferRequest request) {
    log.info("Transferring {} lamports to destination address {}", request.amount(), request.to());

    var senderAccount = accountLoaderService.loadSenderKeypair();
    isValidAddressOrThrow(request.to());
    ensureSufficientBalanceForTransfer(senderAccount, request.to(), request.amount());
    var latestBlockhash = getLatestBlockhash();
    transferClient
        .transferFunds(senderAccount, request.to(), request.amount(), latestBlockhash, listener)
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

  private void ensureSufficientBalanceForTransfer(
      Account senderAccount, String recipientAccount, long lamports) {
    balanceService.ensureSufficientBalanceForTransfer(senderAccount, recipientAccount, lamports);
  }

  private String getLatestBlockhash() {
    return blockHashService.getBlockhash();
  }
}
