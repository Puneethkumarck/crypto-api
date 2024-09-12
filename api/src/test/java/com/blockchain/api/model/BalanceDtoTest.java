package com.blockchain.api.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class BalanceDtoTest {

  @Test
  void shouldCreateBalanceDto() {
    // Given
    var address = "address";
    var balance = "balance";

    // When
    var result = BalanceDto.builder().address(address).balance(balance).build();

    // Then
    var expected = new BalanceDto(address, balance);
    assertEquals(expected, result);
  }
}
