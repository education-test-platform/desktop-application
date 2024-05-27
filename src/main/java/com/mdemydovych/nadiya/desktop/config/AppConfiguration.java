package com.mdemydovych.nadiya.desktop.config;

import com.mdemydovych.nadiya.desktop.config.properties.BackEndProperties;
import com.mdemydovych.nadiya.desktop.model.BackEndHost;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableConfigurationProperties({BackEndProperties.class})
public class AppConfiguration {

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  @Bean
  public BackEndHost backEndHost(@Value("${app.backend.host}") String backHost,
      @Value("${app.broker.uri}") String brokerUri) {
    return new BackEndHost(backHost, brokerUri);
  }
}
