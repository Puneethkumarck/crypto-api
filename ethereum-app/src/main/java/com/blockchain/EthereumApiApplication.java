package com.blockchain;

import com.blockchain.api.domain.service.transfer.EthereumConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(EthereumConfigProperties.class)
public class EthereumApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(EthereumApiApplication.class, args);
  }
}
