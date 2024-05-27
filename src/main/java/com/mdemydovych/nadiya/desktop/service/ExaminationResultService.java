package com.mdemydovych.nadiya.desktop.service;

import com.mdemydovych.nadiya.desktop.config.properties.BackEndProperties;
import com.mdemydovych.nadiya.model.examination.result.ExaminationResultDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExaminationResultService {

  private final QueryExecutorService executorService;

  private final BackEndProperties properties;

  public List<ExaminationResultDto> findAllExamResults(String examId) {
    return List.of(executorService.request(properties.getFindAllExamResultsPath(),
        HttpMethod.GET, null, ExaminationResultDto[].class, examId));
  }
}
