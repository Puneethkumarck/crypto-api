package com.blockchain.api.application.mapper;

import com.blockchain.api.domain.service.transfer.TransferRequest;
import com.blockchain.api.model.EthTransferRequestDto;
import org.mapstruct.Mapper;

@Mapper
public interface TransferRequestDtoMapper {
  TransferRequest toDomain(EthTransferRequestDto transferRequestDto);
}
