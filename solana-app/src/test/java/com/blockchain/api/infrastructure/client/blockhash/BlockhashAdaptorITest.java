package com.blockchain.api.infrastructure.client.blockhash;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import com.blockchain.api.infrastructure.client.BaseSolanaTest;
import com.blockchain.api.infrastructure.client.blochhash.BlockhashAdaptor;
import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class BlockhashAdaptorITest extends BaseSolanaTest {

  @Autowired private BlockhashAdaptor blockhashAdaptor;

  @Test
  void getBlockhash() {
    // given
    var expectedBlockhash = "EsoYa2E4eKxx5wGCNJ3bzecudjskF5GXcyjnRZAXHXp3";
    BaseSolanaTest.stubGetLatestBlockhash();

    // when
    var futureBlockhash = blockhashAdaptor.getLatestBlockhash();

    // then
    await()
        .atMost(Duration.ofSeconds(10))
        .untilAsserted(() -> assertThat(futureBlockhash.join()).isEqualTo(expectedBlockhash));
  }
}
