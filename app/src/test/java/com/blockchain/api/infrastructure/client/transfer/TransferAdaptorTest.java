package com.blockchain.api.infrastructure.client.transfer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.blockchain.api.application.exception.SolanaTransactionException;
import com.blockchain.api.domain.service.airdrop.AirDropClient;
import com.blockchain.api.domain.service.balance.BalanceClient;
import com.blockchain.api.domain.service.blockhash.BlockhashClient;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.p2p.solanaj.core.Account;
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
  @Mock private BalanceClient balanceClient;
  @Mock private AirDropClient airDropClient;
  @Mock private BlockhashClient blockhashClient;
  @Mock private NotificationEventListener listener;
  @InjectMocks private TransferAdaptor transferAdaptor;
  @Mock private TaskExecutor taskExecutor;
  @Mock private SubscriptionWebSocketClient subscriptionWebSocketClient;

  @BeforeEach
  void setUp() {
    lenient().when(rpcClient.getApi()).thenReturn(rpcApi);
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
  void shouldTransferFundSuccessFully() {
    // given
    Account senderAccount = new Account();
    String recipientAddress = "p8guAeE7naqQcT2JMCp8Q376MLyzt5XynfGw3uCHM75";
    long lamports = 1_000_000L;
    String expectedSignature = "TransactionSignature";

    when(balanceClient.getBalance(senderAccount.getPublicKey().toBase58(), true))
        .thenReturn(CompletableFuture.completedFuture(BigDecimal.valueOf(lamports)));
    when(balanceClient.getMinimumBalanceForRentExemption())
        .thenReturn(CompletableFuture.completedFuture(BigDecimal.ZERO));
    when(blockhashClient.getLatestBlockhash())
        .thenReturn(CompletableFuture.completedFuture("Blockhash"));
    when(rpcApi.sendTransaction(any(), anyList(), anyString())).thenReturn(expectedSignature);

    try (var mockedStatic = mockStatic(SubscriptionWebSocketClient.class)) {
      mockedStatic
          .when(() -> SubscriptionWebSocketClient.getInstance(rpcClient.getEndpoint()))
          .thenReturn(subscriptionWebSocketClient);

      doNothing().when(subscriptionWebSocketClient).signatureSubscribe(anyString(), any());

      var futureTransfer =
          transferAdaptor.transferFunds(senderAccount, recipientAddress, lamports, listener);

      await()
          .atMost(Duration.ofSeconds(10))
          .untilAsserted(() -> assertThat(futureTransfer.join()).isEqualTo(expectedSignature));
    }
  }

  @Test
  @SneakyThrows
  void shouldTriggerAirdropForInsufficientBalanceAndTransferSuccessfully() {
    // given
    Account senderAccount = new Account();
    String recipientAddress = "p8guAeE7naqQcT2JMCp8Q376MLyzt5XynfGw3uCHM75";
    long lamports = 1_000_000L;
    String expectedSignature = "TransactionSignature";

    when(balanceClient.getBalance(senderAccount.getPublicKey().toBase58(), true))
        .thenReturn(
            CompletableFuture.completedFuture(
                BigDecimal.valueOf(500_000L))) // Initial insufficient balance
        .thenReturn(
            CompletableFuture.completedFuture(
                BigDecimal.valueOf(2_000_000L))); // Balance after airdrop
    when(balanceClient.getMinimumBalanceForRentExemption())
        .thenReturn(CompletableFuture.completedFuture(BigDecimal.ZERO));
    when(airDropClient.requestAirDrop(anyString(), anyLong()))
        .thenReturn(CompletableFuture.completedFuture("AirdropSuccess"));
    when(blockhashClient.getLatestBlockhash())
        .thenReturn(CompletableFuture.completedFuture("Blockhash"));
    when(rpcApi.sendTransaction(any(), anyList(), anyString())).thenReturn(expectedSignature);

    try (var mockedStatic = mockStatic(SubscriptionWebSocketClient.class)) {
      mockedStatic
          .when(() -> SubscriptionWebSocketClient.getInstance(rpcClient.getEndpoint()))
          .thenReturn(subscriptionWebSocketClient);

      doNothing().when(subscriptionWebSocketClient).signatureSubscribe(anyString(), any());

      // when
      var futureTransfer =
          transferAdaptor.transferFunds(senderAccount, recipientAddress, lamports, listener);

      // then
      await()
          .atMost(Duration.ofSeconds(10))
          .untilAsserted(() -> assertThat(futureTransfer.join()).isEqualTo(expectedSignature));

      verify(airDropClient, times(1))
          .requestAirDrop(senderAccount.getPublicKey().toBase58(), 1_000_000_000L);
    }
  }

  @Test
  @SneakyThrows
  void shouldThrowExceptionForInvalidAddress() {
    // given
    Account senderAccount = new Account();
    String invalidRecipientAddress = "InvalidRecipientAddress";
    long lamports = 1_000_000L;

    // when
    var futureTransfer =
        transferAdaptor.transferFunds(senderAccount, invalidRecipientAddress, lamports, listener);

    // then
    await()
        .atMost(Duration.ofSeconds(5))
        .untilAsserted(
            () -> {
              assertThatThrownBy(futureTransfer::join)
                  .isInstanceOf(CompletionException.class)
                  .hasCauseInstanceOf(SolanaTransactionException.class)
                  .hasMessageContaining("Transaction failed");
            });

    verify(airDropClient, never()).requestAirDrop(anyString(), anyLong());
    verify(rpcApi, never()).sendTransaction(any(), anyList(), anyString());
  }
}
