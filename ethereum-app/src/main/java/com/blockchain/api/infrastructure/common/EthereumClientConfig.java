package com.blockchain.api.infrastructure.common;

import java.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

@Configuration
public class EthereumClientConfig {

  @Bean
  public Web3j web3j(@Value("${rpc.client.base.ethereum.url}") String quikNodeUrl) {
    var url =
        quikNodeUrl.contains("http")
            ? quikNodeUrl
            : new String(Base64.getDecoder().decode(quikNodeUrl));
    return Web3j.build(new HttpService(url));
  }
}
