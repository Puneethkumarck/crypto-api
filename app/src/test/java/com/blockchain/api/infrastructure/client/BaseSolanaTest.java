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
public abstract class BaseSolanaTest {

  @RegisterExtension
  public static final WireMockExtension WIRE_MOCK_RULE =
      WireMockExtension.newInstance()
          .options(wireMockConfig().dynamicPort().usingFilesUnderDirectory("src/test/resources"))
          .build();

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("rpc.client.base.solana.url", WIRE_MOCK_RULE::baseUrl);
  }

  protected static void stubGetBalance() {
    WIRE_MOCK_RULE.stubFor(
        WireMock.post(WireMock.urlEqualTo("/"))
            .withRequestBody(WireMock.matchingJsonPath("$.method", WireMock.equalTo("getBalance")))
            .willReturn(
                WireMock.aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBodyFile("stub/get_balance_response_solana.json")));
  }

  protected static void stubGetMinimumBalanceForRentExemption() {
    WIRE_MOCK_RULE.stubFor(
        WireMock.post(WireMock.urlEqualTo("/"))
            .withRequestBody(
                WireMock.matchingJsonPath(
                    "$.method", WireMock.equalTo("getMinimumBalanceForRentExemption")))
            .willReturn(
                WireMock.aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBodyFile(
                        "stub/get_minimum_balance_for_rent_exemption_response_solana.json")));
  }
}
