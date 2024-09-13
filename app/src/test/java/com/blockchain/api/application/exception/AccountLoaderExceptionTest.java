package com.blockchain.api.application.exception;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class AccountLoaderExceptionTest {

  @Test
  void shouldCreateAccountLoaderExceptionWithStaticMethod() {
    // Given
    var message = "Account Loader Exception";

    // When
    var result = AccountLoaderException.withMessage(message);

    // Then
    assertThat(message).isEqualTo(result.getMessage());
  }

  @Test
  void shouldCreateExceptionWithMessage() {
    // Given
    var message = "Account Loader Exception";

    // When
    var result = new AccountLoaderException(message);

    // Then
    assertThat(message).isEqualTo(result.getMessage());
  }

  @Test
  void shouldThrowAccountLoaderException() {
    // given
    String message = "Error while loading account";

    // when / then
    AccountLoaderException thrown =
        assertThrows(
            AccountLoaderException.class,
            () -> {
              throw AccountLoaderException.withMessage(message);
            });

    assertThat(thrown.getMessage()).isEqualTo(message);
  }
}
