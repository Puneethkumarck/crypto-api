package com.blockchain.api.application.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.p2p.solanaj.rpc.RpcException;

class RequestAirDropExceptionTest {

  @Test
  void shouldCreateAirDropExceptionWithMessage() {
    // Given
    var message = "Airdrop request failed for address:%s";
    var address = "blockchainAddress";
    var rpcException = new RpcException("rpcException");

    // When
    var result = RequestAirDropException.withAddress(address, rpcException);

    // Then
    var expectedMessage = message.formatted(address);
    assertEquals(expectedMessage, result.getMessage());
  }
}
