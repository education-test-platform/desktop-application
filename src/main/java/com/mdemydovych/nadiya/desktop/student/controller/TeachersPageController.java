package com.mdemydovych.nadiya.desktop.student.controller;

import com.mdemydovych.nadiya.desktop.service.CommonOperation;
import com.mdemydovych.nadiya.desktop.service.UserService;
import com.mdemydovych.nadiya.desktop.student.service.StudentService;
import com.mdemydovych.nadiya.model.user.UserDto;
import java.util.List;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TeachersPageController {

  private final UserService userService;

  private final StudentService studentService;

  private final CommonOperation commonOperation;

  @FXML
  private TableView<UserDto> teachersTable;

  @FXML
  private TableColumn<UserDto, Integer> idColumn;

  @FXML
  private TableColumn<UserDto, String> nameColumn;

  @FXML
  private TableColumn<UserDto, String> mailColumn;

  @FXML
  private TextField searchField;

  @FXML
  private void initialize() {
    idColumn.setCellValueFactory(
        p -> new ReadOnlyObjectWrapper<>(teachersTable.getItems().indexOf(p.getValue()) + 1));
    nameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
    mailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
    commonOperation.addDoubleClickEvent(teachersTable, this::doubleClickEvent);
    fillTable();
  }

  private void doubleClickEvent(UserDto userDto) {
    CommonOperation.confirmationAlert("Підпис", "Підписатись на вчителя",
        String.format("Ви впевнені, що хочете підписатись на %s?", userDto.getUsername()),
        Event::consume,
        actionEvent -> {
          studentService.assignMeToTeacher(userDto.getId());
          commonOperation.showInformation("Успіх", "Успішна підписка",
              String.format("Ви успішно підписались на %s", userDto.getUsername()));
        });
  }

  private void fillTable() {
    List<UserDto> teachers = userService.getTeachers();
    commonOperation.setFilteredAndFillTable(teachersTable, searchField, teachers,
        (s, userDto) -> userDto.getUsername().toLowerCase().contains(s)
            || userDto.getEmail().toLowerCase().contains(s));
  }
}
