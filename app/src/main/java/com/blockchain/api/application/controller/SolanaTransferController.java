package com.blockchain.api.application.controller;

import com.blockchain.api.application.mapper.TransferRequestDtoMapper;
import com.blockchain.api.domain.service.transfer.TransferService;
import com.blockchain.api.model.TransferRequestDto;
import com.blockchain.api.model.TransferResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transfers")
public class SolanaTransferController {

  private final TransferService transferService;

  private final TransferRequestDtoMapper mapper;

  @PostMapping
  public TransferResponseDto transfer(@Valid @RequestBody TransferRequestDto request) {
    log.info("Transfer request: {}", request);
    return mapper.mapToDto(transferService.transfer(mapper.mapToDomain(request)));
  }
}
