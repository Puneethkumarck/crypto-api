package com.blockchain.api.domain.service.blockhash;

import java.util.concurrent.CompletableFuture;

public interface BlockhashClient {
  CompletableFuture<String> getLatestBlockhash();
}
