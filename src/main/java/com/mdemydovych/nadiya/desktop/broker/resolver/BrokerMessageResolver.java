package com.mdemydovych.nadiya.desktop.broker.resolver;

import com.mdemydovych.nadiya.desktop.model.BrokerEvent;
import com.mdemydovych.nadiya.model.broker.BrokerMessage;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BrokerMessageResolver {

  private final List<BrokerAware> brokerAwares;

  public void handleMessage(BrokerMessage message) {
    BrokerEvent event = BrokerEvent.valueOf(message.getContent());
    for (BrokerAware brokerAware : brokerAwares) {
      if (brokerAware.support(event)) {
        brokerAware.handleEvent();
      }
    }
  }
}
