package hhplus.project.architecture.lecture.interfaces.controller;

import hhplus.project.architecture.lecture.application.service.ApplyLectureService;
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

@RestController
@RequestMapping("/lectures")
@AllArgsConstructor
public class ApplyLectureController {

    private final ApplyLectureService applyLectureService;

    // 강의 신청 API
    @PostMapping("/{lectureId}/apply")
    public ResponseEntity<ApplyLectureResponse> applyForLecture(@PathVariable @NotNull Long lectureId, @RequestBody ApplyLectureRequest request) {
        request.setLectureId(lectureId); //DTO에 LectureId 세팅
        ApplyLectureResponse response = applyLectureService.applyForLecture(request);
        HttpStatus status = ObjectUtils.isEmpty(response) ? HttpStatus.BAD_REQUEST : HttpStatus.OK;
        return ResponseEntity.status(status).body(response);
}


// 강의 목록 조회 API
@GetMapping("/available")
    public ResponseEntity<List<LectureListResponse>> getAvailableLectures(@RequestParam(defaultValue = "#{T(java.time.LocalDate).now()") LocalDate date) {
        List<LectureListResponse> response = applyLectureService.getAvailableLectures(date);
    if (ObjectUtils.isEmpty(response)) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    return ResponseEntity.ok(response);
    }

    // 신청 완료 목록 조회 API
    @GetMapping("/{userId}/applied")
    public ResponseEntity<List<ApplyHistoryResponse>> getAppliedLectures(@PathVariable @NotNull Long userId) {
        List<ApplyHistoryResponse> response = applyLectureService.getAppliedLectures(userId);
        if (ObjectUtils.isEmpty(response)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(response);
    }
}
