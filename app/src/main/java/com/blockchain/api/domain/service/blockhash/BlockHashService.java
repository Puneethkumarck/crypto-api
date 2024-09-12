package com.blockchain.api.domain.service.blockhash;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlockHashService {

  private final BlockhashClient blockhashClient;

  public String getBlockhash() {
    log.info("Fetching blockhash");
    return blockhashClient.getLatestBlockhash().join();
  }
}
