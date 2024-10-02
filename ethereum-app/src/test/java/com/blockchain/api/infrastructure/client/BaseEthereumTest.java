package com.blockchain.api.infrastructure.client;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest
@ActiveProfiles("test")
@Slf4j
@ExtendWith(WireMockExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public abstract class BaseEthereumTest {

  @RegisterExtension
  public static final WireMockExtension WIRE_MOCK_RULE =
      WireMockExtension.newInstance()
          .options(wireMockConfig().dynamicPort().usingFilesUnderDirectory("src/test/resources"))
          .build();

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("rpc.client.base.ethereum.url", WIRE_MOCK_RULE::baseUrl);
  }

  protected static void stubGetBalance() {
    WIRE_MOCK_RULE.stubFor(
        WireMock.post(WireMock.urlEqualTo("/"))
            .withRequestBody(
                WireMock.matchingJsonPath("$.method", WireMock.equalTo("eth_getBalance")))
            .willReturn(
                WireMock.aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBodyFile("stub/get_balance_response_ethereum.json")));
  }

  protected static void stubGetUSDCBalance() {
    WIRE_MOCK_RULE.stubFor(
        WireMock.post(WireMock.urlEqualTo("/"))
            .withRequestBody(WireMock.matchingJsonPath("$.method", WireMock.equalTo("eth_call")))
            .willReturn(
                WireMock.aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBodyFile("stub/get_balance_response_usdc.json")));
  }

  protected static void stubGetUSDTBalance() {
    WIRE_MOCK_RULE.stubFor(
            WireMock.post(WireMock.urlEqualTo("/"))
                    .withRequestBody(WireMock.matchingJsonPath("$.method", WireMock.equalTo("eth_call")))
                    .willReturn(
                            WireMock.aResponse()
                                    .withStatus(200)
                                    .withHeader("Content-Type", "application/json")
                                    .withBodyFile("stub/get_balance_response_usdt.json")));
  }

  protected static void stubGetNonce() {
    WIRE_MOCK_RULE.stubFor(
        WireMock.post(WireMock.urlEqualTo("/"))
            .withRequestBody(
                WireMock.matchingJsonPath("$.method", WireMock.equalTo("eth_getTransactionCount")))
            .willReturn(
                WireMock.aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBodyFile("stub/get_nonce_response_ethereum.json")));
  }

  protected static void stubGasPrice() {
    WIRE_MOCK_RULE.stubFor(
        WireMock.post(WireMock.urlEqualTo("/"))
            .withRequestBody(
                WireMock.matchingJsonPath("$.method", WireMock.equalTo("eth_gasPrice")))
            .willReturn(
                WireMock.aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBodyFile("stub/get_gasprice_response_ethereum.json")));
  }

  protected static void stubSendTransaction() {
    WIRE_MOCK_RULE.stubFor(
        WireMock.post(WireMock.urlEqualTo("/"))
            .withRequestBody(
                WireMock.matchingJsonPath("$.method", WireMock.equalTo("eth_sendRawTransaction")))
            .willReturn(
                WireMock.aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBodyFile("stub/send_transaction_response_ethereum.json")));
  }
}
