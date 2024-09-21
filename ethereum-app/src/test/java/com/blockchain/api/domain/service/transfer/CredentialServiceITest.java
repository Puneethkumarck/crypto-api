package com.blockchain.api.domain.service.transfer;

import static org.assertj.core.api.Assertions.assertThat;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.web3j.crypto.Credentials;

@SpringBootTest
@ActiveProfiles("test")
@Slf4j
class CredentialServiceITest {

  @Autowired private CredentialService credentialService;

  @Test
  void shouldReturnTrue_whenAddressIsValid() {

    // when
    var result = credentialService.getCredential();

    // then
    assertThat(result).isInstanceOf(Credentials.class);
    assertThat(result.getAddress()).isEqualTo("0xbd3a24c4447e0aacdf1000d478186e16ba2c013a");
  }
}
