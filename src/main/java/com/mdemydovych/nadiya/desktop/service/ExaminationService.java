package com.mdemydovych.nadiya.desktop.service;

import com.mdemydovych.nadiya.desktop.config.properties.BackEndProperties;
import com.mdemydovych.nadiya.model.examination.core.ExaminationAo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExaminationService {

  private final QueryExecutorService executorService;

  private final BackEndProperties properties;

  public ExaminationAo findById(String id) {
    return executorService.request(properties.getExaminationByIdPath(),
        HttpMethod.GET, null, ExaminationAo.class, id);
  }
}
