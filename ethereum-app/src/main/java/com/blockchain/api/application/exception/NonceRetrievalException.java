package com.blockchain.api.application.exception;

public class NonceRetrievalException extends RuntimeException {

  public NonceRetrievalException(String message) {
    super(message);
  }

  public static NonceRetrievalException withAddress(String address) {
    return new NonceRetrievalException("Unable to retrieve nonce for address: " + address);
  }
}
