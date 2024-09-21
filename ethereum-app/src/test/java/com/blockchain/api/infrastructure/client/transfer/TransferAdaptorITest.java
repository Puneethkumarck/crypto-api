package com.blockchain.api.infrastructure.client.transfer;

import static org.assertj.core.api.Assertions.assertThat;

import com.blockchain.api.infrastructure.client.BaseEthereumTest;
import java.math.BigInteger;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Slf4j
class TransferAdaptorITest extends BaseEthereumTest {

  @Autowired private TransferAdaptor transferAdaptor;

  @Test
  void getNonce() {
    // given
    var address = "0xbd3a24c4447e0aacdf1000d478186e16ba2c013a";
    stubGetNonce();

    // when
    var nonce = transferAdaptor.getNonce(address).join();

    // then
    assertThat(nonce).isEqualTo(2);
  }

  @Test
  void getGasPrice() {
    // given
    stubGasPrice();

    // when
    var gasPrice = transferAdaptor.getGasPrice().join();

    // then
    assertThat(gasPrice).isEqualByComparingTo(new BigInteger("11175538793"));
  }
}
