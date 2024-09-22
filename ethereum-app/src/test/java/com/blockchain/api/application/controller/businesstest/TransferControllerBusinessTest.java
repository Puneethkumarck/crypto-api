package com.blockchain.api.application.controller.businesstest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.blockchain.api.infrastructure.client.BaseEthereumTest;
import com.blockchain.api.model.ApiError;
import com.blockchain.api.model.EthTransferRequestDto;
import com.blockchain.api.model.TransferRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
public class TransferControllerBusinessTest extends BaseEthereumTest {
  @Autowired protected MockMvc mockMvc;
  @Autowired protected ObjectMapper objectMapper;

  @Test
  @SneakyThrows
  void shouldThrowInvalidAddressExceptionWhenAddressIsInvalid() {
    // given
    var transferRequestDto = TransferRequestDto.builder().to("0x123").amount(1000L).build();

    // when
    var response =
        mockMvc
            .perform(
                post("/api/v1/transfers")
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(transferRequestDto)))
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

    var resultDto = objectMapper.readValue(response, ApiError.class);

    var expected =
        ApiError.builder().message("Invalid address: 0x123").status(BAD_REQUEST.toString()).build();

    assertThat(resultDto).usingRecursiveComparison().isEqualTo(expected);
  }

  @SneakyThrows
  @Test
  void shouldThrowInsufficientBalanceExceptionWhenBalanceIsInsufficient() {
    // given
    var transferRequestDto =
        TransferRequestDto.builder()
            .to("0x5d940f3947c4ab1fbdbf1f540a10019931065f7a")
            .amount(1000L)
            .build();
    stubGetNonce();
    stubGasPrice();
    stubGetBalance();

    var response =
        mockMvc
            .perform(
                post("/api/v1/transfers")
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(transferRequestDto)))
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

    var resultDto = objectMapper.readValue(response, ApiError.class);

    var expected =
        ApiError.builder()
            .message(
                "Insufficient balance for address: %s"
                    .formatted("0xbd3a24c4447e0aacdf1000d478186e16ba2c013a"))
            .status(BAD_REQUEST.toString())
            .build();

    assertThat(resultDto).usingRecursiveComparison().isEqualTo(expected);
  }

  @Test
  @SneakyThrows
  void shouldTransferEth() {
    // given
    var transferRequestDto =
        EthTransferRequestDto.builder()
            .to("0x5d940f3947c4ab1fbdbf1f540a10019931065f7a")
            .amount("0.0001")
            .build();
    stubGetNonce();
    stubGasPrice();
    stubGetBalance();
    stubSendTransaction();

    // when
    mockMvc
        .perform(
            post("/api/v1/transfers")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transferRequestDto)))
        .andExpect(status().isOk());
  }
}
