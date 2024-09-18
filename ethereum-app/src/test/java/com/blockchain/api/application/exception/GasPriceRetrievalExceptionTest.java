package com.blockchain.api.application.exception;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class GasPriceRetrievalExceptionTest {

  @Test
  void shouldReturnGasPriceRetrievalException() {
    // given
    var message = "error while retrieving gas price";
    var cause = new Throwable();

    // when
    var result = new GasPriceRetrievalException(message, cause);

    // then
    assertThat(result.getMessage()).isEqualTo(message);
  }

  @Test
  void shouldCreateGasPriceRetrievalExceptionWithStaticMethod() {
    // Given
    var message = "error while retrieving gas price";
    var cause = new Throwable();

    // When
    var result = GasPriceRetrievalException.build(cause);

    // Then
    assertEquals(message, result.getMessage());
  }
}
