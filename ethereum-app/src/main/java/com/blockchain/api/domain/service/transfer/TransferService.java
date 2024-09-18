package com.blockchain.api.domain.service.transfer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class TransferService {

  private final TransactionFeeService transactionFeeService;

  private final TransferClient transferClient;

  public void transfer(TransferRequest transactionRequest) {}
}
