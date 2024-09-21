package com.blockchain.api.domain.service.transfer;

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;

@SpringBootTest
@ActiveProfiles("test")
@Slf4j
@Disabled
public class AddressGeneratorTest {

  private static final KeyPairGenerator keyPairGenerator;

  static {
    Security.addProvider(new BouncyCastleProvider());

    try {
      keyPairGenerator = KeyPairGenerator.getInstance("ECDSA", "BC");
    } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
      throw new IllegalStateException(e);
    }
  }

  @SneakyThrows
  @Test
  void generate_ethereum_address() {
    ECGenParameterSpec ecGenParameterSpec = new ECGenParameterSpec("secp256k1");
    keyPairGenerator.initialize(ecGenParameterSpec, new SecureRandom());
    KeyPair keyPair = keyPairGenerator.generateKeyPair();
    ECKeyPair ecKeyPair = ECKeyPair.create(keyPair);
    String address = "0x" + Keys.getAddress(ecKeyPair);
    log.info(
        "Generated address: publicKey: {} and private key : {}",
        address,
        Base64.toBase64String(keyPair.getPrivate().getEncoded()));
  }
}
