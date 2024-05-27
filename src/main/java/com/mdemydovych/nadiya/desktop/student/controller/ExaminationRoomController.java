package com.mdemydovych.nadiya.desktop.student.controller;

import com.mdemydovych.nadiya.desktop.service.CommonOperation;
import com.mdemydovych.nadiya.desktop.service.ExaminationService;
import com.mdemydovych.nadiya.desktop.student.service.StudentService;
import com.mdemydovych.nadiya.model.examination.core.ExaminationAo;
import com.mdemydovych.nadiya.model.examination.core.ExaminationDto;
import com.mdemydovych.nadiya.model.examination.core.QuestionAo;
import com.mdemydovych.nadiya.model.examination.result.AnswerDto;
import com.mdemydovych.nadiya.model.examination.result.ExaminationResultDto;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExaminationRoomController {

  private final StudentService studentService;

  private final ExaminationService examinationService;

  private final QuestionCardController cardController;

  private final CommonOperation commonOperation;

  @Value("classpath:/student/templates/QuestionCard.fxml")
  private Resource questionCardResource;

  @FXML
  private Button nextButton;

  @FXML
  private TextField examName;

  @FXML
  private AnchorPane questionPane;

  @Setter
  private String examId;

  @FXML
  private void initialize() {
    ExaminationResultDto result = buildExaminationResult();
    ExaminationAo examination = examinationService.findById(examId);
    List<QuestionAo> questions = examination.getQuestions();
    examName.setText(examination.getTitle());
    cardController.clear();
    nextButton.setOnAction(nextButtonEvent(questions, result));
  }

  private EventHandler<ActionEvent> nextButtonEvent(
      List<QuestionAo> questions, ExaminationResultDto result) {
    Iterator<QuestionAo> questionIterator = questions.iterator();
    return actionEvent -> {
      cardController.getQuestionChoose().ifPresent(
          answerDto -> putAnswerToResult(result, answerDto));
      if (questionIterator.hasNext()) {
        cardController.setQuestion(questionIterator.next());
        commonOperation.openInPane(questionCardResource, questionPane);
        return;
      }
      try {
        studentService.saveExamResult(result);
        commonOperation.showInformation("Успіх", "Відповіді збережено",
            "Відповіді на екзамен успішно збережено");
      } catch (Exception e) {
        commonOperation.showError("Помилка", "",
            "При збереженні відповідей відбулась помилка");
      } finally {
        ((Stage) questionPane.getScene().getWindow()).close();
      }
    };
  }

  private void putAnswerToResult(ExaminationResultDto result, AnswerDto answer) {
    for (AnswerDto savedAnswer : result.getAnswers()) {
      if (savedAnswer.getQuestionId().equals(answer.getQuestionId())) {
        return;
      }
    }
    result.getAnswers().add(answer);
  }

  private ExaminationResultDto buildExaminationResult() {
    ExaminationResultDto result = new ExaminationResultDto();
    result.setAnswers(new ArrayList<>());
    ExaminationDto examination = new ExaminationDto();
    examination.setId(examId);
    result.setExamination(examination);
    return result;
  }
}
