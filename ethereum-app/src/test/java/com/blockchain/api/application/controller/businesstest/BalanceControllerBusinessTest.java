package com.blockchain.api.application.controller.businesstest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.blockchain.api.infrastructure.client.BaseEthereumTest;
import com.blockchain.api.model.ApiError;
import com.blockchain.api.model.BalanceDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@Slf4j
public class BalanceControllerBusinessTest extends BaseEthereumTest {
  @Autowired protected MockMvc mockMvc;
  @Autowired protected ObjectMapper objectMapper;

  @Test
  @SneakyThrows
  void shouldThrowInvalidAddressExceptionWhenAddressIsInvalid() {
    // given
    var address = "8FKTyHML";

    // when
    var response =
        mockMvc
            .perform(get("/api/v1/balances/{address}", address))
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

    // then
    var resultDto = objectMapper.readValue(response, ApiError.class);

    var expected =
        ApiError.builder()
            .message("Invalid address: 8FKTyHML")
            .status(BAD_REQUEST.toString())
            .build();

    assertThat(resultDto).usingRecursiveComparison().isEqualTo(expected);
  }

  @SneakyThrows
  @Test
  void shouldGetBalance() {
    // given
    var address = "0x5d940f3947c4ab1fbdbf1f540a10019931065f7a";
    stubGetBalance();

    // when
    var result =
        mockMvc
            .perform(get("/api/v1/balances/{address}", address))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    var resultDto = objectMapper.readValue(result, BalanceDto.class);

    var expected = BalanceDto.builder().address(address).balance("0.1").build();

    assertThat(resultDto).usingRecursiveComparison().isEqualTo(expected);
  }
}
