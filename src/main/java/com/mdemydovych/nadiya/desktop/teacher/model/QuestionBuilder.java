package com.mdemydovych.nadiya.desktop.teacher.model;

import com.mdemydovych.nadiya.model.examination.core.VariantDto;
import java.util.List;
import lombok.Data;

@Data
public class QuestionBuilder {

  private String description;

  private String imagePath;

  private List<VariantDto> variants = List.of();
}
