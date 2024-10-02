package com.blockchain.api.application.exception;

public class BalanceRetrievalException extends RuntimeException {

  public BalanceRetrievalException(String message) {
    super(message);
  }

  public static BalanceRetrievalException withAddress(String message) {
    return new BalanceRetrievalException(
        "Rpc error while retrieving balance %s".formatted(message));
  }
}
