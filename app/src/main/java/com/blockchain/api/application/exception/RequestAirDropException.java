package com.blockchain.api.application.exception;

import org.p2p.solanaj.rpc.RpcException;

public class RequestAirDropException extends RuntimeException {

  public RequestAirDropException(String message) {
    super(message);
  }

  public RequestAirDropException(String message, Throwable cause) {
    super(message, cause);
  }

  public static RequestAirDropException withAddress(String address, RpcException e) {
    return new RequestAirDropException(
        "Airdrop request failed for address:%s".formatted(address), e);
  }
}
