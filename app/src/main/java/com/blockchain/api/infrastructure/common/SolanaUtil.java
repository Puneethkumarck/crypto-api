package com.blockchain.api.infrastructure.common;

import static java.math.RoundingMode.UNNECESSARY;

import com.blockchain.api.application.exception.InvalidAddressException;
import java.math.BigDecimal;
import lombok.extern.slf4j.Slf4j;
import org.p2p.solanaj.core.PublicKey;

@Slf4j
public class SolanaUtil {
  public static Boolean isValidAddress(String address) {
    try {
      PublicKey.valueOf(address);
      return true;
    } catch (IllegalArgumentException e) {
      log.error("Invalid address: {}", address, e);
      return false;
    }
  }

  public static BigDecimal lamportsToSol(long lamports) {
    return BigDecimal.valueOf(lamports).divide(BigDecimal.valueOf(1_000_000_000), 9, UNNECESSARY);
  }

  public static String isValidAddressOrThrow(String address) {
    if (!isValidAddress(address)) {
      throw InvalidAddressException.withAddress(address);
    }
    return address;
  }
}
