package com.blockchain.api.infrastructure.client.balance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import com.blockchain.api.infrastructure.client.BaseSolanaTest;
import java.math.BigDecimal;
import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class BalanceAdaptorITest extends BaseSolanaTest {
  @Autowired private BalanceAdaptor balanceAdaptor;

  @Test
  void getAccountBalance() {
    // given
    var address = "CVSvjutqskYyF1hZTyZARGjGvf8d1Pp9mJMAJ8hTMHhm";
    var expectedBalance = BigDecimal.valueOf(3L);
    stubGetBalance();

    // when
    var futureBalance = balanceAdaptor.getBalance(address, false);

    // then
    await()
        .atMost(Duration.ofSeconds(5))
        .untilAsserted(
            () -> assertThat(futureBalance.join()).isEqualByComparingTo(expectedBalance));
  }

  @Test
  void getMinimumBalanceForRentExemption() {
    // given
    var expectedBalance = BigDecimal.valueOf(0.000890880);
    stubGetMinimumBalanceForRentExemption();

    // when
    var balance = balanceAdaptor.getMinimumBalanceForRentExemption();

    // then
    await()
        .atMost(Duration.ofSeconds(5))
        .untilAsserted(() -> assertThat(balance.join()).isEqualByComparingTo(expectedBalance));
  }
}
