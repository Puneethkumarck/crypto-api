package com.blockchain.api.infrastructure.client.transfer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.blockchain.api.application.exception.NonceRetrievalException;
import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;

@ExtendWith(MockitoExtension.class)
class TransferAdaptorTest {
  @Mock private Web3j rpcClient;
  @InjectMocks private TransferAdaptor transferAdaptor;

  @Test
  @SneakyThrows
  void shouldReturnNonce() {
    // given
    var address = "0x1234567890abcdef";
    var expectedNonce = BigInteger.valueOf(42);
    Request<?, EthGetTransactionCount> ethGetTransactionCountRequest = mock(Request.class);
    var ethGetTransactionCount = mock(EthGetTransactionCount.class);
    when(ethGetTransactionCount.getTransactionCount()).thenReturn(expectedNonce);
    when(rpcClient.ethGetTransactionCount(eq(address), eq(DefaultBlockParameterName.LATEST)))
        .thenAnswer(invocation -> ethGetTransactionCountRequest);
    var ethGetTransactionCountFuture = CompletableFuture.completedFuture(ethGetTransactionCount);
    when(ethGetTransactionCountRequest.sendAsync())
        .thenAnswer(invocation -> ethGetTransactionCountFuture);

    // When
    var nonceFuture = transferAdaptor.getNonce(address);

    // Then
    assertEquals(expectedNonce, nonceFuture.get());
    verify(rpcClient).ethGetTransactionCount(eq(address), eq(DefaultBlockParameterName.LATEST));
    verify(ethGetTransactionCountRequest).sendAsync();
    verify(ethGetTransactionCount).getTransactionCount();
  }

  @Test
  void shouldThrowNonceRetrievalException_whenNonceRequestFails() {
    // given
    var address = "0x1234567890abcdef";
    Request<?, EthGetTransactionCount> ethGetTransactionCountRequest = mock(Request.class);
    when(rpcClient.ethGetTransactionCount(eq(address), eq(DefaultBlockParameterName.LATEST)))
        .thenAnswer(invocation -> ethGetTransactionCountRequest);
    when(ethGetTransactionCountRequest.sendAsync())
        .thenReturn(CompletableFuture.failedFuture(new RuntimeException("RPC error")));

    // when
    var result = transferAdaptor.getNonce(address);

    // then
    assertThatThrownBy(result::join)
        .isInstanceOf(CompletionException.class)
        .hasCauseInstanceOf(NonceRetrievalException.class)
        .hasRootCauseInstanceOf(RuntimeException.class)
        .hasRootCauseMessage("Unable to retrieve nonce for address: %s".formatted(address));

    verify(rpcClient).ethGetTransactionCount(eq(address), eq(DefaultBlockParameterName.LATEST));
  }

  @Test
  void shouldRetrieveGasPrice() {
    // given
    when(rpcClient.ethGasPrice()).thenReturn(mock(Request.class));
    var ethGasPrice = mock(EthGasPrice.class);
    var gasPrice = BigInteger.valueOf(42);
    when(ethGasPrice.getGasPrice()).thenReturn(gasPrice);
    when(rpcClient.ethGasPrice().sendAsync())
        .thenReturn(CompletableFuture.completedFuture(ethGasPrice));

    // when
    var result = transferAdaptor.getGasPrice();

    // then
    assertThat(result.join()).isEqualTo(gasPrice);
  }
}
