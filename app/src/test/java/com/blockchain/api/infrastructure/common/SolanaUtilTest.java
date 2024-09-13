package com.blockchain.api.infrastructure.common;

import static com.blockchain.api.infrastructure.common.SolanaUtil.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SolanaUtilTest {

  @Test
  void shouldConvertLamportToSol() {
    // given
    var lamport = 1_000_000_000L;

    // when
    var result = lamportsToSol(lamport);

    // then
    var expected = BigDecimal.ONE;
    assertThat(result).isEqualByComparingTo(expected);
  }
}
