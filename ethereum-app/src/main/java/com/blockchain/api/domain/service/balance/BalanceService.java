package com.blockchain.api.domain.service.balance;

import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.web3j.utils.Convert;

@Slf4j
@Service
@RequiredArgsConstructor
public class BalanceService {

  private final BalanceClient balanceClient;

  public BigDecimal getEthereumBalance(String address) {
    var balance = balanceClient.getBalance(address).join();
    return Convert.fromWei(new BigDecimal(balance), Convert.Unit.ETHER);
  }

  public boolean isBalanceSufficient(String address, BigDecimal amount) {
    var balance = getEthereumBalance(address);
    return balance.compareTo(amount) >= 0;
  }
}
