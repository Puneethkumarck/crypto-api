package com.blockchain.api.domain.service.airdrop;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AirDropServiceTest {
  @InjectMocks private AirDropService airDropService;

  @Mock private AirDropClient airDropClient;

  @Test
  void shouldRequestAirDrop() {
    // given
    var address = "solBlockChainAddress";
    var amount = 100000L;
    var transactionHash = "transactionhash";
    when(airDropClient.requestAirDrop(address, amount))
        .thenReturn(completedFuture(transactionHash));

    // when
    var result = airDropService.requestAirDrop(address, amount);

    // then
    assertThat(result).isEqualTo(transactionHash);
  }
}
