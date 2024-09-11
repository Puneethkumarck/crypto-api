package com.blockchain.api.application.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.blockchain.api.application.BaseControllerTest;
import com.blockchain.api.application.mapper.BalanceDtoMapper;
import com.blockchain.api.application.mapper.BalanceDtoMapperImpl;
import com.blockchain.api.domain.service.balance.BalanceService;
import com.blockchain.api.model.ApiError;
import com.blockchain.api.model.BalanceDto;
import java.math.BigDecimal;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

@WebMvcTest(SolanaBalanceController.class)
@Import(BalanceDtoMapperImpl.class)
class SolanaBalanceControllerTest extends BaseControllerTest {
  @MockBean private BalanceService balanceService;
  @Autowired private BalanceDtoMapper balanceMapper;

  @Test
  @SneakyThrows
  void getBalance() {
    // given
    var address = "CVSvjutqskYyF1hZTyZARGjGvf8d1Pp9mJMAJ8hTMHhm";
    when(balanceService.getSolanaBalance(address)).thenReturn(BigDecimal.valueOf(100L));

    // when
    var result =
        mockMvc
            .perform(
                get("/api/v1/balances/{address}", "CVSvjutqskYyF1hZTyZARGjGvf8d1Pp9mJMAJ8hTMHhm"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    var resultDto = objectMapper.readValue(result, BalanceDto.class);

    var expected = BalanceDto.builder().address(address).balance("100").build();

    assertThat(resultDto).usingRecursiveComparison().isEqualTo(expected);
  }

  @Test
  @SneakyThrows
  void getBalanceInvalidAddress() {
    // given
    var address = "CVSvjutqskYyF1hZTyZAR";

    // when
    var result =
        mockMvc
            .perform(get("/api/v1/balances/{address}", address))
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

    var response = objectMapper.readValue(result, ApiError.class);

    var expected =
        ApiError.builder().status("400 BAD_REQUEST").message("Invalid address:" + address).build();

    assertThat(response).usingRecursiveComparison().isEqualTo(expected);
  }

  @Test
  @SneakyThrows
  void getBalanceEmptyAddress() {
    // given
    var address = " ";

    // when
    var result =
        mockMvc
            .perform(get("/api/v1/balances/{address}", address))
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

    // then
    var response = objectMapper.readValue(result, ApiError.class);
    var expected =
        ApiError.builder().status("400 BAD_REQUEST").message("Invalid address: ").build();
    assertThat(response).isEqualTo(expected);
  }
}
