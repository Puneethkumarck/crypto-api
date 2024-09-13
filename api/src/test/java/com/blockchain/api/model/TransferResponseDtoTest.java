package com.blockchain.api.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TransferResponseDtoTest {

  @Test
  void shouldCreateTransferResponseDto() {
    // Given
    var transactionHash = "transactionHash";

    // When
    var result = TransferResponseDto.builder().transactionHash(transactionHash).build();

    // Then
    var expected = new TransferResponseDto(transactionHash);
    assertEquals(expected, result);
  }
}
