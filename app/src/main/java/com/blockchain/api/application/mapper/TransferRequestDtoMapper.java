package com.blockchain.api.application.mapper;

import com.blockchain.api.domain.service.transfer.TransferRequest;
import com.blockchain.api.domain.service.transfer.TransferResponse;
import com.blockchain.api.model.TransferRequestDto;
import com.blockchain.api.model.TransferResponseDto;
import org.mapstruct.Mapper;

@Mapper
public interface TransferRequestDtoMapper {
  TransferRequest mapToDomain(TransferRequestDto transferRequestDto);

  TransferResponseDto mapToDto(TransferResponse transferResponse);
}
