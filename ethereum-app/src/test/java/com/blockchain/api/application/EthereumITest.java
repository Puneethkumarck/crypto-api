package com.blockchain.api.application;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.blockchain.api.domain.service.balance.BalanceClient;
import com.blockchain.api.domain.service.transfer.TransferClient;
import java.math.BigDecimal;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.web3j.utils.Convert;

@SpringBootTest
@ActiveProfiles("test")
@Slf4j
public class EthereumITest {
  @Autowired private BalanceClient balanceClient;

  @Autowired private TransferClient transferClient;

  @Test
  void shouldReturnBalance() {
    // given
    var address = "0xbd3a24c4447e0aacdf1000d478186e16ba2c013a";

    // when
    var result = balanceClient.getBalance(address).join();

    // then
    assertNotNull(result);
    log.info("Balance BigInteger: {}", Convert.fromWei(new BigDecimal(result), Convert.Unit.ETHER));
  }

  @Test
  void shouldGetGasPrice() {
    // when
    var result = transferClient.getGasPrice().join();

    // then
    assertNotNull(result);
    log.info(
        "original value {} Gas price: {}",
        result,
        Convert.fromWei(new BigDecimal(result), Convert.Unit.ETHER));
  }
}
