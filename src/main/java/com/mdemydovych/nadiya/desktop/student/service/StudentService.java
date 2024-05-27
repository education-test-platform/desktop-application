package com.mdemydovych.nadiya.desktop.student.service;

import com.mdemydovych.nadiya.desktop.config.properties.BackEndProperties;
import com.mdemydovych.nadiya.desktop.service.QueryExecutorService;
import com.mdemydovych.nadiya.model.examination.core.ExaminationPreview;
import com.mdemydovych.nadiya.model.examination.result.ExaminationResultDto;
import com.mdemydovych.nadiya.model.user.UserDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentService {

  private final QueryExecutorService executorService;

  private final BackEndProperties properties;

  public void assignMeToTeacher(String teacherId) {
    executorService.request(properties.getAssignMeToTeacherPath(),
        HttpMethod.GET, null, Void.class, teacherId);
  }

  public List<UserDto> getMyTeachers() {
    return List.of(executorService.request(properties.getMyTeachersPath(),
        HttpMethod.GET, null, UserDto[].class));
  }

  public List<ExaminationResultDto> findMyExamResults() {
    return List.of(executorService.request(properties.getMyAllExamResultsPath(),
        HttpMethod.GET, null, ExaminationResultDto[].class));
  }

  public List<ExaminationPreview> myActiveExams() {
    return List.of(
        executorService.request(properties.getMyActiveExamsPath(),
            HttpMethod.GET, null, ExaminationPreview[].class));
  }

  public void saveExamResult(ExaminationResultDto result) {
    executorService.request(properties.getMeSaveExaminationResult(),
        HttpMethod.POST, result, Void.class);
  }
}
