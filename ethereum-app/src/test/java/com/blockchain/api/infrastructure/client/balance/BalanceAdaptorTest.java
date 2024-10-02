package com.blockchain.api.infrastructure.client.balance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import com.blockchain.api.domain.service.balance.Contract;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthGetBalance;

@ExtendWith(MockitoExtension.class)
class BalanceAdaptorTest {
  @Mock private Web3j rpcClient;
  @InjectMocks private BalanceAdaptor balanceAdaptor;

  @Test
  void shouldReturnBalance() {
    // given
    var address = "0x1234567890abcdef";
    var expectedBalance = BigInteger.ONE;
    Request<?, EthGetBalance> ethGetBalanceRequest = mock(Request.class);
    var ethGetBalance = mock(EthGetBalance.class);
    when(ethGetBalance.getBalance()).thenReturn(expectedBalance);
    when(rpcClient.ethGetBalance(eq(address), eq(DefaultBlockParameterName.LATEST)))
        .thenAnswer(invocation -> ethGetBalanceRequest);
    var ethGetBalanceFuture = CompletableFuture.completedFuture(ethGetBalance);
    when(ethGetBalanceRequest.sendAsync()).thenAnswer(invocation -> ethGetBalanceFuture);

    // when
    var balance = balanceAdaptor.getBalance(address);

    // then
    assertThat(balance.join()).isEqualTo(expectedBalance);
  }

  @Test
  void shouldThrowInvalidAddressExceptionForNullAddress() {
    // given
    var address = "0x1234567890123456789012345678901234567890"; // Valid Ethereum address
    var expectedException = new RuntimeException("RPC client error");

    Request<?, EthCall> ethCallRequest = mock(Request.class);
    doReturn(ethCallRequest).when(rpcClient).ethCall(any(Transaction.class), any());

    when(ethCallRequest.sendAsync()).thenReturn(CompletableFuture.failedFuture(expectedException));

    // when/then
    assertThatThrownBy(() -> balanceAdaptor.getNonEthBalance(address, Contract.USDC).join())
        .isInstanceOf(CompletionException.class)
        .hasCauseInstanceOf(RuntimeException.class)
        .hasMessageContaining(
            "Rpc error while retrieving balance 0x1234567890123456789012345678901234567890");
  }
}
