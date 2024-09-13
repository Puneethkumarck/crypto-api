package com.blockchain.api.application.exception;

import org.p2p.solanaj.rpc.RpcException;

public class SolanaTransactionException extends RuntimeException {

  public SolanaTransactionException(String message, Throwable cause) {
    super(message, cause);
  }

  public static SolanaTransactionException withRpcException(
      String recipientAddress, RpcException e) {
    return new SolanaTransactionException(
        "Failed to send transaction for recipient address: %s".formatted(recipientAddress), e);
  }

  public static SolanaTransactionException withException(String recipientAddress, Exception e) {
    return new SolanaTransactionException(
        "Exception initiating transaction: %s".formatted(recipientAddress), e);
  }
}
