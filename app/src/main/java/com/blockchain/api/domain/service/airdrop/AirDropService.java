package com.blockchain.api.domain.service.airdrop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AirDropService {

  private final AirDropClient airDropClient;

  public String requestAirDrop(String address, long amount) {
    return airDropClient.requestAirDrop(address, amount).join();
  }
}
