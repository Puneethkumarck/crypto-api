package com.blockchain.api.domain.service.blockhash;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Slf4j
class BlockHashServiceIT {

  @Autowired private BlockHashService blockHashService;

  @Test
  void shouldReturnBlockhash() {

    // when
    var blockHash = blockHashService.getBlockhash();

    assertNotNull(blockHash);
  }
}
