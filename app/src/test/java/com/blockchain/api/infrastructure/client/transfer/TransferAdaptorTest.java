package com.blockchain.api.infrastructure.client.transfer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.blockchain.api.application.exception.SolanaTransactionException;
import java.time.Duration;
import java.util.concurrent.CompletionException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.p2p.solanaj.core.Account;
import org.p2p.solanaj.core.Transaction;
import org.p2p.solanaj.rpc.RpcApi;
import org.p2p.solanaj.rpc.RpcClient;
import org.p2p.solanaj.ws.SubscriptionWebSocketClient;
import org.p2p.solanaj.ws.listeners.NotificationEventListener;
import org.springframework.core.task.TaskExecutor;

@ExtendWith(MockitoExtension.class)
@Slf4j
class TransferAdaptorTest {
  @Mock private RpcClient rpcClient;
  @Mock private RpcApi rpcApi;
  @Mock private NotificationEventListener listener;
  @InjectMocks private TransferAdaptor transferAdaptor;
  @Mock private TaskExecutor taskExecutor;
  @Mock private SubscriptionWebSocketClient subscriptionWebSocketClient;
  private static final String BLOCK_HASH = "latestBlockhash";
  private static final Account SENDER_ACCOUNT = new Account();
  private static final String RECIPIENT_ADDRESS = "81e58SU8EHdBmSnETExsCwurbfWeCbNg9UoAFmKPyyj3";
  private static final Long LAM_PORTS = 1000L;

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
  void shouldTransferFundsSuccessfully() throws Exception {
    // given
    var expectedSignature = "transactionSignature";
    when(rpcApi.sendTransaction(any(Transaction.class), anyList(), eq(BLOCK_HASH)))
        .thenReturn(expectedSignature);

    try (MockedStatic<SubscriptionWebSocketClient> mockedStatic =
        mockStatic(SubscriptionWebSocketClient.class)) {
      mockedStatic
          .when(() -> SubscriptionWebSocketClient.getInstance(rpcClient.getEndpoint()))
          .thenReturn(subscriptionWebSocketClient);

      doNothing().when(subscriptionWebSocketClient).signatureSubscribe(anyString(), any());

      // when
      var futureTransfer =
          transferAdaptor.transferFunds(
              SENDER_ACCOUNT, RECIPIENT_ADDRESS, LAM_PORTS, BLOCK_HASH, listener);

      // then
      await()
          .atMost(Duration.ofSeconds(10))
          .untilAsserted(
              () -> {
                assertThat(futureTransfer.join()).isEqualTo(expectedSignature);
              });
      verify(subscriptionWebSocketClient).signatureSubscribe(eq(expectedSignature), eq(listener));
    }
  }

  @Test
  @SneakyThrows
  void shouldHandleRpcExceptionDuringTransfer() {
    // given
    when(rpcApi.sendTransaction(any(Transaction.class), anyList(), eq(BLOCK_HASH)))
        .thenThrow(new RuntimeException("RPC error"));

    try (MockedStatic<SubscriptionWebSocketClient> mockedStatic =
        mockStatic(SubscriptionWebSocketClient.class)) {
      mockedStatic
          .when(() -> SubscriptionWebSocketClient.getInstance(rpcClient.getEndpoint()))
          .thenReturn(subscriptionWebSocketClient);

      // when
      var result =
          transferAdaptor.transferFunds(
              SENDER_ACCOUNT, RECIPIENT_ADDRESS, LAM_PORTS, BLOCK_HASH, listener);

      // then
      await()
          .atMost(Duration.ofSeconds(5))
          .untilAsserted(
              () -> {
                assertThatThrownBy(result::join)
                    .isInstanceOf(CompletionException.class)
                    .hasCauseInstanceOf(SolanaTransactionException.class)
                    .hasRootCauseInstanceOf(RuntimeException.class)
                    .hasRootCauseMessage("RPC error");
              });

      verify(subscriptionWebSocketClient, never()).signatureSubscribe(anyString(), any());
    }
  }

  @Test
  void shouldHandleGenericExceptionDuringTransfer() throws Exception {
    when(rpcApi.sendTransaction(any(Transaction.class), anyList(), eq(BLOCK_HASH)))
        .thenThrow(new RuntimeException("Generic error"));

    try (MockedStatic<SubscriptionWebSocketClient> mockedStatic =
        mockStatic(SubscriptionWebSocketClient.class)) {
      mockedStatic
          .when(() -> SubscriptionWebSocketClient.getInstance(rpcClient.getEndpoint()))
          .thenReturn(subscriptionWebSocketClient);

      // when
      var futureTransfer =
          transferAdaptor.transferFunds(
              SENDER_ACCOUNT, RECIPIENT_ADDRESS, LAM_PORTS, BLOCK_HASH, listener);

      // then
      await()
          .atMost(Duration.ofSeconds(5))
          .untilAsserted(
              () -> {
                assertThatThrownBy(futureTransfer::join)
                    .isInstanceOf(CompletionException.class)
                    .hasCauseInstanceOf(SolanaTransactionException.class)
                    .hasRootCauseInstanceOf(RuntimeException.class)
                    .hasRootCauseMessage("Generic error");
              });

      verify(subscriptionWebSocketClient, never()).signatureSubscribe(anyString(), any());
    }
  }
}
