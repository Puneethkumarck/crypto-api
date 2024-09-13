package com.blockchain.api;

import com.blockchain.api.domain.service.transfer.SolanaConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(SolanaConfigProperties.class)
public class CryptoApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(CryptoApiApplication.class, args);
  }
}
