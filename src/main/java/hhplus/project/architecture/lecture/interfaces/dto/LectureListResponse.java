package hhplus.project.architecture.lecture.interfaces.dto;

import hhplus.project.architecture.lecture.domain.entity.Lecture;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LectureListResponse {
    private Long lectureId;
    private String lectureName;
    private String coach;
    private LocalDateTime lectureDate;
    private int capacity;
    private int appliedCount;

    // Entity에서 DTO로 변환
    public static LectureListResponse fromEntity(Lecture lecture) {
        return new LectureListResponse(
                lecture.getLectureId(),
                lecture.getLectureName(),
                lecture.getCoach(),
                lecture.getLectureDate(),
                lecture.getCapacity(),
                lecture.getAppliedCount()
        );
    }
}
