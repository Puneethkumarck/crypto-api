package com.blockchain.api.application.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class InvalidAddressExceptionTest {

  @Test
  void shouldCreateInvalidAddressExceptionWithMessage() {
    // Given
    var message = "Invalid Address Exception";

    // When
    var result = new InvalidAddressException(message);

    // Then
    assertEquals(message, result.getMessage());
  }

  @Test
  void shouldThrowInvalidAddressException() {
    // Given
    var message = "Invalid Address Exception";

    // When / Then
    InvalidAddressException thrown =
        assertThrows(
            InvalidAddressException.class,
            () -> {
              throw new InvalidAddressException(message);
            });

    assertEquals(message, thrown.getMessage());
  }

  @Test
  void shouldCreateInvalidAddressExceptionWithStaticMethod() {
    // Given
    var message = "blockChainAddress";

    // When
    var result = InvalidAddressException.withAddress(message);

    // Then
    var expectedMessage = "Invalid address:%s".formatted(message);
    assertEquals(expectedMessage, result.getMessage());
  }
}
