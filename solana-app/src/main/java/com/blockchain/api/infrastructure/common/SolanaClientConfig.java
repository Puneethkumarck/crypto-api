package com.blockchain.api.infrastructure.common;

import java.util.concurrent.Executor;
import org.p2p.solanaj.rpc.RpcClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class SolanaClientConfig {

  /**
   * Read Solana blockchain Mainnet - https://api.mainnet-beta.solana.com Devnet
   * -https://api.devnet.solana.com Testnet - https://api.testnet.solana.com
   *
   * @return RpcClient
   */
  @Bean
  @RefreshScope
  RpcClient solanaNodeClient(@Value("${rpc.client.base.solana.url:" + "" + "}") String rpcUrl) {
    return new RpcClient(rpcUrl);
  }

  @Bean(name = "taskExecutor")
  public Executor taskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(10);
    executor.setMaxPoolSize(20);
    executor.setQueueCapacity(30);
    executor.setThreadNamePrefix("Async-");
    executor.initialize();
    return executor;
  }
}
