package com.mdemydovych.nadiya.desktop;

import com.mdemydovych.nadiya.desktop.NadiyaFxApplication.StageReadyEvent;
import com.mdemydovych.nadiya.desktop.utils.ApplicationPageTitles;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StageInitializer implements ApplicationListener<NadiyaFxApplication.StageReadyEvent> {

  private static final Logger LOGGER = LoggerFactory.getLogger(StageInitializer.class);

  private final ApplicationContext applicationContext;

  @Value("classpath:/LoginPage.fxml")
  private Resource loginResource;


  @Override
  public void onApplicationEvent(StageReadyEvent event) {
    openLoginPage(event);
  }

  private void openLoginPage(StageReadyEvent event) {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(loginResource.getURL());
      fxmlLoader.setControllerFactory(applicationContext::getBean);
      Parent parent = fxmlLoader.load();
      Stage stage = event.getStage();
      stage.setScene(new Scene(parent));
      stage.setTitle(ApplicationPageTitles.LOGIN_TITLE);
      stage.show();
    } catch (IOException e) {
      LOGGER.error("Error {}", e.getMessage());
      throw new RuntimeException(e);
    }
  }
}
