package hhplus.project.architecture.lecture.interfaces.dto;

import hhplus.project.architecture.lecture.application.dto.LectureAppliedHistResponse;
import lombok.*;

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

    public static ApplyHistoryResponse from(LectureAppliedHistResponse lectureAppliedHistResponse) {
        return new ApplyHistoryResponse(
                lectureAppliedHistResponse.getLectureId(),
                lectureAppliedHistResponse.getLectureName(),
                lectureAppliedHistResponse.getCoach(),
                lectureAppliedHistResponse.getLectureDate(),
                lectureAppliedHistResponse.getRegDate()
        );
    }
}
