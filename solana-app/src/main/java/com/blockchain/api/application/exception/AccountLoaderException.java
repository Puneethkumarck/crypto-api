package com.blockchain.api.application.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AccountLoaderException extends RuntimeException {
  public AccountLoaderException(String message) {
    super(message);
  }

  public static AccountLoaderException withMessage(String message) {
    return new AccountLoaderException(message);
  }
}
