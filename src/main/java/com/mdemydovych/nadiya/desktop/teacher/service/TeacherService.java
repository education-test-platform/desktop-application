package com.mdemydovych.nadiya.desktop.teacher.service;

import com.mdemydovych.nadiya.desktop.config.properties.BackEndProperties;
import com.mdemydovych.nadiya.desktop.service.QueryExecutorService;
import com.mdemydovych.nadiya.model.examination.core.ExaminationDto;
import com.mdemydovych.nadiya.model.examination.core.ExaminationPreview;
import com.mdemydovych.nadiya.model.user.UserDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeacherService {

  private final QueryExecutorService executorService;

  private final BackEndProperties properties;

  public List<ExaminationPreview> findMyExaminations() {
    return List.of(executorService.request(properties.getMyExamsPath(),
        HttpMethod.GET, null, ExaminationPreview[].class));
  }

  public List<UserDto> getMyStudents() {
    return List.of(executorService.request(properties.getMyStudentsPath(),
        HttpMethod.GET, null, UserDto[].class));
  }

  public void meSaveExamination(ExaminationDto examination) {
    executorService.request(properties.getMeSaveExamination(),
        HttpMethod.POST, examination, Void.class);
  }
}
