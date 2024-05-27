package com.mdemydovych.nadiya.desktop.teacher.controller;

import com.mdemydovych.nadiya.desktop.service.CommonOperation;
import com.mdemydovych.nadiya.desktop.service.ExaminationResultService;
import com.mdemydovych.nadiya.desktop.utils.ApplicationPageTitles;
import com.mdemydovych.nadiya.model.examination.result.ExaminationResultDto;
import java.util.List;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StudentsExaminationResultsPageController {

  private final ExaminationResultService examinationResultService;

  private final StudentExaminationDetailsResultPageController detailsResultPageController;

  private final CommonOperation commonOperation;

  @Value("classpath:/teacher/templates/StudentExaminationDetailsResultPage.fxml")
  private Resource studentExamDetailsPageResource;

  @FXML
  private TableView<ExaminationResultDto> studentExamResultsTable;

  @FXML
  private TextField searchField;

  @FXML
  private TableColumn<ExaminationResultDto, Integer> idColumn;

  @FXML
  private TableColumn<ExaminationResultDto, String> scoreColumn;

  @FXML
  private TableColumn<ExaminationResultDto, String> studentNameColumn;

  @Setter
  private String examId;

  @FXML
  private void initialize() {
    idColumn.setCellValueFactory(
        p -> new ReadOnlyObjectWrapper<>(
            studentExamResultsTable.getItems().indexOf(p.getValue()) + 1));
    scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
    studentNameColumn.setCellValueFactory(
        cellData -> new SimpleStringProperty(cellData.getValue().getStudent().getUsername()));
    commonOperation.addDoubleClickEvent(studentExamResultsTable,
        resultDto -> {
          detailsResultPageController.setExaminationResult(resultDto);
          commonOperation.openNewWindow(studentExamDetailsPageResource,
              ApplicationPageTitles.PERSONAL_STUDENT_EXAM_RESULT_TITLE,
              (parent, stage) -> {
              }, Modality.APPLICATION_MODAL);
        });
    fillTable();
  }

  private void fillTable() {
    List<ExaminationResultDto> examinationResults =
        examinationResultService.findAllExamResults(examId);
    commonOperation.setFilteredAndFillTable(
        studentExamResultsTable, searchField, examinationResults,
        (s, examination) -> examination.getStudent().getUsername().toLowerCase().contains(s)
            || examination.getStudent().getEmail().toLowerCase().contains(s));
  }
}
