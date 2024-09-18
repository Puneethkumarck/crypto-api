package com.blockchain.api.domain.service.transfer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
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
class TransactionFeeServiceTest {
  @InjectMocks private TransactionFeeService transactionFeeService;

  @Mock private TransferClient transferClient;

  @Test
  void shouldReturnTransactionFee_whenTransactionFeeIsCalculated() {
    // given
    when(transferClient.getGasPrice())
        .thenReturn(CompletableFuture.completedFuture(BigInteger.valueOf(1000000000)));
    var expectedTransactionFee = new BigDecimal("0.000021");

    // when
    var transactionFee = transactionFeeService.getTransactionFee();

    // then
    assertThat(transactionFee).isEqualByComparingTo(expectedTransactionFee);
  }
}
