package com.blockchain.api.application;

import com.blockchain.api.infrastructure.client.BaseSolanaTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
public abstract class BaseControllerIT extends BaseSolanaTest {
  @Autowired protected MockMvc mockMvc;
  @Autowired protected ObjectMapper objectMapper;
}
