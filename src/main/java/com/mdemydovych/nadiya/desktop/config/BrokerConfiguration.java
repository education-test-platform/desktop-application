package com.mdemydovych.nadiya.desktop.config;

import com.mdemydovych.nadiya.desktop.broker.listener.SubscriptionBrokerListener;
import com.mdemydovych.nadiya.desktop.broker.resolver.BrokerMessageResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BrokerConfiguration {

  @Bean
  public SubscriptionBrokerListener subscriptionBrokerListener(BrokerMessageResolver resolver) {
    return new SubscriptionBrokerListener(resolver);
  }
}
