package com.blockchain.api.infrastructure.client.balance;

import static com.blockchain.api.infrastructure.common.SolanaUtil.lamportsToSol;
import static java.math.RoundingMode.UNNECESSARY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
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
class BalanceAdaptorTest {

  private static final long VALID_LAMPORTS = 3000000000L;
  private static final String VALID_ADDRESS = "CVSvjutqskYyF1hZTyZARGjGvf8d1Pp9mJMAJ8hTMHhm";
  private static final PublicKey VALID_PUBLIC_KEY = PublicKey.valueOf(VALID_ADDRESS);
  @Mock private RpcClient rpcClient;
  @Mock private RpcApi rpcApi;
  @Mock private Executor taskExecutor;
  @InjectMocks private BalanceAdaptor balanceAdaptor;

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
  void shouldConvertLamportsToSolCorrectly() throws RpcException {
    // given
    when(rpcApi.getBalance(VALID_PUBLIC_KEY)).thenReturn(VALID_LAMPORTS);

    // when
    var balance = balanceAdaptor.getBalance(VALID_ADDRESS);

    // then
    await()
        .atMost(Duration.ofSeconds(5))
        .untilAsserted(
            () ->
                assertThat(balance.join())
                    .isEqualByComparingTo(BigDecimal.valueOf(3).setScale(9, UNNECESSARY)));

    verify(rpcApi, times(1)).getBalance(VALID_PUBLIC_KEY);
  }

  @Test
  @SneakyThrows
  void shouldReturnZeroBalanceOnRpcException() {
    // given
    doThrow(new RpcException("RPC error")).when(rpcApi).getBalance(VALID_PUBLIC_KEY);

    // when
    var balance = balanceAdaptor.getBalance(VALID_ADDRESS).join();

    // then
    await()
        .atMost(Duration.ofSeconds(5))
        .untilAsserted(() -> assertThat(balance).isEqualByComparingTo(BigDecimal.ZERO));

    verify(rpcApi, times(1)).getBalance(VALID_PUBLIC_KEY);
  }

  @Test
  @SneakyThrows
  void shouldFetchMinimumBalanceForRentExemptionSuccessfully() {
    // given
    long lamportsForRentExemption = 890880L;
    when(rpcApi.getMinimumBalanceForRentExemption(0)).thenReturn(lamportsForRentExemption);

    // when
    var resultFuture = balanceAdaptor.getMinimumBalanceForRentExemption();

    // then
    var expectedBalance = lamportsToSol(lamportsForRentExemption);

    await()
        .atMost(Duration.ofSeconds(5))
        .untilAsserted(() -> assertThat(resultFuture.join()).isEqualByComparingTo(expectedBalance));

    verify(rpcApi, times(1)).getMinimumBalanceForRentExemption(0);
  }

  @Test
  @SneakyThrows
  void shouldReturnZeroWhenMinimumBalanceForRentExemptionRpcExceptionOccurs() {
    // given
    when(rpcApi.getMinimumBalanceForRentExemption(0))
        .thenThrow(new RpcException("RPC failure on minimum balance fetch"));

    // when
    var resultFuture = balanceAdaptor.getMinimumBalanceForRentExemption();

    // then
    await()
        .atMost(Duration.ofSeconds(5))
        .untilAsserted(() -> assertThat(resultFuture.join()).isEqualByComparingTo(BigDecimal.ZERO));

    verify(rpcApi, times(1)).getMinimumBalanceForRentExemption(0);
  }
}
