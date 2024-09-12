package com.blockchain.api.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ApiErrorTest {

  @Test
  void shouldCreateApiError() {
    // Given
    var status = "status";
    var message = "message";

    // When
    var result = ApiError.builder().status(status).message(message).build();

    // Then
    var expected = new ApiError(status, message);
    assertThat(result).isEqualTo(expected);
  }
}
