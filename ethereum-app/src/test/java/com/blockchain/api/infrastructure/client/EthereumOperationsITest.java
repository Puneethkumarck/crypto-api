package com.blockchain.api.infrastructure.client;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.blockchain.api.domain.service.transfer.TransferClient;
import com.blockchain.api.domain.service.transfer.TransferRequest;
import com.blockchain.api.domain.service.transfer.TransferService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Slf4j
@Disabled
public class EthereumOperationsITest {

  @Autowired private TransferClient transferClient;

  @Autowired private TransferService transferService;

  @Test
  void getNonce() {
    // given
    var address = "0xbd3a24c4447e0aacdf1000d478186e16ba2c013a";

    // when
    var nonce = transferClient.getNonce(address).join();

    // then
    assertNotNull(nonce);
  }

  @Test
  void getGasPrice() {
    // when
    var gasPrice = transferClient.getGasPrice().join();

    // then
    assertNotNull(gasPrice);
  }

  @Test
  void shouldTransfer_whenTransferRequestIsValid() {
    // given
    var transferRequest =
        TransferRequest.builder()
            .to("0x5d940f3947c4ab1fbdbf1f540a10019931065f7a")
            .amount("0.01")
            .build();

    // when
    transferService.transfer(transferRequest);
  }
}
