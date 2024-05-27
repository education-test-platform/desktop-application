package com.mdemydovych.nadiya.desktop.broker.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mdemydovych.nadiya.desktop.broker.resolver.BrokerMessageResolver;
import com.mdemydovych.nadiya.model.broker.BrokerMessage;
import io.github.centrifugal.centrifuge.PublicationEvent;
import io.github.centrifugal.centrifuge.Subscription;
import io.github.centrifugal.centrifuge.SubscriptionEventListener;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
public class SubscriptionBrokerListener extends SubscriptionEventListener {

  private final BrokerMessageResolver brokerMessageResolver;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  @SneakyThrows
  public void onPublication(Subscription sub, PublicationEvent event) {
    brokerMessageResolver.handleMessage(
        objectMapper.readValue(event.getData(), BrokerMessage.class));
  }
}
