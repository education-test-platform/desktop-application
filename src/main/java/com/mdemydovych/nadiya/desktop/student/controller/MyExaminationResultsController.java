package com.mdemydovych.nadiya.desktop.student.controller;

import com.mdemydovych.nadiya.desktop.service.CommonOperation;
import com.mdemydovych.nadiya.desktop.service.ExaminationResultService;
import com.mdemydovych.nadiya.desktop.student.service.StudentService;
import com.mdemydovych.nadiya.model.examination.result.ExaminationResultDto;
import java.util.List;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MyExaminationResultsController {

  private final StudentService studentService;

  private final CommonOperation commonOperation;

  @FXML
  private TextField searchField;

  @FXML
  private TableColumn<ExaminationResultDto, String> nameColumn;

  @FXML
  private TableColumn<ExaminationResultDto, String> scoreColumn;

  @FXML
  private TableColumn<ExaminationResultDto, String> teacherColumn;

  @FXML
  private TableView<ExaminationResultDto> examResultsTable;

  @FXML
  private TableColumn<ExaminationResultDto, Integer> idColumn;

  @FXML
  private void initialize() {
    idColumn.setCellValueFactory(
        p -> new ReadOnlyObjectWrapper<>(examResultsTable.getItems().indexOf(p.getValue()) + 1));
    nameColumn.setCellValueFactory(
        cellData -> new SimpleStringProperty(cellData.getValue().getExamination().getTitle()));
    scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
    teacherColumn.setCellValueFactory(
        cellData -> new SimpleStringProperty(
            cellData.getValue().getExamination().getTeacher().getUsername()));
    fillTable();
  }

  private void fillTable() {
    List<ExaminationResultDto> results = studentService.findMyExamResults();
    commonOperation.setFilteredAndFillTable(examResultsTable, searchField, results,
        (s, exam) -> exam.getExamination().getTitle().toLowerCase().contains(s)
            || exam.getExamination().getTeacher().getUsername().toLowerCase().contains(s));
  }
}
