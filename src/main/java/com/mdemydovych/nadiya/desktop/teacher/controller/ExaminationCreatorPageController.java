package com.mdemydovych.nadiya.desktop.teacher.controller;

import com.mdemydovych.nadiya.desktop.service.CommonOperation;
import com.mdemydovych.nadiya.desktop.teacher.cell.FilePathCell;
import com.mdemydovych.nadiya.desktop.teacher.model.QuestionBuilder;
import com.mdemydovych.nadiya.desktop.teacher.service.TeacherService;
import com.mdemydovych.nadiya.desktop.utils.ApplicationPageTitles;
import com.mdemydovych.nadiya.model.examination.core.ExaminationDto;
import com.mdemydovych.nadiya.model.examination.core.PictureDto;
import com.mdemydovych.nadiya.model.examination.core.QuestionDto;
import io.micrometer.common.util.StringUtils;
import java.io.File;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Modality;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

@Component
@RequiredArgsConstructor
public class ExaminationCreatorPageController {

  private final TeacherService teacherService;

  private final CommonOperation commonOperation;

  private final VariantsCardController cardController;

  @Value("classpath:/teacher/templates/VariantsCard.fxml")
  private Resource cardControllerResource;

  @FXML
  private TableView<QuestionBuilder> questionsTable;

  @FXML
  private TableColumn<QuestionBuilder, String> questionDescriptionColumn;

  @FXML
  private TableColumn<QuestionBuilder, String> questionPictureColumn;

  @FXML
  private TextField examNameField;

  @FXML
  private Button addQuestionButton;

  @FXML
  private Button saveExamButton;


  @FXML
  private void initialize() {
    ObservableList<QuestionBuilder> questions = FXCollections.observableArrayList();
    questionPictureColumn.setCellValueFactory(new PropertyValueFactory<>("imagePath"));
    questionPictureColumn.setCellFactory(col -> new FilePathCell());
    questionDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
    questionDescriptionColumn.setOnEditCommit(
        evt -> evt.getRowValue().setDescription(evt.getNewValue()));
    questionDescriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    addQuestionButton.setOnAction(addQuestionButtonEvent(questions));
    commonOperation.addDoubleClickEvent(questionsTable, doubleClickEvent());
    questionsTable.setItems(questions);
    saveExamButton.setOnAction(saveExamButtonEvent());
  }

  private EventHandler<ActionEvent> addQuestionButtonEvent(
      ObservableList<QuestionBuilder> questions) {
    return actionEvent -> questions.add(new QuestionBuilder());
  }

  private Consumer<QuestionBuilder> doubleClickEvent() {
    return questionBuilder -> {
      cardController.setVariantDtos(questionBuilder.getVariants());
      commonOperation.openNewWindow(cardControllerResource,
          ApplicationPageTitles.VARIANTS_CARD_TITLE,
          (parent, stage) -> stage.setOnHiding(
              windowEvent -> questionBuilder.setVariants(cardController.getVariantDtos())),
          Modality.APPLICATION_MODAL);
    };
  }

  private EventHandler<ActionEvent> saveExamButtonEvent() {
    return actionEvent -> {
      ExaminationDto examination = buildExamination();
      teacherService.meSaveExamination(examination);
      commonOperation.showInformation("Успіх!", "Тест збережено!", "Ваш тест успішно збережено");
    };
  }

  private ExaminationDto buildExamination() {
    ExaminationDto examination = new ExaminationDto();
    examination.setTitle(examNameField.getText());
    examination.setQuestions(buildQuestions());
    return examination;
  }

  private Set<QuestionDto> buildQuestions() {
    return questionsTable.getItems().stream()
        .map(this::map)
        .collect(Collectors.toSet());
  }

  @SneakyThrows
  private QuestionDto map(QuestionBuilder builder) {
    QuestionDto question = new QuestionDto();
    question.setVariants(builder.getVariants());
    question.setDescription(builder.getDescription());
    if (StringUtils.isNotBlank(builder.getImagePath())) {
      PictureDto picture = new PictureDto();
      picture.setPicture(FileCopyUtils.copyToByteArray(new File(builder.getImagePath())));
      question.setPicture(picture);
    }
    return question;
  }
}
