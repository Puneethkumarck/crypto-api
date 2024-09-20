package com.blockchain.api.infrastructure.common;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.web3j.protocol.Web3j;

class EthereumClientConfigTest {

  @Test
  void shouldReturnWeb3jWhenUrlIsEncoded() {
    // given
    var ethereumConfig = new EthereumClientConfig();

    // when
    var result =
        ethereumConfig.web3j(
            "aHR0cHM6Ly9wYXRpZW50LWRpdmluZS1hdXJhLmV0aGVyZXVtLXNlcG9saWEucXVpa25vZGUucHJvL2M1YjNmYWZkZjE5M2M3NjExNzYxMjg5NTE3OGJmOGMwMjMwZTAzZWQ=");

    // then
    assertThat(result).isInstanceOf(Web3j.class);
  }

  @Test
  void shouldReturnWeb3WhenUrlIsNotEncoded() {
    // given
    var ethereumConfig = new EthereumClientConfig();

    // when
    var result = ethereumConfig.web3j("http://localhost:60928");

    // then
    assertThat(result).isInstanceOf(Web3j.class);
  }
}
