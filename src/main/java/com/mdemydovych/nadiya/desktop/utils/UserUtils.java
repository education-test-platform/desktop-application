package com.mdemydovych.nadiya.desktop.utils;


import com.mdemydovych.nadiya.model.user.UserDto;

public class UserUtils {

  private static UserDto CURRENT_USER;

  private UserUtils() {

  }

  public static UserDto getCurrentUser() {
    return CURRENT_USER;
  }

  public static void initUser(UserDto userDto) {
    CURRENT_USER = userDto;
  }
}
