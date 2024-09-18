package com.blockchain.api.domain.service.transfer;

import com.blockchain.api.application.exception.AccountLoaderException;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.p2p.solanaj.core.Account;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountLoaderService {

  private final SolanaConfigProperties solanaConfigProperties;

  public Account loadSenderKeypair() {
    var secretKey = solanaConfigProperties.keypair();
    log.info("Retrieved SECRET_KEY from properties: {}", secretKey);

    return loadKeypair(secretKey);
  }

  private Account loadKeypair(String secretKey) {
    if (secretKey == null || secretKey.isBlank()) {
      throw AccountLoaderException.withMessage("SECRET_KEY not found in properties.");
    }
    return secretKey.startsWith("[") ? loadFromJsonArray(secretKey) : loadFromBase64(secretKey);
  }

  private Account loadFromJsonArray(String jsonArray) {
    return Account.fromJson(jsonArray);
  }

  private Account loadFromBase64(String base64Key) {
    return Account.fromJson(new String(Base64.getDecoder().decode(base64Key)));
  }
}
