package com.mdemydovych.nadiya.desktop.controller;

import com.mdemydovych.nadiya.desktop.service.CommonOperation;
import com.mdemydovych.nadiya.desktop.utils.UserUtils;
import com.mdemydovych.nadiya.model.user.UserRole;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MainPageController {

  private final CommonOperation commonOperation;

  @Value("classpath:/student/templates/ExaminationsPage.fxml")
  private Resource examinationsPageResource;

  @Value("classpath:/teacher/templates/ExaminationCreatorPage.fxml")
  private Resource examinationCreatorPageResource;

  @Value("classpath:/student/templates/TeachersPage.fxml")
  private Resource teachersPageResource;

  @Value("classpath:/student/templates/MyTeachersPage.fxml")
  private Resource myTeachersPageResource;

  @Value("classpath:/student/templates/MyExaminationResultsPage.fxml")
  private Resource myExamResultsPageResource;

  @Value("classpath:/teacher/templates/MyStudentsPage.fxml")
  private Resource myStudentsPageResource;

  @Value("classpath:/teacher/templates/MyExaminationsPage.fxml")
  private Resource myExaminationsPageResource;


  @FXML
  private MenuItem examinationsPage;

  @FXML
  private MenuItem examCreatorPage;

  @FXML
  private MenuItem teacherPage;

  @FXML
  private MenuItem myTeachersPage;

  @FXML
  private MenuItem myExamResultsPage;

  @FXML
  private MenuItem myStudentsPage;

  @FXML
  private MenuItem myExamsPage;


  @FXML
  private AnchorPane workStation;

  @FXML
  private void initialize() {
    registerMenuItem(myExamResultsPage, myExamResultsPageResource, UserRole.STUDENT);
    registerMenuItem(myTeachersPage, myTeachersPageResource, UserRole.STUDENT);
    registerMenuItem(teacherPage, teachersPageResource, UserRole.STUDENT);
    registerMenuItem(examinationsPage, examinationsPageResource, UserRole.STUDENT);
    registerMenuItem(examCreatorPage, examinationCreatorPageResource, UserRole.TEACHER);
    registerMenuItem(myStudentsPage, myStudentsPageResource, UserRole.TEACHER);
    registerMenuItem(myExamsPage, myExaminationsPageResource, UserRole.TEACHER);
  }

  private void registerMenuItem(MenuItem item, Resource resource, UserRole role) {
    if (UserUtils.getCurrentUser().getRole() == role) {
      item.setOnAction(openInPane(resource));
      return;
    }
    item.setVisible(false);
  }

  private EventHandler<ActionEvent> openInPane(Resource resource) {
    return actionEvent -> commonOperation.openInPane(resource, workStation);
  }
}

