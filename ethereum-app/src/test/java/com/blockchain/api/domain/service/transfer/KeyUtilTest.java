package com.blockchain.api.domain.service.transfer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.java_websocket.util.Base64;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Slf4j
class KeyUtilTest {

  @Autowired private EthereumConfigProperties ethereumConfigProperties;

  private static final String MOCK_PRIVATE_KEY_BASE64 =
      "MIGNAgEAMBAGByqGSM49AgEGBSuBBAAKBHYwdAIBAQQg27O3dgMNXurJcSQYYUsrN2cubL7DJRVBbEYoawUsC5mgBwYFK4EEAAqhRANCAATcP3GFD3voS/8QPk9M8NmBxyMXWnI54ghC5H+6Ci/ATQZWWuhs/IoFlKqTOTm1DE7keFxOM9eSYkXSdZLJ9xGZ";
  private byte[] privateKeyBytes;

  @BeforeEach
  @SneakyThrows
  void setUp() {
    privateKeyBytes = Base64.decode(MOCK_PRIVATE_KEY_BASE64);
  }

  @Test
  @SneakyThrows
  void shouldGenerateECDSAKeypair() {
    // given
    var keypair = ethereumConfigProperties.keypair();

    // when
    var result = KeyUtil.loadEcdsaKeyPair(Base64.decode(keypair));

    // then
    assertNotNull(result);
  }

  @Test
  @SneakyThrows
  void shouldLoadEcdsaKeyPair() {
    // When
    var keyPair = KeyUtil.loadEcdsaKeyPair(privateKeyBytes);

    // Then
    assertNotNull(keyPair, "Generated keyPair should not be null");
    assertNotNull(keyPair.getPrivate(), "Private key should not be null");
    assertNotNull(keyPair.getPublic(), "Public key should not be null");
  }

  @Test
  @SneakyThrows
  void shouldGeneratePublicKeyFromPrivateKey() {
    // given
    BCECPrivateKey mockPrivateKey = mock(BCECPrivateKey.class);
    ECParameterSpec mockSpec = mock(ECParameterSpec.class);
    when(mockPrivateKey.getParameters()).thenReturn(mockSpec);
    when(mockSpec.getG()).thenReturn(null);

    // when
    var publicKey = KeyUtil.loadEcdsaKeyPair(privateKeyBytes).getPublic();

    assertNotNull(publicKey, "Public key should be generated from private key");
  }
}
