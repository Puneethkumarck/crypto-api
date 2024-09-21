package com.blockchain.api.application.exception;

public class TransactionFailureException extends RuntimeException {

  public TransactionFailureException(String message, Throwable cause) {
    super(message, cause);
  }

  public static TransactionFailureException withTransaction(String transaction, Throwable cause) {
    return new TransactionFailureException(
        "error occrred during executing transaction: %s".formatted(transaction), cause);
  }
}
