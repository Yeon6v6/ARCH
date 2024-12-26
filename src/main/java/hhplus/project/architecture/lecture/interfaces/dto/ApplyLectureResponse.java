package hhplus.project.architecture.lecture.interfaces.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplyLectureResponse {
    private String code; //응답(리턴)코드
    private String message; //응답(리턴)메시지

    // 성공 응답
    public static ApplyLectureResponse success(String message) {
        return new ApplyLectureResponse("SUCCESS", message);
    }

    // 실패 응답
    public static ApplyLectureResponse failure(String message) {
        return new ApplyLectureResponse("FAIL", message);
    }
}
