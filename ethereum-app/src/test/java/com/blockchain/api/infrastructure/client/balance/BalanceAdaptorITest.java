package com.blockchain.api.infrastructure.client.balance;

import static org.assertj.core.api.Assertions.assertThat;

import com.blockchain.api.infrastructure.client.BaseEthereumTest;
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
class BalanceAdaptorITest extends BaseEthereumTest {

  @Autowired private BalanceAdaptor balanceAdaptor;

  @Test
  void getAccountBalance() {
    // given
    var address = "0xbd3a24c4447e0aacdf1000d478186e16ba2c013a";
    stubGetBalance();

    // when
    var futureBalance = balanceAdaptor.getBalance(address).join();
    var ethBalance = Convert.fromWei(new BigDecimal(futureBalance), Convert.Unit.ETHER);
    var ethExpectedBalance = new BigDecimal("0.1");

    // then
    assertThat(ethBalance).isEqualByComparingTo(ethExpectedBalance);
  }
}
