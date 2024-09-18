package com.blockchain.api.domain.service.transfer;

import java.util.AbstractMap;
import org.p2p.solanaj.ws.SignatureNotification;
import org.p2p.solanaj.ws.listeners.NotificationEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LogWebSocketNotification implements NotificationEventListener {

  Logger log = LoggerFactory.getLogger(LogWebSocketNotification.class);

  @Override
  public void onNotificationEvent(Object data) {
    log.info("Received notification: {}", data);

    if (data instanceof AbstractMap) {
      AbstractMap<String, String> map = (AbstractMap<String, String>) data;
      log.info(String.format("Data = %s", map));
    } else if (data instanceof SignatureNotification notification) {
      if (notification.hasError()) {
        log.error("SignatureNotification error: {}", notification.getError());
      } else {
        log.info("SignatureNotification received without errors");
      }
    } else {
      log.error("Unexpected data type received: {}", data.getClass().getName());
    }
  }
}
