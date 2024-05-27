package com.mdemydovych.nadiya.desktop.teacher.cell;

import com.mdemydovych.nadiya.desktop.teacher.model.QuestionBuilder;
import java.io.File;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.stage.FileChooser;

public class FilePathCell extends TableCell<QuestionBuilder, String> {

  private final Button chooseFileButton = new Button("Обрати файл");

  public FilePathCell() {
    chooseFileButton.setOnAction(event -> {
      FileChooser fileChooser = new FileChooser();
      File selectedFile = fileChooser.showOpenDialog(getScene().getWindow());
      if (selectedFile != null) {
        String filePath = selectedFile.getAbsolutePath();
        commitEdit(filePath);
        getTableView().getItems().get(getIndex()).setImagePath(filePath);
      }
    });
  }

  @Override
  protected void updateItem(String item, boolean empty) {
    super.updateItem(item, empty);
    if (empty) {
      setGraphic(null);
    } else {
      setGraphic(chooseFileButton);
    }
  }
}