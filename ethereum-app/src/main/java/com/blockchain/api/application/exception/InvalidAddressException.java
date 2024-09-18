package com.blockchain.api.application.exception;

public class InvalidAddressException extends RuntimeException {
  public InvalidAddressException(String message) {
    super(message);
  }

  public static InvalidAddressException withAddress(String address) {
    return new InvalidAddressException("Invalid address:%s".formatted(address));
  }
}
