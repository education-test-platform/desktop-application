package com.mdemydovych.nadiya.desktop.teacher.controller;

import com.mdemydovych.nadiya.model.examination.core.QuestionDto;
import com.mdemydovych.nadiya.model.examination.result.AnswerDto;
import com.mdemydovych.nadiya.model.examination.result.ExaminationResultDto;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StudentExaminationDetailsResultPageController {

  @FXML
  private TableView<AnswerDto> examStudentResultDetailTable;

  @FXML
  private TableColumn<AnswerDto, String> answerColumn;

  @FXML
  private TableColumn<AnswerDto, String> questionColumn;

  @Setter
  private ExaminationResultDto examinationResult;

  @FXML
  private void initialize() {
    answerColumn.setCellValueFactory(new PropertyValueFactory<>("answer"));
    questionColumn.setCellValueFactory(
        cellData -> new SimpleStringProperty(
            findQuestion(cellData.getValue().getQuestionId()).getDescription()));
    fillTable();
  }

  private QuestionDto findQuestion(String questionId) {
    return examinationResult.getExamination().getQuestions()
        .stream()
        .filter(question -> question.getId().equals(questionId))
        .findFirst()
        .orElse(new QuestionDto());
  }

  private void fillTable() {
    ObservableList<AnswerDto> answers =
        FXCollections.observableList(examinationResult.getAnswers());
    examStudentResultDetailTable.setItems(answers);
  }
}
