package com.mdemydovych.nadiya.desktop.teacher.controller;

import com.mdemydovych.nadiya.desktop.broker.resolver.impl.BrokerAwareAbstract;
import com.mdemydovych.nadiya.desktop.model.BrokerEvent;
import com.mdemydovych.nadiya.desktop.service.CommonOperation;
import com.mdemydovych.nadiya.desktop.teacher.service.TeacherService;
import com.mdemydovych.nadiya.model.user.UserDto;
import java.util.List;
import java.util.Objects;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MyStudentsPageController extends BrokerAwareAbstract {

  private final TeacherService teacherService;

  private final CommonOperation commonOperation;

  @FXML
  private TableView<UserDto> studentsTable;

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
        p -> new ReadOnlyObjectWrapper<>(studentsTable.getItems().indexOf(p.getValue()) + 1));
    nameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
    mailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
    fillTable();
  }

  @Override
  public void handleEvent() {
    if (Objects.nonNull(studentsTable) && isActiveWindow(studentsTable.getScene())) {
      refreshTable();
    }
  }

  private void refreshTable() {
    fillTable();
    studentsTable.refresh();
  }

  private void fillTable() {
    List<UserDto> students = teacherService.getMyStudents();
    commonOperation.setFilteredAndFillTable(studentsTable, searchField, students,
        (s, userDto) -> userDto.getUsername().toLowerCase().contains(s)
            || userDto.getEmail().toLowerCase().contains(s));
  }

  @Override
  public boolean support(BrokerEvent event) {
    return event == BrokerEvent.ASSIGN_STUDENT;
  }
}
