package com.mdemydovych.nadiya.desktop.service;

import io.micrometer.common.util.StringUtils;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommonOperation {

  private final ApplicationContext applicationContext;

  @Value("classpath:/uiForm/loadCard.fxml")
  private Resource loadCard;

  public void openNewWindow(Resource resource, String title,
      BiConsumer<Parent, Stage> contentAction, Modality modality) {
    openNewWindow(resource, title, contentAction, modality, new Stage());
  }

  public void openNewWindow(Resource resource, String title,
      BiConsumer<Parent, Stage> contentAction, Modality modality, Stage stage) {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(resource.getURL());
      fxmlLoader.setControllerFactory(applicationContext::getBean);
      Parent root = fxmlLoader.load();
      stage.initModality(modality);
      stage.setTitle(title);
      stage.setScene(new Scene(root));
      contentAction.accept(root, stage);
      stage.show();
    } catch (Exception e) {
      log.error("Error occurred while opening new page.", e);
    }
  }

  @SuppressWarnings("unchecked")
  public <T, K> void openInPane(Resource resource, AnchorPane anchorPane) {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(resource.getURL());
      fxmlLoader.setControllerFactory(applicationContext::getBean);
      Parent root = fxmlLoader.load();
      anchorPane.getChildren().clear();
      anchorPane.getChildren().add(root);
      TableView<T> table = (TableView<T>) root.lookup("TableView");
      if (Objects.nonNull(table)) {
        table.getSelectionModel().setCellSelectionEnabled(true);
        Scene scene = Objects.nonNull(table.getScene()) ? table.getScene() : anchorPane.getScene();
        scene.getAccelerators()
            .put(new KeyCodeCombination(KeyCode.C, KeyCombination.SHORTCUT_DOWN), () -> {
              TablePosition<T, K> pos = table.getSelectionModel().getSelectedCells().iterator()
                  .next();
              int row = pos.getRow();
              T item = table.getItems().get(row);
              TableColumn<T, K> col = pos.getTableColumn();
              K data = col.getCellObservableValue(item).getValue();
              final Clipboard clipboard = Clipboard.getSystemClipboard();
              final ClipboardContent content = new ClipboardContent();
              content.putString(data.toString());
              clipboard.setContent(content);
            });
      }
    } catch (Exception e) {
      log.error("Error occurred while opening new page.", e);
      showError("Error", "Error while open pane", e.getLocalizedMessage());
    }
  }

  public void showInformation(String title, String header, String context) {
    showAlert(title, header, context, AlertType.INFORMATION);
  }

  public void showError(String title, String header, String context) {
    showAlert(title, header, context, AlertType.ERROR);
  }

  private void showAlert(String title, String header, String context, AlertType alertType) {
    Alert alert = new Alert(alertType);
    alert.setTitle(title);
    alert.setHeaderText(header);
    alert.setContentText(context);
    alert.showAndWait();
  }

  public static void confirmationAlert(String title,
      String headerText,
      String contentText,
      EventHandler<ActionEvent> negativeAction,
      EventHandler<ActionEvent> positiveAction) {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle(title);
    alert.setHeaderText(headerText);
    alert.setContentText(contentText);
    Button yesButton = ((Button) alert.getDialogPane().lookupButton(ButtonType.OK));
    yesButton.setText("Так");
    yesButton.setDefaultButton(false);
    yesButton.setOnAction(positiveAction);

    Button noButton = ((Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL));
    noButton.setText("Ні");
    noButton.setDefaultButton(true);
    noButton.setOnAction(negativeAction);
    alert.showAndWait();
  }

  public <T> void addDoubleClickEvent(TableView<T> table, Consumer<T> eventHandler) {
    addDoubleClickEvent(null, table, eventHandler);
  }

  public <T> void addDoubleClickEvent(Stage stage, TableView<T> table, Consumer<T> eventHandler) {
    table.setOnMouseClicked(mouseEvent -> {
      if (mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2) {
        T item = table.getSelectionModel().getSelectedItem();
        if (Objects.nonNull(item)) {
          eventHandler.accept(item);
          if (Objects.nonNull(stage)) {
            stage.close();
          }
        }
      }
    });
  }

  public <T> void setFilteredAndFillTable(TableView<T> table,
      TextField filterField, List<T> items, BiPredicate<String, T> predicate) {
    FilteredList<T> filteredList = new FilteredList<>(
        FXCollections.observableList(items), p -> true);
    filterField.textProperty()
        .addListener(((observableValue, oldValue, newValue) -> {
          filteredList.setPredicate(userDto -> {
            if (StringUtils.isBlank(newValue)) {
              return true;
            }
            String lowerCaseFilter = newValue.toLowerCase();
            return predicate.test(lowerCaseFilter, userDto);
          });
        }));
    table.setItems(filteredList);
  }
}

