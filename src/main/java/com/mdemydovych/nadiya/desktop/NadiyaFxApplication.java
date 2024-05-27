package com.mdemydovych.nadiya.desktop;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class NadiyaFxApplication extends Application {

  private ConfigurableApplicationContext applicationContext;


  @Override
  public void init() {
    this.applicationContext = new SpringApplicationBuilder(NadiyaDesktopApplication.class).run();
  }

  @Override
  public void start(Stage stage) {
    applicationContext.publishEvent(new StageReadyEvent(stage));
  }

  @Override
  public void stop() {
    applicationContext.close();
    Platform.exit();
  }

  static class StageReadyEvent extends ApplicationEvent {

    public StageReadyEvent(Stage stage) {
      super(stage);
    }

    public Stage getStage() {
      return (Stage) getSource();
    }
  }
}
