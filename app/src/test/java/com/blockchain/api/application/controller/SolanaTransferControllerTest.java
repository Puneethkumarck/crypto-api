package com.blockchain.api.application.controller;

import static org.mockito.Mockito.doNothing;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.blockchain.api.application.BaseControllerTest;
import com.blockchain.api.application.mapper.TransferRequestDtoMapper;
import com.blockchain.api.application.mapper.TransferRequestDtoMapperImpl;
import com.blockchain.api.domain.service.transfer.TransferService;
import com.blockchain.api.model.TransferRequestDto;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

@WebMvcTest(SolanaTransferController.class)
@Import(TransferRequestDtoMapperImpl.class)
class SolanaTransferControllerTest extends BaseControllerTest {

  @MockBean private TransferService transferService;
  @Autowired private TransferRequestDtoMapper mapper;

  @Test
  @SneakyThrows
  void transfer() {
    // given
    TransferRequestDto request =
        TransferRequestDto.builder()
            .to("8FKTyHMLQAsZB1Jv7RTshkXuwXHpYsWtEhKhZmXzvH2p")
            .amount(5000L)
            .build();

    doNothing().when(transferService).transfer(mapper.mapToDomain(request));

    // when
    mockMvc
        .perform(
            post("/api/v1/transfers")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());
  }

  @Test
  @SneakyThrows
  void transferInvalidRequest() {
    // given
    TransferRequestDto request =
        TransferRequestDto.builder().to("8FKTyHMLQAsZB1Jv7RTshkXuwXHpYsWtEhKhZmXzvH2p").build();
    // when
    mockMvc
        .perform(
            post("/api/v1/transfers")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Amount is required"));
  }

  @Test
  @SneakyThrows
  void transferInvalidRequestToAddress() {
    // given
    TransferRequestDto request = TransferRequestDto.builder().amount(5000L).build();
    // when
    mockMvc
        .perform(
            post("/api/v1/transfers")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("To address is required"));
  }
}
