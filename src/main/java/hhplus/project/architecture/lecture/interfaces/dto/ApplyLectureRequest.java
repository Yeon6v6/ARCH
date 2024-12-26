package hhplus.project.architecture.lecture.interfaces.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplyLectureRequest {
    private Long userId;
    private Long lectureId;
}
