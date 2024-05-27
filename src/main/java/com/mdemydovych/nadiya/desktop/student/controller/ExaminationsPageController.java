package com.mdemydovych.nadiya.desktop.student.controller;

import com.mdemydovych.nadiya.desktop.broker.resolver.impl.BrokerAwareAbstract;
import com.mdemydovych.nadiya.desktop.model.BrokerEvent;
import com.mdemydovych.nadiya.desktop.service.CommonOperation;
import com.mdemydovych.nadiya.desktop.student.service.StudentService;
import com.mdemydovych.nadiya.desktop.utils.ApplicationPageTitles;
import com.mdemydovych.nadiya.model.examination.core.ExaminationPreview;
import java.util.List;
import java.util.Objects;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExaminationsPageController extends BrokerAwareAbstract {

  private final StudentService studentService;

  private final ExaminationRoomController examinationRoomController;

  private final CommonOperation commonOperation;

  @Value("classpath:/student/templates/ExamRoom.fxml")
  private Resource examRoomPage;

  @FXML
  private TableColumn<ExaminationPreview, Integer> idColumn;

  @FXML
  private TableColumn<ExaminationPreview, String> nameColumn;

  @FXML
  private TableView<ExaminationPreview> examinationsTable;

  @FXML
  private TableColumn<ExaminationPreview, String> teacherNameColumn;

  @FXML
  private void initialize() {
    idColumn.setCellValueFactory(
        p -> new ReadOnlyObjectWrapper<>(examinationsTable.getItems().indexOf(p.getValue()) + 1));
    nameColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
    teacherNameColumn.setCellValueFactory(new PropertyValueFactory<>("teacher"));
    teacherNameColumn.setCellValueFactory(
        cellData -> new SimpleStringProperty(cellData.getValue().getTeacher().getUsername()));
    fillTable();
    commonOperation.addDoubleClickEvent(examinationsTable, this::doubleClickEvent);
  }

  private void doubleClickEvent(ExaminationPreview preview) {
    CommonOperation.confirmationAlert("Почати екзамен", "Ви впевнні?",
        "Ви впевнені, що хочете почати екзамен?",
        Event::consume,
        event -> startExam(preview));
  }

  private void startExam(ExaminationPreview preview) {
    examinationRoomController.setExamId(preview.getId());
    commonOperation.openNewWindow(examRoomPage, ApplicationPageTitles.EXAMINATION_ROOM_TITLE,
        (parent, stage) -> stage.setOnHiding(windowEvent -> refreshTable()),
        Modality.APPLICATION_MODAL);
  }

  @Override
  public void handleEvent() {
    if (Objects.nonNull(examinationsTable) && isActiveWindow(examinationsTable.getScene())) {
      refreshTable();
    }
  }

  public void refreshTable() {
    fillTable();
    examinationsTable.refresh();
  }

  private void fillTable() {
    List<ExaminationPreview> previews = studentService.myActiveExams();
    examinationsTable.setItems(FXCollections.observableList(previews));
  }

  @Override
  public boolean support(BrokerEvent event) {
    return event == BrokerEvent.PUBLISH_EXAM;
  }
}
