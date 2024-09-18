package com.blockchain.api.application.controller;

import com.blockchain.api.application.exception.InvalidAddressException;
import com.blockchain.api.application.mapper.BalanceDtoMapper;
import com.blockchain.api.domain.service.balance.BalanceService;
import com.blockchain.api.model.ApiError;
import com.blockchain.api.model.BalanceDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.crypto.WalletUtils;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/balances")
@Tag(
    name = "Ethereum Balance",
    description = "Operations related to fetching Ethereum account balances")
public class EthereumBalanceController {

  private final BalanceDtoMapper mapper;
  private final BalanceService balanceService;

  @Operation(
      summary = "Get Ethereum Balance",
      description = "Fetch the balance of a Ethereum account by its address")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Successfully retrieved balance",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BalanceDto.class))),
    @ApiResponse(
        responseCode = "400",
        description = "Invalid address format",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiError.class))),
    @ApiResponse(
        responseCode = "500",
        description = "Internal server error",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiError.class)))
  })
  @GetMapping("/{address}")
  public BalanceDto getBalance(@PathVariable("address") String address) {
    log.info("Fetching balance for address: {}", address);
    if (!WalletUtils.isValidAddress(address)) {
      throw InvalidAddressException.withAddress(address);
    }
    return mapper.mapToDto(address, balanceService.getEthereumBalance(address).toString());
  }
}
