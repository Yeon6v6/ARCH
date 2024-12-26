package hhplus.project.architecture.lecture.application.dto;

import hhplus.project.architecture.lecture.domain.entity.ApplyHistory;
import hhplus.project.architecture.lecture.domain.entity.Lecture;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LectureAppliedHistResponse {
    private Long lectureId;
    private String lectureName;
    private String coach;
    private LocalDateTime lectureDate;
    private LocalDateTime regDate; // 신청 시간

    public static LectureAppliedHistResponse fromEntities(ApplyHistory applyHistory, Lecture lecture) {
        return new LectureAppliedHistResponse(
                lecture.getLectureId(),
                lecture.getLectureName(),
                lecture.getCoach(),
                lecture.getLectureDate(),
                applyHistory.getRegDate()
        );
    }
}
