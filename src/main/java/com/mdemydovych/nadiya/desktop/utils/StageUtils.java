package com.mdemydovych.nadiya.desktop.utils;

import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.AllArgsConstructor;
import lombok.Data;

public class StageUtils {

  private StageUtils() {

  }

  public static void calculateAndSetPageSize(Stage stage) {
    Size size = calculateSize();
    stage.setMinHeight(size.getHeight());
    stage.setMinWidth(size.getWidth());
  }

  private static Size calculateSize() {
    double screenWidth = Screen.getPrimary().getVisualBounds().getWidth() / 1.5;
    double screenHeight = Screen.getPrimary().getVisualBounds().getHeight() / 1.5;
    return new Size(screenWidth, screenHeight);
  }


  @Data
  @AllArgsConstructor
  private static class Size {

    private double width;

    private double height;

  }

}