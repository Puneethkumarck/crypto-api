package com.blockchain.api.domain.service.balance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BalanceServiceTest {
  @InjectMocks private BalanceService balanceService;

  @Mock private BalanceClient balanceClient;

  @Test
  void shouldReturnEthereumBalance() {
    // given
    var address = "0xbd3a24c4447e0aacdf1000d478186e16ba2c013a";
    when(balanceClient.getBalance(address))
        .thenReturn(CompletableFuture.completedFuture(new BigInteger("1000000000000000000")));

    // when
    var balance = balanceService.getEthereumBalance(address);

    // then
    assertThat(balance).isEqualTo(new BigDecimal("1"));
  }

  @Test
  void shouldReturnTrue_whenBalanceIsSufficient() {
    // given
    var address = "0xbd3a24c4447e0aacdf1000d478186e16ba2c013a";

    when(balanceClient.getBalance(address))
        .thenReturn(CompletableFuture.completedFuture(new BigInteger("2000000000000000000")));

    // when
    var result = balanceService.isBalanceSufficient(address, new BigDecimal("0.2"));

    // then
    assertThat(result).isTrue();
  }

  @Test
  void shouldReturnFalse_whenBalanceIsInsufficient() {
    // given
    var address = "0xbd3a24c4447e0aacdf1000d478186e16ba2c013a";
    var amount = BigInteger.TWO;
    when(balanceClient.getBalance(address))
        .thenReturn(CompletableFuture.completedFuture(new BigInteger("1000000000000000000")));

    // when
    var result = balanceService.isBalanceSufficient(address, new BigDecimal("2.0"));

    // then
    assertThat(result).isFalse();
  }
}
