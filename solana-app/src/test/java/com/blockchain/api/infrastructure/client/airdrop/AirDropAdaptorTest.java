package com.blockchain.api.infrastructure.client.airdrop;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.blockchain.api.application.exception.RequestAirDropException;
import java.time.Duration;
import java.util.concurrent.Executor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.p2p.solanaj.core.PublicKey;
import org.p2p.solanaj.rpc.RpcApi;
import org.p2p.solanaj.rpc.RpcClient;
import org.p2p.solanaj.rpc.RpcException;

@ExtendWith(MockitoExtension.class)
class AirDropAdaptorTest {

  private static final String VALID_ADDRESS = "CVSvjutqskYyF1hZTyZARGjGvf8d1Pp9mJMAJ8hTMHhm";
  private static final long VALID_AMOUNT = 1000000000L;
  private static final String REQUEST_AIRDROP_SIGNATURE = "airdrop-signature";

  @Mock private RpcClient rpcClient;
  @Mock private RpcApi rpcApi;
  @Mock private Executor taskExecutor;
  @InjectMocks private AirDropAdaptor airDropAdaptor;

  @BeforeEach
  void setUp() {
    when(rpcClient.getApi()).thenReturn(rpcApi);
    doAnswer(
            invocation -> {
              Runnable runnable = invocation.getArgument(0);
              runnable.run();
              return null;
            })
        .when(taskExecutor)
        .execute(any(Runnable.class));
  }

  @Test
  @SneakyThrows
  void shouldRequestAirDropSuccessfully() {
    // given
    when(rpcApi.requestAirdrop(PublicKey.valueOf(VALID_ADDRESS), VALID_AMOUNT))
        .thenReturn(REQUEST_AIRDROP_SIGNATURE);

    // when
    var response = airDropAdaptor.requestAirDrop(VALID_ADDRESS, VALID_AMOUNT);

    // then
    await()
        .atMost(Duration.ofSeconds(5))
        .untilAsserted(() -> assertThat(response.join()).isEqualTo(REQUEST_AIRDROP_SIGNATURE));

    verify(rpcApi, times(1)).requestAirdrop(PublicKey.valueOf(VALID_ADDRESS), VALID_AMOUNT);
  }

  @Test
  @SneakyThrows
  void shouldThrowRequestAirDropExceptionOnRpcFailure() {
    // given
    when(rpcApi.requestAirdrop(PublicKey.valueOf(VALID_ADDRESS), VALID_AMOUNT))
        .thenThrow(new RpcException("RPC error"));

    // when
    var response = airDropAdaptor.requestAirDrop(VALID_ADDRESS, VALID_AMOUNT);

    // then
    await()
        .atMost(Duration.ofSeconds(5))
        .untilAsserted(
            () ->
                assertThat(response)
                    .hasFailedWithThrowableThat()
                    .isInstanceOf(RequestAirDropException.class)
                    .hasMessageContaining(
                        "Airdrop request failed for address:%s".formatted(VALID_ADDRESS)));

    verify(rpcApi, times(1)).requestAirdrop(PublicKey.valueOf(VALID_ADDRESS), VALID_AMOUNT);
  }
}
