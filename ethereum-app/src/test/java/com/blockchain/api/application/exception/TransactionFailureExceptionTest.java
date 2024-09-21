package com.blockchain.api.application.exception;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TransactionFailureExceptionTest {

  @Test
  void shouldCreateExceptionWithCorrectMessageAndCause() {
    // given
    var transaction = "signedTransaction";
    var cause = new RuntimeException("RPC error");

    // when
    var exception = TransactionFailureException.withTransaction(transaction, cause);

    // then
    assertThat(exception)
        .isInstanceOf(TransactionFailureException.class)
        .hasMessage("error occrred during executing transaction: signedTransaction")
        .hasCause(cause)
        .hasRootCauseMessage("RPC error");
  }

  @Test
  void shouldRetainCauseInException() {
    // given
    var message = "Transaction failed";
    var cause = new IllegalArgumentException("Invalid input");

    // when
    var exception = new TransactionFailureException(message, cause);

    // then
    assertThat(exception)
        .isInstanceOf(TransactionFailureException.class)
        .hasMessage(message)
        .hasCause(cause)
        .hasRootCauseMessage("Invalid input");
  }
}
