package com.blockchain.api.domain.service.balance;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
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
  void shouldReturnSolanaBalance() {
    // given
    var address = "someAddress";
    var expectedBalance = new BigDecimal("1.999603");
    when(balanceClient.getBalance(address, false)).thenReturn(completedFuture(expectedBalance));

    // when
    var actualBalance = balanceService.getSolanaBalance(address);

    // then
    assertThat(actualBalance).isEqualTo(expectedBalance);
  }

  @Test
  void shouldReturnMinimumBalanceForRentExemption() {
    // given
    var expectedMinimumBalance = new BigDecimal("890880");
    when(balanceClient.getMinimumBalanceForRentExemption())
        .thenReturn(completedFuture(expectedMinimumBalance));

    // when
    var actualMinimumBalance = balanceService.getMinimumBalanceForRentExemption();

    // then
    assertThat(actualMinimumBalance).isEqualTo(expectedMinimumBalance);
  }
}
