package com.mdemydovych.nadiya.desktop.service;

import com.mdemydovych.nadiya.desktop.model.BackEndHost;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class QueryExecutorService {

  private final RestTemplate restTemplate;

  private final BackEndHost backEndHost;

  public <T> T request(String url, HttpMethod method, Object body,
      Class<T> responseType, Object... params) {
    try {
      return restTemplate
          .exchange(backEndHost.getHostUrl().concat(url),
              method, new HttpEntity<>(body), responseType, params).getBody();
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }
}
