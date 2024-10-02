package com.blockchain.api.infrastructure.client.balance;

import static org.assertj.core.api.Assertions.assertThat;

import com.blockchain.api.domain.service.balance.Contract;
import com.blockchain.api.infrastructure.client.BaseEthereumTest;
import java.math.BigDecimal;
import java.math.RoundingMode;
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

  @Test
  void getAccountBalanceUSDC() {
    // given
    var address = "0x6bf1b0fB6B1A82fA0e42E50A798507FE8021A741";
    stubGetUSDCBalance();

    // when
    var futureBalance = balanceAdaptor.getNonEthBalance(address, Contract.USDC).join();
    var usdcBalance =
        new BigDecimal(futureBalance)
            .divide(BigDecimal.valueOf(1_000_000), 6, RoundingMode.UNNECESSARY);
    var expectedUsdcBalance = new BigDecimal("80000.800882");

    // then
    assertThat(usdcBalance).isEqualByComparingTo(expectedUsdcBalance);
  }

  @Test
  void getAccountBalanceUSDT() {
    // given
    var address = "0xDccd1E7ef46A5620081E581398d5A77bF1E9762d";
    stubGetUSDTBalance();

    // when
    var futureBalance = balanceAdaptor.getNonEthBalance(address, Contract.USDC).join();
    var usdtBalance =
            new BigDecimal(futureBalance)
                    .divide(BigDecimal.valueOf(1_000_000), 6, RoundingMode.UNNECESSARY);
    var expectedUsdtBalance = new BigDecimal("1995442.593467");

    // then
    assertThat(usdtBalance).isEqualByComparingTo(expectedUsdtBalance);
  }
}
