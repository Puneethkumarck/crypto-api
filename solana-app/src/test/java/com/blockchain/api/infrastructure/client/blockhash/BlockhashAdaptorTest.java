package com.blockchain.api.infrastructure.client.blockhash;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.p2p.solanaj.rpc.types.config.Commitment.FINALIZED;

import com.blockchain.api.infrastructure.client.blochhash.BlockhashAdaptor;
import java.time.Duration;
import java.util.concurrent.CompletionException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.p2p.solanaj.rpc.RpcApi;
import org.p2p.solanaj.rpc.RpcClient;
import org.p2p.solanaj.rpc.RpcException;
import org.springframework.core.task.TaskExecutor;

@ExtendWith(MockitoExtension.class)
class BlockhashAdaptorTest {
  @InjectMocks private BlockhashAdaptor blockhashAdaptor;
  @Mock private RpcClient rpcClient;
  @Mock private RpcApi rpcApi;
  @Mock private TaskExecutor taskExecutor;

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
  void shouldReturnBlockhash() {
    // given
    when(rpcApi.getLatestBlockhash(FINALIZED)).thenReturn("blockhash");

    // when
    var blockhash = blockhashAdaptor.getLatestBlockhash();

    // then
    await()
        .atMost(Duration.ofSeconds(5))
        .untilAsserted(
            () -> {
              assertTrue(blockhash.join().length() > 0);
            });
  }

  @Test
  @SneakyThrows
  void shouldThrowRpcException() {
    // given
    when(rpcApi.getLatestBlockhash(FINALIZED)).thenThrow(new RpcException("error"));

    // when
    var blockhashFuture = blockhashAdaptor.getLatestBlockhash();

    // then
    await()
        .atMost(Duration.ofSeconds(5))
        .untilAsserted(
            () -> {
              assertThrows(CompletionException.class, blockhashFuture::join);
            });
  }
}
