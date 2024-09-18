package com.blockchain.api.infrastructure.client.blochhash;

import static org.p2p.solanaj.rpc.types.config.Commitment.FINALIZED;

import com.blockchain.api.domain.service.blockhash.BlockhashClient;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.p2p.solanaj.rpc.RpcClient;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class BlockhashAdaptor implements BlockhashClient {

  private final RpcClient solanaNodeClient;

  private final TaskExecutor taskExecutor;

  @Override
  public CompletableFuture<String> getLatestBlockhash() {
    log.info("Fetching recent blockhash");
    return CompletableFuture.supplyAsync(this::fetchBlockhash, taskExecutor);
  }

  @SneakyThrows
  private String fetchBlockhash() {
    return solanaNodeClient.getApi().getLatestBlockhash(FINALIZED);
  }
}
