package com.mdemydovych.nadiya.desktop.controller;

import com.mdemydovych.nadiya.desktop.broker.BrokerClientServer;
import com.mdemydovych.nadiya.desktop.config.properties.BackEndProperties;
import com.mdemydovych.nadiya.desktop.model.BackEndHost;
import com.mdemydovych.nadiya.desktop.service.CommonOperation;
import com.mdemydovych.nadiya.desktop.service.QueryExecutorService;
import com.mdemydovych.nadiya.desktop.utils.ApplicationPageTitles;
import com.mdemydovych.nadiya.desktop.utils.StageUtils;
import com.mdemydovych.nadiya.desktop.utils.UserUtils;
import com.mdemydovych.nadiya.model.user.UserDto;
import javafx.concurrent.Worker.State;
import javafx.fxml.FXML;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoginPageController {

  private final CommonOperation commonOperation;

  private final QueryExecutorService executorService;

  private final BrokerClientServer brokerClientServer;

  private final BackEndProperties properties;

  private final BackEndHost host;

  @Value("classpath:/MainPage.fxml")
  private Resource mainPageResource;

  @FXML
  private WebView webView;

  @FXML
  private void initialize() {
    webView.getEngine().setJavaScriptEnabled(true);
    webView.getEngine().load(host.getHostUrl());
    webView.getEngine().getLoadWorker().stateProperty()
        .addListener(((observableValue, oldValue, newValue) -> {
          if (newValue == State.SUCCEEDED) {
            String currentUrl = webView.getEngine().getLocation();
            if (currentUrl.startsWith(host.getHostUrl())) {
              initCurrentUser();
              brokerClientServer.startClient();
              openMainPage();
            }
          }
        }));
  }

  private void openMainPage() {
    commonOperation.openNewWindow(mainPageResource, ApplicationPageTitles.MAIN_TITLE,
        ((parent, stage) -> {
          StageUtils.calculateAndSetPageSize(stage);
        }), Modality.NONE);
    Stage loginStage = (Stage) webView.getScene().getWindow();
    loginStage.close();
  }

  private void initCurrentUser() {
    UserDto userInfo = executorService.request(properties.getMyInfoPath(),
        HttpMethod.GET, null, UserDto.class);
    UserUtils.initUser(userInfo);
  }
}
