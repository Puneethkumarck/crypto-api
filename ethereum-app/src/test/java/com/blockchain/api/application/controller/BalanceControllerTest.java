package com.blockchain.api.application.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
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

@WebMvcTest(BalanceController.class)
@Import(BalanceDtoMapperImpl.class)
class BalanceControllerTest extends BaseControllerTest {

  @MockBean private BalanceService balanceService;
  @Autowired private BalanceDtoMapper balanceMapper;

  @Test
  @SneakyThrows
  void getBalance() {
    // given
    var address = "0x5d940f3947c4ab1fbdbf1f540a10019931065f7a";
    when(balanceService.getEthereumBalance(address)).thenReturn(new BigDecimal("0.1"));

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

  @Test
  @SneakyThrows
  void getBalanceInvalidAddress() {
    // given
    var address = "8FKTyHMLQAsZB1Jv7RTshkXuwXHpYsWtEhKhZmXzvH2p";
    when(balanceService.getEthereumBalance(address)).thenReturn(new BigDecimal("0.1"));

    // when
    var result =
        mockMvc
            .perform(get("/api/v1/balances/{address}", address))
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

    var resultDto = objectMapper.readValue(result, ApiError.class);

    var expected =
        ApiError.builder()
            .status(BAD_REQUEST.toString())
            .message("Invalid address: %s".formatted(address))
            .build();

    assertThat(resultDto).usingRecursiveComparison().isEqualTo(expected);
  }
}
