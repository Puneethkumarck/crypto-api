package com.blockchain.api.application.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.blockchain.api.application.BaseControllerIT;
import com.blockchain.api.model.BalanceDto;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

public class SolanaBalanceControllerIT extends BaseControllerIT {
  @Test
  @SneakyThrows
  void getBalance() {
    // given
    var address = "CVSvjutqskYyF1hZTyZARGjGvf8d1Pp9mJMAJ8hTMHhm";
    stubGetBalance();

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

    var expected = BalanceDto.builder().address(address).balance("3.000000000").build();

    assertThat(resultDto).usingRecursiveComparison().isEqualTo(expected);
  }
}
