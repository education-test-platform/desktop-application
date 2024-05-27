package com.mdemydovych.nadiya.desktop.teacher.controller;

import com.mdemydovych.nadiya.model.examination.core.VariantDto;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.BooleanStringConverter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VariantsCardController {

  @FXML
  private TableView<VariantDto> variantsTable;

  @FXML
  private TableColumn<VariantDto, String> descriptionColumn;

  @FXML
  private TableColumn<VariantDto, Boolean> correctedColumn;

  @FXML
  private Button saveButton;

  @FXML
  private Button variantAddButton;

  @Setter
  @Getter
  private List<VariantDto> variantDtos;

  @FXML
  private void initialize() {
    ObservableList<VariantDto> variants = FXCollections.observableArrayList(variantDtos);
    descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
    descriptionColumn.setOnEditCommit(evt -> evt.getRowValue().setDescription(evt.getNewValue()));
    descriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    correctedColumn.setCellValueFactory(new PropertyValueFactory<>("corrected"));
    correctedColumn.setOnEditCommit(evt -> evt.getRowValue().setCorrected(evt.getNewValue()));
    correctedColumn.setCellFactory(TextFieldTableCell.forTableColumn(new BooleanStringConverter()));
    saveButton.setOnAction(saveButtonEvent());
    variantAddButton.setOnAction(variantAddButtonEvent(variants));
    variantsTable.setItems(variants);
  }

  private EventHandler<ActionEvent> saveButtonEvent() {
    return actionEvent -> {
      variantDtos = variantsTable.getItems();
      ((Stage) variantsTable.getScene().getWindow()).close();
    };
  }

  private EventHandler<ActionEvent> variantAddButtonEvent(ObservableList<VariantDto> variants) {
    return actionEvent -> variants.add(new VariantDto());
  }

}
