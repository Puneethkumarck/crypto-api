package com.blockchain.api.domain.service.transfer;

import java.math.BigDecimal;
import java.math.BigInteger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.web3j.utils.Convert;

@RequiredArgsConstructor
@Service
@Slf4j
public class TransactionFeeService {

  private final TransferClient transferClient;
  public static final BigDecimal GAS_LIMIT = new BigDecimal("21000"); // For Simple Transactions

  public BigDecimal getTransactionFee() {
    var gasPriceInWei = transferClient.getGasPrice().join();
    return calculateTransactionFee(gasPriceInWei);
  }

  private BigDecimal calculateTransactionFee(BigInteger gasPriceInWei) {
    var gasFeeInEther = convertWeiToEther(new BigDecimal(gasPriceInWei));
    return gasFeeInEther.multiply(GAS_LIMIT);
  }

  private BigDecimal convertWeiToEther(BigDecimal wei) {
    return Convert.fromWei(wei, Convert.Unit.ETHER);
  }
}
