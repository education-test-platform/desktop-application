package com.mdemydovych.nadiya.desktop.broker;

import com.mdemydovych.nadiya.desktop.broker.listener.BrokerListener;
import com.mdemydovych.nadiya.desktop.broker.listener.SubscriptionBrokerListener;
import com.mdemydovych.nadiya.desktop.model.BackEndHost;
import com.mdemydovych.nadiya.desktop.utils.CookieUtils;
import com.mdemydovych.nadiya.desktop.utils.UserUtils;
import io.github.centrifugal.centrifuge.Client;
import io.github.centrifugal.centrifuge.Options;
import io.github.centrifugal.centrifuge.Subscription;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BrokerClientServer {

  private final BackEndHost backEndHost;

  private final SubscriptionBrokerListener subscriptionBrokerListener;

  @SneakyThrows
  public void startClient() {
    Client client = initClient();
    Subscription subscription = client
        .newSubscription(UserUtils.getCurrentUser().getId(), subscriptionBrokerListener);
    client.connect();
    subscription.subscribe();
  }

  @SneakyThrows
  private Client initClient() {
    Options opts = new Options();
    opts.setHeaders(
        Map.of(CookieUtils.COOKIE_HEADER, CookieUtils.extractCookies(backEndHost.getHostUrl())));
    return new Client(backEndHost.getBrokerHost(), opts, new BrokerListener());
  }
}
