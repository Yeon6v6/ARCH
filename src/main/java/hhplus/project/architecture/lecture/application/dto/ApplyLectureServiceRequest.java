package hhplus.project.architecture.lecture.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApplyLectureServiceRequest {

    private Long userId;
    private Long lectureId;
}
