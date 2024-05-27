package com.mdemydovych.nadiya.desktop.service;

import com.mdemydovych.nadiya.desktop.config.properties.BackEndProperties;
import com.mdemydovych.nadiya.model.user.UserDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final QueryExecutorService executorService;

  private final BackEndProperties properties;

  public List<UserDto> getTeachers() {
    return List.of(executorService.
        request(properties.getGetTeachersPath(), HttpMethod.GET, null, UserDto[].class));
  }
}
