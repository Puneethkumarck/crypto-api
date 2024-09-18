package com.blockchain.api.domain.service.transfer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

import java.util.AbstractMap;
import java.util.HashMap;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.p2p.solanaj.ws.SignatureNotification;
import org.slf4j.Logger;

@ExtendWith(MockitoExtension.class)
class LogWebSocketNotificationTest {

  @InjectMocks private LogWebSocketNotification logWebSocketNotification;

  @Mock private Logger logger;

  @Test
  void shouldLogNotificationForAbstractMap() {
    // given
    AbstractMap<String, String> mockData = new HashMap<>();
    mockData.put("key", "value");

    // when
    logWebSocketNotification.onNotificationEvent(mockData);

    // then
    verify(logger).info("Received notification: {}", mockData);
    verify(logger).info(String.format("Data = %s", mockData));
  }

  @Test
  void shouldLogNotificationForSignatureNotificationWithError() {
    // given
    var signatureNotification = new SignatureNotification("Error occurred");

    // when
    logWebSocketNotification.onNotificationEvent(signatureNotification);

    // then
    verify(logger).info("Received notification: {}", signatureNotification);
    verify(logger).error("SignatureNotification error: {}", "Error occurred");
  }

  @Test
  void shouldLogNotificationForSignatureNotificationWithoutError() {
    // given
    var signatureNotification = new SignatureNotification(null);

    // when
    logWebSocketNotification.onNotificationEvent(signatureNotification);

    // then
    verify(logger).info("Received notification: {}", signatureNotification);
    verify(logger).info("SignatureNotification received without errors");
  }

  @Test
  void shouldLogUnexpectedDataType() {
    // given
    Object unexpectedData = new Object();

    // when
    logWebSocketNotification.onNotificationEvent(unexpectedData);

    // then
    verify(logger).info("Received notification: {}", unexpectedData);
    verify(logger).error("Unexpected data type received: {}", unexpectedData.getClass().getName());
  }
}
