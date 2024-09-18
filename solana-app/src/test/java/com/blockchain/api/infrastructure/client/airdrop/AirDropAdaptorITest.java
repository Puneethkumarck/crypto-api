package com.blockchain.api.infrastructure.client.airdrop;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import com.blockchain.api.infrastructure.client.BaseSolanaTest;
import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AirDropAdaptorITest extends BaseSolanaTest {

  @Autowired private AirDropAdaptor airDropAdaptor;

  @Test
  void shouldAirdropSuccessfully() {
    // given
    var address = "CVSvjutqskYyF1hZTyZARGjGvf8d1Pp9mJMAJ8hTMHhm";
    var amount = 6000L;
    stubAirdrop();

    // when
    var response = airDropAdaptor.requestAirDrop(address, amount);

    // then
    var expectedSignature =
        "29UQVUfYBKxCU8R7KVjoLn51gpGqhrvc7m4gjFQ3QRGeBMQJWMZ4T75kQxewUnujnQBBepV2JNowGiRF2VpSJvRL";
    await()
        .atMost(Duration.ofSeconds(5))
        .untilAsserted(() -> assertThat(response.join()).isEqualTo(expectedSignature));
  }
}
