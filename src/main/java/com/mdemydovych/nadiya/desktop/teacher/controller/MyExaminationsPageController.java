package com.mdemydovych.nadiya.desktop.teacher.controller;

import com.mdemydovych.nadiya.desktop.service.CommonOperation;
import com.mdemydovych.nadiya.desktop.teacher.service.TeacherService;
import com.mdemydovych.nadiya.desktop.utils.ApplicationPageTitles;
import com.mdemydovych.nadiya.model.examination.core.ExaminationPreview;
import java.util.List;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MyExaminationsPageController {

  private final TeacherService teacherService;

  private final CommonOperation commonOperation;

  private final StudentsExaminationResultsPageController resultsPageController;

  @Value("classpath:/teacher/templates/StudentsExaminationResultsPage.fxml")
  private Resource studentsExaminationResultResource;

  @FXML
  private TableView<ExaminationPreview> examResultsTable;

  @FXML
  private TableColumn<ExaminationPreview, Integer> idColumn;

  @FXML
  private TableColumn<ExaminationPreview, String> nameColumn;

  @FXML
  private TextField searchField;

  @FXML
  private void initialize() {
    idColumn.setCellValueFactory(
        p -> new ReadOnlyObjectWrapper<>(examResultsTable.getItems().indexOf(p.getValue()) + 1));
    nameColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
    commonOperation.addDoubleClickEvent(examResultsTable,
        preview -> {
          resultsPageController.setExamId(preview.getId());
          commonOperation.openNewWindow(studentsExaminationResultResource,
              ApplicationPageTitles.EXAM_STUDENT_RESULTS_TITLE, (parent, stage) -> {
              }, Modality.APPLICATION_MODAL);
        });
    fillTable();
  }

  private void fillTable() {
    List<ExaminationPreview> examinations = teacherService.findMyExaminations();
    commonOperation.setFilteredAndFillTable(examResultsTable, searchField, examinations,
        (s, examination) -> examination.getTitle().toLowerCase().contains(s));
  }
}
