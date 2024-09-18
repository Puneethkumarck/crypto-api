package com.blockchain.api.application.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.blockchain.api.domain.service.transfer.TransferRequest;
import com.blockchain.api.model.TransferRequestDto;
import org.junit.jupiter.api.Test;

class TransferRequestDtoMapperTest {

  private TransferRequestDtoMapper mapper = new TransferRequestDtoMapperImpl();

  @Test
  void mapToDomain() {
    // given
    var transferRequestDto =
        TransferRequestDto.builder().amount(1000L).to("destinationAddress").build();

    // when
    var transferRequest = mapper.mapToDomain(transferRequestDto);

    // then
    var expected = TransferRequest.builder().amount(1000L).to("destinationAddress").build();

    assertThat(transferRequest).isEqualTo(expected);
  }
}
