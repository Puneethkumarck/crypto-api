package com.blockchain.api.application.exception;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class Base64EncodeTest {

  @Test
  void testBase64Encode() {
    var encode =
        Base64.getEncoder()
            .encodeToString(
                "https://patient-divine-aura.ethereum-sepolia.quiknode.pro/c5b3fafdf193c76117612895178bf8c0230e03ed"
                    .getBytes(StandardCharsets.UTF_8));
    var decode = Arrays.toString(Base64.getDecoder().decode(encode));

    log.info("encoded : {} and decoded : {} ", encode, decode);
  }
}
