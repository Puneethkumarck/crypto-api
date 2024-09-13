package com.blockchain.api.domain.service.transfer;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    var mockFuture = CompletableFuture.completedFuture("transactionSignature");
    when(transferClient.transferFunds(
            eq(mockAccount), eq(request.to()), eq(request.amount()), any()))
        .thenReturn(mockFuture);

    // when & then
    assertDoesNotThrow(() -> transferService.transfer(request));

    verify(accountLoaderService).loadSenderKeypair();
    verify(transferClient)
        .transferFunds(eq(mockAccount), eq(request.to()), eq(request.amount()), any());
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

    CompletableFuture<String> mockFuture = new CompletableFuture<>();
    mockFuture.completeExceptionally(new RuntimeException("Transfer failed"));
    when(transferClient.transferFunds(
            eq(mockAccount), eq(request.to()), eq(request.amount()), any()))
        .thenReturn(mockFuture);

    // when & then
    assertDoesNotThrow(() -> transferService.transfer(request));

    verify(accountLoaderService).loadSenderKeypair();
    verify(transferClient)
        .transferFunds(eq(mockAccount), eq(request.to()), eq(request.amount()), any());
  }
}
