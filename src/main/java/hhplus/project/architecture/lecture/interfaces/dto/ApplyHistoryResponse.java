package hhplus.project.architecture.lecture.interfaces.dto;

import hhplus.project.architecture.lecture.domain.entity.ApplyHistory;
import hhplus.project.architecture.lecture.domain.entity.Lecture;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplyHistoryResponse {
    private Long lectureId;
    private String lectureName;
    private String coach;
    private LocalDateTime lectureDate;
    private LocalDateTime regDate;

    public static ApplyHistoryResponse fromEntity(ApplyHistory applyHistory) {
        Lecture lecture = applyHistory.getLecture(); // Lecture 객체 가져오기
        return new ApplyHistoryResponse(
                lecture.getLectureId(),
                lecture.getLectureName(),
                lecture.getCoach(),
                lecture.getLectureDate(),
                applyHistory.getRegdate()
        );
    }
}
