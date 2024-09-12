package com.blockchain.api.domain.service.transfer;

import static org.p2p.solanaj.rpc.types.config.Commitment.CONFIRMED;

import com.blockchain.api.domain.service.airdrop.AirDropClient;
import com.blockchain.api.domain.service.blockhash.BlockhashClient;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.p2p.solanaj.core.Account;
import org.p2p.solanaj.core.PublicKey;
import org.p2p.solanaj.core.Transaction;
import org.p2p.solanaj.programs.SystemProgram;
import org.p2p.solanaj.rpc.Cluster;
import org.p2p.solanaj.rpc.RpcClient;
import org.p2p.solanaj.ws.listeners.NotificationEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Slf4j
@Ignore
class TransferServiceIT {

  @Autowired private TransferService transferService;

  @Autowired private BlockhashClient blockhashClient;

  @Autowired private AirDropClient airDropClient;

  @Test
  void transfer() {
    // given
    var request =
        TransferRequest.builder()
            .to("81e58SU8EHdBmSnETExsCwurbfWeCbNg9UoAFmKPyyj3")
            .amount(6000L)
            .build();

    // when
    transferService.transfer(request);
  }

  @Test
  @SneakyThrows
  void testTransfer() {
    long lamport = 5000L;
    PublicKey from = new PublicKey("p8guAeE7naqQcT2JMCp8Q376MLyzt5XynfGw3uCHM75");
    PublicKey to = new PublicKey("81e58SU8EHdBmSnETExsCwurbfWeCbNg9UoAFmKPyyj3");
    Account signer =
        Account.fromJson(
            new String(
                Base64.getDecoder()
                    .decode(
                        "WzE0LDEyNSwxODcsMTM2LDIzOSwxNzMsMTk4LDcwLDEzMiwxMTYsNjgsMTcxLDQxLDE5MywyMTIsMTU2LDIyMSwxNywxOTIsMTU1LDQxLDY5LDEwNCwxODUsNDcsNDAsMTU4LDI0MSwxMTMsMTcsMjQsMzEsMTIsMTgsMjUzLDE2NCw0Nyw4NSwxMjksMTk2LDExNiwxODcsNzMsMTY4LDIwMywyMDIsMTcwLDIyNCwxMTcsNDAsNjksNSwxMTYsMjAzLDUyLDE5NSwyMjUsNjksNDMsMjQ3LDgyLDIyNywxLDk2XQ==")));
    Transaction transaction = new Transaction();
    transaction.addInstruction(SystemProgram.transfer(from, to, lamport));
    final RpcClient client = new RpcClient(Cluster.DEVNET);
    var blockHash = client.getApi().getLatestBlockhash(CONFIRMED);
    var fees = client.call("getMaxRetransmitSlot", new ArrayList<>(), Long.class);
    String result = client.getApi().sendTransaction(transaction, signer, blockHash);
    log.info("Transaction Signature: {} and qn_estimatePriorityFees {}", result, fees);
  }

  // @Test
  @SneakyThrows
  void testTransferWithConfirmation() {
    long lamport = 5000L;
    PublicKey from = new PublicKey("p8guAeE7naqQcT2JMCp8Q376MLyzt5XynfGw3uCHM75");
    PublicKey to = new PublicKey("81e58SU8EHdBmSnETExsCwurbfWeCbNg9UoAFmKPyyj3");
    Account signer =
        Account.fromJson(
            new String(
                Base64.getDecoder()
                    .decode(
                        "WzE0LDEyNSwxODcsMTM2LDIzOSwxNzMsMTk4LDcwLDEzMiwxMTYsNjgsMTcxLDQxLDE5MywyMTIsMTU2LDIyMSwxNywxOTIsMTU1LDQxLDY5LDEwNCwxODUsNDcsNDAsMTU4LDI0MSwxMTMsMTcsMjQsMzEsMTIsMTgsMjUzLDE2NCw0Nyw4NSwxMjksMTk2LDExNiwxODcsNzMsMTY4LDIwMywyMDIsMTcwLDIyNCwxMTcsNDAsNjksNSwxMTYsMjAzLDUyLDE5NSwyMjUsNjksNDMsMjQ3LDgyLDIyNywxLDk2XQ==")));
    Transaction transaction = new Transaction();
    transaction.addInstruction(SystemProgram.transfer(from, to, lamport));
    final RpcClient client = new RpcClient(Cluster.DEVNET);

    NotificationEventListener listener =
        data -> log.info("Transaction confirmed with notification data: {}", data);

    var recentBlckHash = client.getApi().getLatestBlockhash(CONFIRMED);

    client.getApi().sendAndConfirmTransaction(transaction, List.of(signer), listener);

    log.info("Transaction sent and waiting for confirmation.");
  }

  @Test
  @SneakyThrows
  void testGetTransaction() {
    final RpcClient client = new RpcClient(Cluster.DEVNET);
    var transaction =
        client
            .getApi()
            .getTransaction(
                "5AyP9EFhfj8ryd1BhP6V1HHc99WFVjwuABRNLnL2CD6a41pCQh8vG7JJWTnhhwG7uEL71ToodQvS8BjHiGTgg3D1",
                CONFIRMED);
    log.info("Transaction: {}", transaction);
  }

  @Test
  @SneakyThrows
  void getClusterNodes() {
    final RpcClient client = new RpcClient(Cluster.DEVNET);
    var clusterNodes = client.getApi().getInflationRate();
    log.info("Cluster Nodes: {}", clusterNodes);
  }

  @Test
  void getLatestBlockhash() {
    var blockhash = blockhashClient.getLatestBlockhash();
    log.info("Blockhash: {}", blockhash);
  }

  @Test
  void requestAirDrop() {
    var address = "CVSvjutqskYyF1hZTyZARGjGvf8d1Pp9mJMAJ8hTMHhm";
    var amount = 6000L;
    var response = airDropClient.requestAirDrop(address, amount);
    log.info("Airdrop response: {}", response);
  }
}
