package com.blockchain.api.application.validator;

import static com.blockchain.api.application.validator.SolanaAddressValidator.isValidAddress;
import static com.blockchain.api.application.validator.SolanaAddressValidator.isValidAddressOrThrow;
import static org.junit.jupiter.api.Assertions.*;

import com.blockchain.api.application.exception.InvalidAddressException;
import org.junit.jupiter.api.Test;

class SolanaAddressValidatorTest {
  @Test
  void shouldReturnTrueWhenAddressIsValid() {
    // Given
    var address = "p8guAeE7naqQcT2JMCp8Q376MLyzt5XynfGw3uCHM75";

    // When
    var result = isValidAddress(address);

    // Then
    assertTrue(result);
  }

  @Test
  void shouldReturnFalseWhenAddressIsInvalid() {
    // Given
    var address = "G1tJm4v8";

    // When
    var result = isValidAddress(address);

    // Then
    assertFalse(result);
  }

  @Test
  void shouldThrowInvalidAddressException() {
    // Given
    var address = "G1tJm4v8";

    // When // Then
    assertThrows(
        InvalidAddressException.class,
        () -> {
          isValidAddressOrThrow(address);
        });
  }
}
