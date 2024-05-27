package com.mdemydovych.nadiya.desktop.broker.resolver;

import com.mdemydovych.nadiya.desktop.model.BrokerEvent;

public interface BrokerAware {

  void handleEvent();

  boolean support(BrokerEvent event);
}
