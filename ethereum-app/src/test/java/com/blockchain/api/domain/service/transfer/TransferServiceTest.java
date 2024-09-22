package com.blockchain.api.domain.service.transfer;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.concurrent.CompletableFuture.failedFuture;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.blockchain.api.application.exception.NonceRetrievalException;
import com.blockchain.api.domain.service.balance.BalanceService;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.web3j.crypto.Credentials;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {
  @Mock private TransferClient transferClient;

  @Mock private TransactionFeeService transactionFeeService;

  @Mock private CredentialService credentialService;

  @Mock private BalanceService balanceService;

  @InjectMocks private TransferService transferService;

  @Test
  void shouldTransferEth() {
    // given
    var address = "0xbd3a24c4447e0aacdf1000d478186e16ba2c013a";
    var fromAddress = "0x476c88ed464efd251a8b18eb84785f7c46807873";

    TransferRequest transactionRequest =
        TransferRequest.builder().to(address).amount("0.1").build();

    when(transferClient.getNonce(fromAddress)).thenReturn(completedFuture(BigInteger.ONE));
    when(transferClient.getGasPrice()).thenReturn(completedFuture(BigInteger.ONE));
    when(credentialService.getCredential()).thenReturn(Credentials.create("0x123"));
    when(transactionFeeService.getTransactionFee()).thenReturn(new BigDecimal("0.1"));
    when(balanceService.isBalanceSufficient(fromAddress, new BigDecimal("0.2"))).thenReturn(true);

    // when
    transferService.transfer(transactionRequest);

    // then
    verify(transferClient).getNonce(fromAddress);
    verify(transferClient).getGasPrice();
    verify(transferClient).transfer(anyString());
  }

  @Test
  void shouldThrowNonceRetrievalException() {
    // given
    var address = "0xbd3a24c4447e0aacdf1000d478186e16ba2c013a";
    var fromAddress = "0x476c88ed464efd251a8b18eb84785f7c46807873";
    TransferRequest transactionRequest =
        TransferRequest.builder().to(address).amount("0.1").build();

    when(transferClient.getNonce(fromAddress))
        .thenReturn(failedFuture(new NonceRetrievalException(address)));
    when(credentialService.getCredential()).thenReturn(Credentials.create("0x123"));
    when(transactionFeeService.getTransactionFee()).thenReturn(new BigDecimal("0.1"));
    when(balanceService.isBalanceSufficient(fromAddress, new BigDecimal("0.2"))).thenReturn(true);

    // when
    var thrownException =
        assertThrows(CompletionException.class, () -> transferService.transfer(transactionRequest));

    // then
    assertThat(thrownException)
        .isInstanceOf(CompletionException.class)
        .hasRootCauseInstanceOf(NonceRetrievalException.class)
        .hasRootCauseMessage(address);
    verify(transferClient).getNonce(fromAddress);
    verify(transferClient, times(0)).getGasPrice();
    verify(transferClient, times(0)).transfer(anyString());
  }

  @Test
  void shouldThrowGasPriceRetrievalException() {
    // given
    var address = "0xbd3a24c4447e0aacdf1000d478186e16ba2c013a";
    var fromAddress = "0x476c88ed464efd251a8b18eb84785f7c46807873";
    var transactionRequest = TransferRequest.builder().to(address).amount("0.1").build();

    when(transferClient.getNonce(fromAddress)).thenReturn(completedFuture(BigInteger.ONE));
    when(credentialService.getCredential()).thenReturn(Credentials.create("0x123"));
    when(transactionFeeService.getTransactionFee()).thenReturn(new BigDecimal("0.1"));
    when(balanceService.isBalanceSufficient(fromAddress, new BigDecimal("0.2"))).thenReturn(true);
    when(transferClient.getGasPrice()).thenReturn(failedFuture(new RuntimeException("RPC error")));
    when(transferClient.getGasPrice())
        .thenReturn(CompletableFuture.failedFuture(new RuntimeException("RPC error")));

    // when
    var thrownException =
        assertThrows(CompletionException.class, () -> transferService.transfer(transactionRequest));

    // then
    assertThat(thrownException)
        .isInstanceOf(CompletionException.class)
        .hasRootCauseInstanceOf(RuntimeException.class)
        .hasRootCauseMessage("RPC error");
    verify(transferClient).getNonce(fromAddress);
    verify(transferClient).getGasPrice();
    verify(transferClient, times(0)).transfer(anyString());
  }
}
