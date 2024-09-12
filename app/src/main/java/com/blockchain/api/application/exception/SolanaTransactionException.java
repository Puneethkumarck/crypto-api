package com.blockchain.api.application.exception;

public class SolanaTransactionException extends RuntimeException {
  public SolanaTransactionException(String message) {
    super(message);
  }

  public SolanaTransactionException(String message, Throwable cause) {
    super(message, cause);
  }
}
