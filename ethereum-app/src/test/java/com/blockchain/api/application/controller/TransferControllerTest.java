package com.blockchain.api.application.controller;

import static org.mockito.Mockito.doNothing;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

@WebMvcTest(TransferController.class)
@Import(TransferRequestDtoMapperImpl.class)
class TransferControllerTest extends BaseControllerTest {
  @MockBean private TransferService transferService;
  @Autowired private TransferRequestDtoMapper mapper;

  @Test
  @SneakyThrows
  void shouldTransferEth() {

    // given
    var transferRequestDto = TransferRequestDto.builder().to("0x123").amount(1000L).build();

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
