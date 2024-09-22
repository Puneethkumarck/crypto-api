package com.blockchain.api.application.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class InsufficientBalanceExceptionTest {

  @Test
  void shouldReturnInsufficientBalanceException() {
    // given
    var message = "Insufficient balance";

    // when
    var result = new InsufficientBalanceException(message);

    // then
    assertEquals(message, result.getMessage());
  }

  @Test
  void shouldCreateInsufficientBalanceExceptionWithStaticMethod() {
    // Given
    var message = "Insufficient balance";

    // When
    var result = InsufficientBalanceException.create(message);

    // Then
    assertEquals(message, result.getMessage());
  }
}
