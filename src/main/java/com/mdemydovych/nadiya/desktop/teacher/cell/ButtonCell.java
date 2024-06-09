package com.mdemydovych.nadiya.desktop.teacher.cell;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;

public class ButtonCell<T> extends TableCell<T, String> {

  private final Button button;

  private final BiConsumer<Button, T> buttonCustom;

  public ButtonCell(Consumer<T> handler, BiConsumer<Button, T> buttonCustom) {
    this.button = new Button();
    this.buttonCustom = buttonCustom;
    button.setOnAction(actionEvent -> {
      T entity = getTableView().getItems().get(getIndex());
      handler.accept(entity);
    });
  }

  @Override
  protected void updateItem(String item, boolean empty) {
    super.updateItem(item, empty);
    if (empty) {
      setGraphic(null);
    } else {
      T entity = getTableView().getItems().get(getIndex());
      buttonCustom.accept(button, entity);
      setGraphic(button);
    }
  }
}
