package com.mdemydovych.nadiya.desktop.student.controller;

import com.mdemydovych.nadiya.model.examination.core.QuestionAo;
import com.mdemydovych.nadiya.model.examination.core.VariantAo;
import com.mdemydovych.nadiya.model.examination.result.AnswerDto;
import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
public class QuestionCardController {

  @FXML
  private ImageView image;

  @FXML
  private GridPane variantsPane;

  @FXML
  private Label questionLabel;

  @Setter
  private QuestionAo question;

  @FXML
  private void initialize() {
    questionLabel.setText(question.getDescription());
    initImage(question);
    initVariants(question);
  }

  public void clear() {
    if (Objects.nonNull(variantsPane)) {
      variantsPane.getChildren().clear();
    }
  }

  public Optional<AnswerDto> getQuestionChoose() {
    if (Objects.isNull(variantsPane)
        || variantsPane.getChildren().isEmpty()) {
      return Optional.empty();
    }
    return variantsPane.getChildren().stream()
        .filter(node -> node instanceof RadioButton)
        .map(node -> (RadioButton) node)
        .filter(RadioButton::isSelected)
        .findFirst()
        .map(RadioButton::getText)
        .map(this::buildAnswer);
  }

  private AnswerDto buildAnswer(String answer) {
    AnswerDto result = new AnswerDto();
    result.setQuestionId(question.getId());
    result.setAnswer(answer);
    return result;
  }

  private void initImage(QuestionAo questionAo) {
    if (Objects.nonNull(questionAo.getPicture())) {
      ByteArrayInputStream stream = new ByteArrayInputStream(questionAo.getPicture().getPicture());
      image.setImage(new Image(stream));
    }
  }

  private void initVariants(QuestionAo questionAo) {
    List<VariantAo> variants = questionAo.getVariants();
    ToggleGroup group = new ToggleGroup();
    for (int i = 0; i < variants.size(); i++) {
      VariantAo variant = variants.get(i);
      RadioButton button = new RadioButton(variant.getDescription());
      button.setToggleGroup(group);
      variantsPane.add(button, calculateCellPosition(i, variantsPane.getColumnCount()),
          calculateGroup(i, variantsPane.getColumnCount() + 1));
    }
  }

  private int calculateCellPosition(int cyclePosition, int maxSize) {
    if (cyclePosition <= maxSize) {
      return cyclePosition;
    }
    int preparedMaxSize = maxSize + 1;
    int group = calculateGroup(cyclePosition, preparedMaxSize);
    return (preparedMaxSize * group) - cyclePosition;
  }

  private int calculateGroup(int cyclePosition, int maxSize) {
    return (cyclePosition / maxSize) + 1;
  }
}
