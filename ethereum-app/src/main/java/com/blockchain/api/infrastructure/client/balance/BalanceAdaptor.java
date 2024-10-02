package com.blockchain.api.infrastructure.client.balance;

import com.blockchain.api.application.exception.BalanceRetrievalException;
import com.blockchain.api.domain.service.balance.BalanceClient;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.blockchain.api.domain.service.balance.Contract;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;

@Slf4j
@Component
@RequiredArgsConstructor
public class BalanceAdaptor implements BalanceClient {

  private final Web3j rpcClient;

  @Override
  public CompletableFuture<BigInteger> getBalance(String address) {
    return rpcClient
        .ethGetBalance(address, DefaultBlockParameterName.LATEST)
        .sendAsync()
        .thenApply(
            balance -> {
              log.info("Balance request completed successfully for address: {}", address);
              return balance.getBalance();
            });
  }

  @Override
  public CompletableFuture<BigInteger> getNonEthBalance(String address , Contract tokenType) {
    var function =
        new Function(
            "balanceOf", List.of(new Address(address)), List.of(new TypeReference<Uint256>() {}));

    return rpcClient
        .ethCall(
            Transaction.createEthCallTransaction(
                address, tokenType.getAddress(), FunctionEncoder.encode(function)),
            DefaultBlockParameterName.LATEST)
        .sendAsync()
        .thenApply(
            ethCall -> {
              List<Type> responseValues =
                  FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
              BigInteger balance = ((Uint256) responseValues.get(0)).getValue();
              log.info(
                  "USDC balance request completed successfully for address: {}, balance: {}",
                  address,
                  balance);
              return balance;
            })
        .exceptionally(
            exception -> {
              log.error(
                  "Exception occurred while fetching USDC balance for address {}: {}",
                  address,
                  exception.getMessage());
              throw BalanceRetrievalException.withAddress(address);
            });
  }
}
