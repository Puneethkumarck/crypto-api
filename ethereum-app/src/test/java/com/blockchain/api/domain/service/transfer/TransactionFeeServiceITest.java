package com.blockchain.api.domain.service.transfer;

import static org.assertj.core.api.Assertions.assertThat;

import com.blockchain.api.infrastructure.client.BaseEthereumTest;
import java.math.BigDecimal;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Slf4j
class TransactionFeeServiceITest extends BaseEthereumTest {

  @Autowired private TransactionFeeService transactionFeeService;

  @Test
  void shouldReturnTransactionFee_whenTransactionFeeIsCalculated() {
    // given
    stubGasPrice();
    var expectedTransactionFee = new BigDecimal("0.000234686314653000");

    // when
    var transactionFee = transactionFeeService.getTransactionFee();

    // then
    assertThat(transactionFee).isEqualByComparingTo(expectedTransactionFee);
  }
}
