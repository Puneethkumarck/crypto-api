package com.blockchain.api.application.exception;

public class InsufficientBalanceException extends RuntimeException {

  public InsufficientBalanceException(String message) {
    super(message);
  }

  public static InsufficientBalanceException create(String message) {
    return new InsufficientBalanceException(message);
  }
}
