package com.blockchain.api.domain.service.balance;

import java.math.BigInteger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BalanceService {

  private final BalanceClient balanceClient;

  public BigInteger getEthereumBalance(String address) {
    return balanceClient.getBalance(address).join();
  }

  public boolean isBalanceSufficient(String address, BigInteger amount) {
    var balance = getEthereumBalance(address);
    return balance.compareTo(amount) >= 0;
  }
}
