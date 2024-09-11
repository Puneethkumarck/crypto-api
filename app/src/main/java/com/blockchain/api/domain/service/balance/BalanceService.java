package com.blockchain.api.domain.service.balance;

import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BalanceService {

  private final BalanceClient balanceClient;

  public BigDecimal getSolanaBalance(String address) {
    return balanceClient.getBalance(address).join();
  }

  public BigDecimal getMinimumBalanceForRentExemption() {
    return balanceClient.getMinimumBalanceForRentExemption().join();
  }
}
