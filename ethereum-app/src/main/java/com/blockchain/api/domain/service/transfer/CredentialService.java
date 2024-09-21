package com.blockchain.api.domain.service.transfer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;

@RequiredArgsConstructor
@Component
@Slf4j
public class CredentialService {

  private final EthereumConfigProperties ethereumConfigProperties;

  public Credentials getCredential() {
    return Credentials.create(
        ECKeyPair.create(
            KeyUtil.loadEcdsaKeyPair(Base64.decode(ethereumConfigProperties.keypair()))));
  }
}
