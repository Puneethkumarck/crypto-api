package com.blockchain.api.domain.service.balance;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.blockchain.api.domain.service.airdrop.AirDropClient;
import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.p2p.solanaj.core.Account;

@ExtendWith(MockitoExtension.class)
class BalanceServiceTest {
  @InjectMocks private BalanceService balanceService;
  @Mock private BalanceClient balanceClient;
  @Mock private AirDropClient airDropClient;
  private static final Account SENDER_ACCOUNT = new Account();
  private static final String RECIPIENT_ADDRESS = "recipientAddress";
  private static final long LAM_PORTS = 5000L;

  @Test
  void shouldReturnSolanaBalance() {
    // given
    var address = "someAddress";
    var expectedBalance = new BigDecimal("1.999603");
    when(balanceClient.getBalance(address, false)).thenReturn(completedFuture(expectedBalance));

    // when
    var actualBalance = balanceService.getSolanaBalance(address);

    // then
    assertThat(actualBalance).isEqualTo(expectedBalance);
  }

  @Test
  void shouldReturnMinimumBalanceForRentExemption() {
    // given
    var expectedMinimumBalance = new BigDecimal("890880");
    when(balanceClient.getMinimumBalanceForRentExemption())
        .thenReturn(completedFuture(expectedMinimumBalance));

    // when
    var actualMinimumBalance = balanceService.getMinimumBalanceForRentExemption();

    // then
    assertThat(actualMinimumBalance).isEqualTo(expectedMinimumBalance);
  }

  @Test
  @SneakyThrows
  void shouldEnsureSufficientBalanceWithoutAirdrop() {
    // given
    var senderAddress = SENDER_ACCOUNT.getPublicKey().toBase58();
    var balance = new BigDecimal("1000000000");
    var minRentExemption = new BigDecimal("890880");

    when(balanceClient.getBalance(senderAddress, true))
        .thenReturn(CompletableFuture.completedFuture(balance));
    when(balanceClient.getBalance(RECIPIENT_ADDRESS, true))
        .thenReturn(CompletableFuture.completedFuture(balance));
    when(balanceClient.getMinimumBalanceForRentExemption())
        .thenReturn(CompletableFuture.completedFuture(minRentExemption));

    // when
    balanceService.ensureSufficientBalanceForTransfer(SENDER_ACCOUNT, RECIPIENT_ADDRESS, LAM_PORTS);

    // then
    verify(balanceClient, times(1)).getBalance(senderAddress, true);
    verify(balanceClient, times(1)).getBalance(RECIPIENT_ADDRESS, true);
    verify(balanceClient, times(2)).getMinimumBalanceForRentExemption();
    verify(airDropClient, never()).requestAirDrop(anyString(), anyLong());
  }

  @Test
  void shouldRequestAirdropForSenderWhenBalanceIsInsufficient() {
    // given
    var senderAddress = SENDER_ACCOUNT.getPublicKey().toBase58();
    var insufficientBalance = new BigDecimal("500");
    var minRentExemption = new BigDecimal("890880");
    var sufficientBalance = new BigDecimal("1000000000");

    when(balanceClient.getBalance(senderAddress, true))
        .thenReturn(CompletableFuture.completedFuture(insufficientBalance));
    when(balanceClient.getBalance(RECIPIENT_ADDRESS, true))
        .thenReturn(CompletableFuture.completedFuture(sufficientBalance));
    when(balanceClient.getMinimumBalanceForRentExemption())
        .thenReturn(CompletableFuture.completedFuture(minRentExemption));
    when(airDropClient.requestAirDrop(senderAddress, 1_000_000_000L))
        .thenReturn(CompletableFuture.completedFuture("transactionSignature"));

    // when
    balanceService.ensureSufficientBalanceForTransfer(SENDER_ACCOUNT, RECIPIENT_ADDRESS, LAM_PORTS);

    // then
    verify(balanceClient, times(1)).getBalance(senderAddress, true);
    verify(balanceClient, times(1)).getBalance(RECIPIENT_ADDRESS, true);
    verify(balanceClient, times(2)).getMinimumBalanceForRentExemption();
    verify(airDropClient, times(1)).requestAirDrop(eq(senderAddress), eq(1_000_000_000L));
  }

  @Test
  void shouldRequestAirdropForRecipientWhenBalanceIsInsufficient() {
    // given
    String senderAddress = SENDER_ACCOUNT.getPublicKey().toBase58();
    var sufficientBalance = new BigDecimal("1000000000");
    BigDecimal insufficientBalance = new BigDecimal("500");
    BigDecimal minRentExemption = new BigDecimal("890880");

    when(balanceClient.getBalance(senderAddress, true))
        .thenReturn(CompletableFuture.completedFuture(sufficientBalance));
    when(balanceClient.getBalance(RECIPIENT_ADDRESS, true))
        .thenReturn(CompletableFuture.completedFuture(insufficientBalance));
    when(balanceClient.getMinimumBalanceForRentExemption())
        .thenReturn(CompletableFuture.completedFuture(minRentExemption));
    when(airDropClient.requestAirDrop(RECIPIENT_ADDRESS, 1_000_000_000L))
        .thenReturn(CompletableFuture.completedFuture("transactionSignature"));

    // when
    balanceService.ensureSufficientBalanceForTransfer(SENDER_ACCOUNT, RECIPIENT_ADDRESS, LAM_PORTS);

    // then
    verify(balanceClient, times(1)).getBalance(senderAddress, true);
    verify(balanceClient, times(1)).getBalance(RECIPIENT_ADDRESS, true);
    verify(balanceClient, times(2)).getMinimumBalanceForRentExemption();
    verify(airDropClient, times(1)).requestAirDrop(eq(RECIPIENT_ADDRESS), eq(1_000_000_000L));
  }

  @Test
  void shouldEnsureSufficientBalanceForSenderAndRecipientWhenBalanceIsInsufficient() {
    // given
    var insufficientBalance = new BigDecimal("500");
    var minRentExemption = new BigDecimal("890880");

    when(balanceClient.getBalance(SENDER_ACCOUNT.getPublicKey().toBase58(), true))
        .thenReturn(CompletableFuture.completedFuture(insufficientBalance));
    when(balanceClient.getBalance(RECIPIENT_ADDRESS, true))
        .thenReturn(CompletableFuture.completedFuture(insufficientBalance));
    when(balanceClient.getMinimumBalanceForRentExemption())
        .thenReturn(CompletableFuture.completedFuture(minRentExemption));
    when(airDropClient.requestAirDrop(SENDER_ACCOUNT.getPublicKey().toBase58(), 1_000_000_000L))
        .thenReturn(CompletableFuture.completedFuture("transactionSignature"));
    when(airDropClient.requestAirDrop(RECIPIENT_ADDRESS, 1_000_000_000L))
        .thenReturn(CompletableFuture.completedFuture("transactionSignature"));

    // when
    balanceService.ensureSufficientBalanceForTransfer(SENDER_ACCOUNT, RECIPIENT_ADDRESS, LAM_PORTS);

    // then
    verify(balanceClient, times(1)).getBalance(SENDER_ACCOUNT.getPublicKey().toBase58(), true);
    verify(balanceClient, times(1)).getBalance(RECIPIENT_ADDRESS, true);
    verify(balanceClient, times(2)).getMinimumBalanceForRentExemption();
    verify(airDropClient, times(1))
        .requestAirDrop(SENDER_ACCOUNT.getPublicKey().toBase58(), 1_000_000_000L);
    verify(airDropClient, times(1)).requestAirDrop(RECIPIENT_ADDRESS, 1_000_000_000L);
  }
}
