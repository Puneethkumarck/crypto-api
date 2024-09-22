package com.blockchain.api.application.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.blockchain.api.application.BaseControllerTest;
import com.blockchain.api.application.mapper.TransferRequestDtoMapper;
import com.blockchain.api.application.mapper.TransferRequestDtoMapperImpl;
import com.blockchain.api.domain.service.transfer.TransferService;
import com.blockchain.api.model.ApiError;
import com.blockchain.api.model.EthTransferRequestDto;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

@WebMvcTest(TransferController.class)
@Import(TransferRequestDtoMapperImpl.class)
@Slf4j
class TransferControllerTest extends BaseControllerTest {
  @MockBean private TransferService transferService;
  @Autowired private TransferRequestDtoMapper mapper;

  @Test
  @SneakyThrows
  void shouldThrowInvalidAddressExceptionWhenAddressIsInvalid() {
    // given
    var transferRequestDto = EthTransferRequestDto.builder().to("0x123").amount("0.1").build();

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

    // then
    var resultDto = objectMapper.readValue(response, ApiError.class);

    var expected =
        ApiError.builder().message("Invalid address: 0x123").status(BAD_REQUEST.toString()).build();

    assertThat(resultDto).usingRecursiveComparison().isEqualTo(expected);
  }

  @Test
  @SneakyThrows
  void shouldTransferEth() {
    // given
    var transferRequestDto =
        EthTransferRequestDto.builder()
            .to("0x5d940f3947c4ab1fbdbf1f540a10019931065f7a")
            .amount("0.1")
            .build();
    doNothing().when(transferService).transfer(mapper.toDomain(transferRequestDto));

    // when
    mockMvc
        .perform(
            post("/api/v1/transfers")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transferRequestDto)))
        .andExpect(status().isOk());
  }
}
