package com.blockchain.api.domain.service.transfer;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

import com.blockchain.api.domain.service.balance.BalanceService;
import com.blockchain.api.domain.service.blockhash.BlockhashClient;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.p2p.solanaj.core.Account;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {
  @Mock private TransferClient transferClient;
  @Mock private AccountLoaderService accountLoaderService;
  @Mock private BlockhashClient blockhashClient;
  @Mock private LogWebSocketNotification listener;
  @Mock private BalanceService balanceService;
  @InjectMocks private TransferService transferService;

  @Test
  void shouldTransferAmountSuccessfully() {
    // given
    var mockAccount = new Account();
    var request =
        TransferRequest.builder()
            .to("81e58SU8EHdBmSnETExsCwurbfWeCbNg9UoAFmKPyyj3")
            .amount(6000L)
            .build();

    when(accountLoaderService.loadSenderKeypair()).thenReturn(mockAccount);
    when(blockhashClient.getLatestBlockhash())
        .thenReturn(CompletableFuture.completedFuture("latestBlockhash"));

    var mockFuture = CompletableFuture.completedFuture("transactionSignature");
    when(transferClient.transferFunds(
            eq(mockAccount),
            eq(request.to()),
            eq(request.amount()),
            eq("latestBlockhash"),
            eq(listener)))
        .thenReturn(mockFuture);

    // when & then
    assertDoesNotThrow(() -> transferService.transfer(request));

    verify(accountLoaderService).loadSenderKeypair();
    verify(transferClient)
        .transferFunds(
            eq(mockAccount),
            eq(request.to()),
            eq(request.amount()),
            eq("latestBlockhash"),
            eq(listener));
    verify(blockhashClient).getLatestBlockhash();
    verify(balanceService)
        .ensureSufficientBalanceForTransfer(
            eq(mockAccount), eq(request.to()), eq(request.amount()));
  }

  @Test
  void shouldHandleTransferFailure() {
    // given
    var mockAccount = new Account();
    var request =
        TransferRequest.builder()
            .to("81e58SU8EHdBmSnETExsCwurbfWeCbNg9UoAFmKPyyj3")
            .amount(6000L)
            .build();

    when(accountLoaderService.loadSenderKeypair()).thenReturn(mockAccount);
    when(blockhashClient.getLatestBlockhash())
        .thenReturn(CompletableFuture.completedFuture("latestBlockhash"));

    CompletableFuture<String> mockFuture = new CompletableFuture<>();
    mockFuture.completeExceptionally(new RuntimeException("Transfer failed"));
    when(transferClient.transferFunds(
            eq(mockAccount),
            eq(request.to()),
            eq(request.amount()),
            eq("latestBlockhash"),
            eq(listener)))
        .thenReturn(mockFuture);

    // when & then
    assertDoesNotThrow(() -> transferService.transfer(request));

    verify(accountLoaderService).loadSenderKeypair();
    verify(transferClient)
        .transferFunds(
            eq(mockAccount),
            eq(request.to()),
            eq(request.amount()),
            eq("latestBlockhash"),
            eq(listener));
    verify(blockhashClient).getLatestBlockhash();
    verify(balanceService)
        .ensureSufficientBalanceForTransfer(
            eq(mockAccount), eq(request.to()), eq(request.amount()));
  }
}
