package com.blockchain.api.domain.service.transfer;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.web3j.utils.Convert;

@Slf4j
public class CurrencyConversionTest {

  @Test
  void convertToWei() {
    // given
    var amount = "0.05";

    // when
    var result = Convert.toWei(amount, Convert.Unit.ETHER).toBigInteger();

    // then
    assertThat(result).isEqualByComparingTo(new BigDecimal("50000000000000000").toBigInteger());
  }
}
