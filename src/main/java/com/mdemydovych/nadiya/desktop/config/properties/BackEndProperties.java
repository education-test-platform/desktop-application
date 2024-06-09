package com.mdemydovych.nadiya.desktop.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app.paths")
public class BackEndProperties {

  private String myActiveExamsPath = "/backend/student/my/active/exams";

  private String examinationByIdPath = "/backend/examination/{id}";

  private String meSaveExaminationResult = "/backend/student/me/save/exam/result";

  private String meSaveExamination = "/backend/teacher/me/save/examination";

  private String assignMeToTeacherPath = "/backend/student/me/assign/to/teacher/{teacherId}";

  private String myTeachersPath = "/backend/student/my/teachers";

  private String getTeachersPath = "/backend/student/get/teachers";

  private String myInfoPath = "/my/info";

  private String myAllExamResultsPath = "/backend/student/my/all/exam/results";

  private String myStudentsPath = "/backend/teacher/my/students";

  private String myExamsPath = "/backend/teacher/my/examinations";

  private String findAllExamResultsPath = "/backend/examination/result/find/{examId}";

  private String meChangeExamStatus = "/backend/teacher/me/change/exam/status/{examId}";
}
