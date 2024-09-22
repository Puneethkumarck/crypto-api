package com.blockchain.api.domain.service.transfer;

import static com.blockchain.api.domain.service.transfer.TransactionFeeService.GAS_LIMIT;

import com.blockchain.api.application.exception.InsufficientBalanceException;
import com.blockchain.api.domain.service.balance.BalanceService;
import java.math.BigDecimal;
import java.math.BigInteger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

@RequiredArgsConstructor
@Service
@Slf4j
public class TransferService {

  private final TransferClient transferClient;

  private final TransactionFeeService transactionFeeService;

  private final BalanceService balanceService;

  private final CredentialService credentialService;
  private static final long SEPOLIA_CHAIN_ID = 11155111L;

  public void transfer(TransferRequest transactionRequest) {
    var toAddress = transactionRequest.to();
    var amount = transactionRequest.amount();
    isBalanceSufficient(amount);
    var fromAddress = credentialService.getCredential().getAddress();
    var nonce = transferClient.getNonce(fromAddress).join();
    var gasPrice = transferClient.getGasPrice().join();

    RawTransaction transaction = buildTransaction(nonce, gasPrice, toAddress, amount);

    byte[] signedTx = signTransaction(transaction);

    transferClient.transfer(Numeric.toHexString(signedTx));
  }

  private RawTransaction buildTransaction(
      final BigInteger nonce,
      final BigInteger gasPrice,
      final String toAddress,
      final String amount) {
    log.info(
        "Building transaction for toAddress: {} and amount: {} nonce: {} gasPrice: {} gasLimit: {} amount in wei: {}",
        toAddress,
        amount,
        nonce,
        gasPrice,
        BigInteger.valueOf(GAS_LIMIT.longValue()),
        Convert.toWei(amount, Convert.Unit.ETHER).toBigInteger());
    return RawTransaction.createEtherTransaction(
        nonce,
        gasPrice,
        BigInteger.valueOf(GAS_LIMIT.longValue()),
        toAddress,
        Convert.toWei(amount, Convert.Unit.ETHER).toBigInteger());
  }

  private byte[] signTransaction(RawTransaction transaction) {
    return TransactionEncoder.signMessage(
        transaction, SEPOLIA_CHAIN_ID, credentialService.getCredential());
  }

  private void isBalanceSufficient(String amount) {
    var toAddress = credentialService.getCredential().getAddress();
    if (!balanceService.isBalanceSufficient(toAddress, new BigDecimal(amount))) {
      throw InsufficientBalanceException.create(
          "Insufficient balance for address: %s".formatted(toAddress));
    }
  }
}
