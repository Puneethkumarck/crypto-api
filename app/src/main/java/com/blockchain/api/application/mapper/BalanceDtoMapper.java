package com.blockchain.api.application.mapper;

import com.blockchain.api.model.BalanceDto;
import org.mapstruct.Mapper;

@Mapper
public interface BalanceDtoMapper {

  BalanceDto mapToDto(String address, String balance);
}
