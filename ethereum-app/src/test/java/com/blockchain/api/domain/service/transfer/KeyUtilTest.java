package com.blockchain.api.domain.service.transfer;

import static org.junit.jupiter.api.Assertions.*;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.Base64;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Slf4j
class KeyUtilTest {

  @Autowired private EthereumConfigProperties ethereumConfigProperties;

  @Test
  void shouldGenerateECDSAKeypair() {
    // given
    var keypair = ethereumConfigProperties.keypair();

    // when
    var result = KeyUtil.loadEcdsaKeyPair(Base64.decode(keypair));

    // then
    assertNotNull(result);
  }
}
