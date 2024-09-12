package com.blockchain.api.application.validator;

import com.blockchain.api.application.exception.InvalidAddressException;
import org.p2p.solanaj.core.PublicKey;

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
    if (!isValidAddress(address)) {
      throw InvalidAddressException.withAddress(address);
    }
    return address;
  }
}
