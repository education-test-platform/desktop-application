import static java.nio.charset.StandardCharsets.UTF_8;

import io.github.centrifugal.centrifuge.Client;
import io.github.centrifugal.centrifuge.ConnectedEvent;
import io.github.centrifugal.centrifuge.ConnectingEvent;
import io.github.centrifugal.centrifuge.DisconnectedEvent;
import io.github.centrifugal.centrifuge.ErrorEvent;
import io.github.centrifugal.centrifuge.EventListener;
import io.github.centrifugal.centrifuge.JoinEvent;
import io.github.centrifugal.centrifuge.LeaveEvent;
import io.github.centrifugal.centrifuge.MessageEvent;
import io.github.centrifugal.centrifuge.Options;
import io.github.centrifugal.centrifuge.PublicationEvent;
import io.github.centrifugal.centrifuge.ServerJoinEvent;
import io.github.centrifugal.centrifuge.ServerLeaveEvent;
import io.github.centrifugal.centrifuge.ServerPublicationEvent;
import io.github.centrifugal.centrifuge.ServerSubscribedEvent;
import io.github.centrifugal.centrifuge.ServerSubscribingEvent;
import io.github.centrifugal.centrifuge.ServerUnsubscribedEvent;
import io.github.centrifugal.centrifuge.SubscribedEvent;
import io.github.centrifugal.centrifuge.SubscribingEvent;
import io.github.centrifugal.centrifuge.Subscription;
import io.github.centrifugal.centrifuge.SubscriptionErrorEvent;
import io.github.centrifugal.centrifuge.SubscriptionEventListener;
import io.github.centrifugal.centrifuge.UnsubscribedEvent;
import java.util.Map;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

//@SpringBootTest
public class TestClass {

//  @Autowired
//  private BrokerPublisherService publisherService;

  @Test
  @SneakyThrows
  void shouldTest() {
    EventListener listener = new EventListener() {
      @Override
      public void onConnected(Client client, ConnectedEvent event) {
        System.out.printf("connected with client id %s%n", event.getClient());
      }

      @Override
      public void onConnecting(Client client, ConnectingEvent event) {
        System.out.printf("connecting: %s%n", event.getReason());
      }

      @Override
      public void onDisconnected(Client client, DisconnectedEvent event) {
        System.out.printf("disconnected %d %s%n", event.getCode(), event.getReason());
      }

      @Override
      public void onError(Client client, ErrorEvent event) {
        System.out.printf("connection error: %s%n", event.getError().toString());
      }

      @Override
      public void onMessage(Client client, MessageEvent event) {
        String data = new String(event.getData(), UTF_8);
        System.out.println("message received: " + data);
      }

      @Override
      public void onSubscribed(Client client, ServerSubscribedEvent event) {
        System.out.println("server side subscribed: " + event.getChannel() + ", recovered "
            + event.getRecovered());
      }

      @Override
      public void onSubscribing(Client client, ServerSubscribingEvent event) {
        System.out.println("server side subscribing: " + event.getChannel());
      }

      @Override
      public void onUnsubscribed(Client client, ServerUnsubscribedEvent event) {
        System.out.println("server side unsubscribed: " + event.getChannel());
      }

      @Override
      public void onPublication(Client client, ServerPublicationEvent event) {
        String data = new String(event.getData(), UTF_8);
        System.out.println("server side publication: " + event.getChannel() + ": " + data);
      }

      @Override
      public void onJoin(Client client, ServerJoinEvent event) {
        System.out.println(
            "server side join: " + event.getChannel() + " from client " + event.getInfo()
                .getClient());
      }

      @Override
      public void onLeave(Client client, ServerLeaveEvent event) {
        System.out.println(
            "server side leave: " + event.getChannel() + " from client " + event.getInfo()
                .getClient());
      }
    };

    Options opts = new Options();
    opts.setHeaders(Map.of("Cookie", "JSESSIONID=3D1A595CF388E2A3DCF369756ABC3AB1; SESSION=f0e489bb-0026-4d33-a32b-f855b10bec2a"));
//    opts.setToken("troken");
    Client client = new Client(
        "ws://localhost:8081/broker/connection/websocket",
        opts,
        listener
    );
    Subscription subscription = client.newSubscription("396d5ccc-a331-4ada-b04b-076ef17297c5",
        new SubscriptionEventListener() {
          @Override
          public void onPublication(Subscription sub, PublicationEvent event) {
            System.out.println("on pub " + new String(event.getData(), UTF_8));
          }

          @Override
          public void onJoin(Subscription sub, JoinEvent event) {
            System.out.println("on join");
          }

          @Override
          public void onLeave(Subscription sub, LeaveEvent event) {
            System.out.println("on leave");
          }

          @Override
          public void onSubscribed(Subscription sub, SubscribedEvent event) {
            System.out.println("on subs");
          }

          @Override
          public void onUnsubscribed(Subscription sub, UnsubscribedEvent event) {
            System.out.println("on unsubs");
          }

          @Override
          public void onSubscribing(Subscription sub, SubscribingEvent event) {
            System.out.println("on onSubscribing");
          }

          @Override
          public void onError(Subscription sub, SubscriptionErrorEvent event) {
            System.out.println("on error");
          }
        });
    client.connect();
    subscription.subscribe();
    Thread.sleep(10000000);
  }

  @Test
  void testtest() {
    String message = "message";
    System.out.println();
  }
}
