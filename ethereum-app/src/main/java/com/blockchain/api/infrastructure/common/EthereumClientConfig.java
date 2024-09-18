package com.blockchain.api.infrastructure.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

@Configuration
public class EthereumClientConfig {

  @Bean
  public Web3j web3j(@Value("${rpc.client.base.ethereum.url}") String quikNodeUrl) {
    return Web3j.build(new HttpService(quikNodeUrl));
  }
}
