package com.blockchain.api.application.validator;

import org.p2p.solanaj.core.PublicKey;

public class SolanaAddressValidator {

  /**
   * Validates if a given string is a valid Solana public key.
   *
   * @param pubkey - The string to validate.
   * @return A boolean indicating if the string is a valid public key.
   */
  public static boolean isValidPublicKey(String pubkey) {
    try {
      new PublicKey(pubkey);
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }
}
