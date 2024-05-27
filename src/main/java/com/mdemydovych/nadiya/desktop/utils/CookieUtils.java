package com.mdemydovych.nadiya.desktop.utils;

import java.net.CookieHandler;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import lombok.SneakyThrows;

public class CookieUtils {

  public static final String COOKIE_HEADER = "Cookie";

  private CookieUtils() {

  }

  @SneakyThrows
  public static String extractCookies(String host) {
    String prepareHost = host.concat("/");
    Map<String, List<String>> headers = CookieHandler
        .getDefault().get(new URI(prepareHost), Map.of());
    for (Entry<String, List<String>> entry : headers.entrySet()) {
      if (entry.getKey().equals(COOKIE_HEADER)) {
        return entry.getValue().get(0);
      }
    }
    return "";
  }
}
