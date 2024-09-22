package com.blockchain.api.application.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.blockchain.api.domain.service.transfer.TransferRequest;
import com.blockchain.api.model.EthTransferRequestDto;
import org.junit.jupiter.api.Test;

class TransferRequestDtoMapperTest {

  private final TransferRequestDtoMapper mapper = new TransferRequestDtoMapperImpl();

  @Test
  void toDomain() {
    var transferRequestDto = EthTransferRequestDto.builder().to("0x123").amount("100").build();

    var transferRequest = mapper.toDomain(transferRequestDto);

    var expected = TransferRequest.builder().to("0x123").amount("100").build();

    assertThat(transferRequest).usingRecursiveComparison().isEqualTo(expected);
  }
}
