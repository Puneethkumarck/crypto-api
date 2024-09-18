package com.blockchain.api.application.exception;

public class GasPriceRetrievalException extends RuntimeException {
  public GasPriceRetrievalException(String message, Throwable cause) {
    super(message, cause);
  }

  public static GasPriceRetrievalException build(Throwable e) {
    return new GasPriceRetrievalException("error while retrieving gas price", e);
  }
}
