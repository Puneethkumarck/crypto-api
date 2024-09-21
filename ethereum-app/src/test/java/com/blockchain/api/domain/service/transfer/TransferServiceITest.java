package com.blockchain.api.domain.service.transfer;

import static org.junit.jupiter.api.Assertions.*;

import com.blockchain.api.infrastructure.client.BaseEthereumTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Slf4j
class TransferServiceITest extends BaseEthereumTest {

  @Autowired private TransferService transferService;

  @Test
  void transfer() {
    // given
    var transferRequest =
        TransferRequest.builder()
            .to("0x5d940f3947c4ab1fbdbf1f540a10019931065f7a")
            .amount("0.01")
            .build();
    stubGetNonce();
    stubGasPrice();
    stubSendTransaction();

    // when
    transferService.transfer(transferRequest);
  }
}
