package com.blockchain.api.infrastructure.common;

import static java.math.RoundingMode.UNNECESSARY;

import java.math.BigDecimal;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SolanaUtil {

  public static BigDecimal lamportsToSol(long lamports) {
    return BigDecimal.valueOf(lamports).divide(BigDecimal.valueOf(1_000_000_000), 9, UNNECESSARY);
  }
}
