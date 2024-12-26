package hhplus.project.architecture.lecture.interfaces.controller;

import hhplus.project.architecture.lecture.application.dto.ApplyLectureServiceRequest;
import hhplus.project.architecture.lecture.application.dto.LectureAppliedHistResponse;
import hhplus.project.architecture.lecture.application.service.ApplyLectureService;
import hhplus.project.architecture.lecture.domain.entity.Lecture;
import hhplus.project.architecture.lecture.interfaces.dto.ApplyHistoryResponse;
import hhplus.project.architecture.lecture.interfaces.dto.ApplyLectureRequest;
import hhplus.project.architecture.lecture.interfaces.dto.ApplyLectureResponse;
import hhplus.project.architecture.lecture.interfaces.dto.LectureListResponse;
import lombok.AllArgsConstructor;

import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/lectures")
@AllArgsConstructor
public class ApplyLectureController {

    private final ApplyLectureService applyLectureService;

    // 강의 신청 API
    @PostMapping("/{lectureId}/apply")
    public ResponseEntity<ApplyLectureResponse> applyForLecture(@PathVariable("lectureId") @NotNull Long lectureId, @RequestBody ApplyLectureRequest request) {
        ApplyLectureServiceRequest serviceRequest = new ApplyLectureServiceRequest(request.getUserId(), lectureId);
        applyLectureService.applyForLecture(serviceRequest);
        return ResponseEntity.ok(ApplyLectureResponse.success("강의 신청이 완료되었습니다."));
    }


    // 강의 목록 조회 API
    @GetMapping("/available")
        public ResponseEntity<List<LectureListResponse>> getAvailableLectures(@RequestParam(name = "date", required = false, defaultValue = "#{T(java.time.LocalDate).now()") LocalDate date) {
            List<Lecture> lectures = applyLectureService.getAvailableLectures(date);
            List<LectureListResponse> response = lectures.stream()
                    .map(LectureListResponse::fromEntity)
                    .collect(Collectors.toList());
        if (ObjectUtils.isEmpty(response)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(response);
    }

    // 신청 완료 목록 조회 API
    @GetMapping("/{userId}/applied")
    public ResponseEntity<List<ApplyHistoryResponse>> getAppliedLectures(@PathVariable("userId") @NotNull Long userId) {
        List<LectureAppliedHistResponse> serviceResponse = applyLectureService.getAppliedLectures(userId);

        // Service Layer의 결과를 DTO로 변환
        List<ApplyHistoryResponse> response = serviceResponse.stream()
                .map(ApplyHistoryResponse::from)
                .collect(Collectors.toList());

        if (response.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(response);
    }
}
