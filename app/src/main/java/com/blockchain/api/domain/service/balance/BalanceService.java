package com.blockchain.api.domain.service.balance;

import com.blockchain.api.domain.service.airdrop.AirDropClient;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.p2p.solanaj.core.Account;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BalanceService {

  private final BalanceClient balanceClient;
  private final AirDropClient airDropClient;

  public BigDecimal getSolanaBalance(String address) {
    return balanceClient.getBalance(address, false).join();
  }

  public BigDecimal getMinimumBalanceForRentExemption() {
    return balanceClient.getMinimumBalanceForRentExemption().join();
  }

  public void ensureSufficientBalanceForTransfer(
      Account senderAccount, String recipientAccount, long lamports) {
    ensureSufficientBalanceForAccount(senderAccount.getPublicKey().toBase58(), lamports);
    ensureSufficientBalanceForAccount(recipientAccount, lamports);
  }

  @SneakyThrows
  private void ensureSufficientBalanceForAccount(String accountAddress, long lamports) {
    BigDecimal balance = balanceClient.getBalance(accountAddress, true).get();
    BigDecimal minRentExemption = balanceClient.getMinimumBalanceForRentExemption().get();
    BigDecimal totalRequired = BigDecimal.valueOf(lamports).add(minRentExemption);

    if (balance.compareTo(totalRequired) < 0) {
      log.info("Insufficient balance for {}. Requesting airdrop.", accountAddress);
      airDropClient.requestAirDrop(accountAddress, 1_000_000_000L).get(); // 1 SOL airdrop
      log.info("Airdrop successful for {}", accountAddress);
    }
  }
}
