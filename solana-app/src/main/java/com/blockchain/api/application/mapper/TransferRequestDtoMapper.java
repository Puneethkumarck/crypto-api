package com.blockchain.api.application.mapper;

import com.blockchain.api.domain.service.transfer.TransferRequest;
import com.blockchain.api.model.TransferRequestDto;
import org.mapstruct.Mapper;

@Mapper
public interface TransferRequestDtoMapper {
  TransferRequest mapToDomain(TransferRequestDto transferRequestDto);
}
