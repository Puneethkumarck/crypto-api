package com.blockchain.api.application.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.p2p.solanaj.rpc.RpcException;

class SolanaTransactionExceptionTest {

  @Test
  void shouldCreateSolanaTransactionExceptionWithMessageAndCause() {
    // Given
    var recipientAddress = "invalidAddress";
    var cause = new RuntimeException("Root cause");

    // When
    var result = SolanaTransactionException.withException(recipientAddress, cause);

    // Then
    var expectedMessage = "Exception initiating transaction: %s".formatted(recipientAddress);
    assertEquals(expectedMessage, result.getMessage());
  }

  @Test
  void shouldCreateSolanaTransactionExceptionWithRpcException() {
    // Given
    var recipientAddress = "invalidSolanaBlockChainAddress";
    var rpcException = new RpcException("RPC Error");

    // When
    var result = SolanaTransactionException.withRpcException(recipientAddress, rpcException);

    // Then
    var expectedMessage =
        "Failed to send transaction for recipient address: %s".formatted(recipientAddress);
    assertEquals(expectedMessage, result.getMessage());
    assertEquals(rpcException, result.getCause());
  }

  @Test
  void shouldThrowSolanaTransactionException() {
    // Given
    var recipientAddress = "InvalidAddress";
    var cause = new Exception("Cause of failure");

    // When / Then
    SolanaTransactionException thrown =
        assertThrows(
            SolanaTransactionException.class,
            () -> {
              throw new SolanaTransactionException(recipientAddress, cause);
            });

    assertEquals(recipientAddress, thrown.getMessage());
    assertEquals(cause, thrown.getCause());
  }
}
