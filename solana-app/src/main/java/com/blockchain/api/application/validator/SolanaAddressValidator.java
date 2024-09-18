package com.blockchain.api.application.validator;

import com.blockchain.api.application.exception.InvalidAddressException;
import lombok.extern.slf4j.Slf4j;
import org.p2p.solanaj.core.PublicKey;

@Slf4j
public class SolanaAddressValidator {

  public static boolean isValidAddress(String pubkey) {
    try {
      new PublicKey(pubkey);
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }

  public static String isValidAddressOrThrow(String address) {
    log.info("Validating address: {}", address);
    if (!isValidAddress(address)) {
      throw InvalidAddressException.withAddress(address);
    }
    return address;
  }
}
