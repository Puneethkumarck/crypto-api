package com.blockchain.api.domain.service.transfer;

import static org.assertj.core.api.Assertions.assertThat;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Slf4j
class EthereumConfigPropertiesITest {

  @Autowired private EthereumConfigProperties ethereumConfigProperties;

  @Test
  void shouldReturnPropertyValue() {
    // given
    var keypair =
        "MIGNAgEAMBAGByqGSM49AgEGBSuBBAAKBHYwdAIBAQQg27O3dgMNXurJcSQYYUsrN2cubL7DJRVBbEYoawUsC5mgBwYFK4EEAAqhRANCAATcP3GFD3voS/8QPk9M8NmBxyMXWnI54ghC5H+6Ci/ATQZWWuhs/IoFlKqTOTm1DE7keFxOM9eSYkXSdZLJ9xGZ";

    // when
    var result = ethereumConfigProperties.keypair();

    // then
    assertThat(result).isEqualTo(keypair);
  }
}
