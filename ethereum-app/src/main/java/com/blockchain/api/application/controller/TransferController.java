package com.blockchain.api.application.controller;

import com.blockchain.api.application.exception.InvalidAddressException;
import com.blockchain.api.application.mapper.TransferRequestDtoMapper;
import com.blockchain.api.domain.service.transfer.TransferService;
import com.blockchain.api.model.EthTransferRequestDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.crypto.WalletUtils;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transfers")
@Tag(
    name = "Ethereum balance transfer",
    description = "Operations related to Ethereum account balances transfer")
public class TransferController {

  private final TransferService transferService;

  private final TransferRequestDtoMapper mapper;

  @PostMapping
  public void transferEthereumBalance(@RequestBody EthTransferRequestDto transferRequestDto) {
    log.info("Transfer request: {}", transferRequestDto);
    if (!WalletUtils.isValidAddress(transferRequestDto.to())) {
      throw InvalidAddressException.withAddress(transferRequestDto.to());
    }
    transferService.transfer(mapper.toDomain(transferRequestDto));
  }
}
