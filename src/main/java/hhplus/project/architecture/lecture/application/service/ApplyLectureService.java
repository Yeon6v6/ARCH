package hhplus.project.architecture.lecture.application.service;

import hhplus.project.architecture.lecture.application.dto.ApplyLectureServiceRequest;
import hhplus.project.architecture.lecture.application.dto.LectureAppliedHistResponse;
import hhplus.project.architecture.lecture.domain.entity.ApplyHistory;
import hhplus.project.architecture.lecture.domain.entity.Lecture;
import hhplus.project.architecture.lecture.domain.repository.ApplyHistoryRepository;
import hhplus.project.architecture.lecture.domain.repository.LectureRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ApplyLectureService {

    private final LectureRepository lectureRepository;
    private final ApplyHistoryRepository applyHistoryRepository;

    // 강의 신청 API
    @Transactional
    public void applyLecture(ApplyLectureServiceRequest request) {
        // 강의 Entity를 비관적 락 잠금으로 갖고오기
        Lecture lecture = lectureRepository.findByLectureWithLock(request.getLectureId())
                .orElseThrow(() -> new EntityNotFoundException("강의를 찾을 수 없습니다."));

        // 이미 신청했는지 체크
        if (applyHistoryRepository.isAppliedLectureByUserId(request.getUserId(), request.getLectureId())) {
            throw new IllegalArgumentException("이미 신청한 강의입니다.");
        }

        // 강의 정원 초과 여부 확인
        if (!lecture.isAvailable()) {
            throw new IllegalStateException("강의 정원이 초과되었습니다.");
        }
        
        // 신청처리
        lecture.apply();
        lectureRepository.save(lecture); //증가한 인원으로 저장

        // 신청 기록 저장
        ApplyHistory applyHistory = ApplyHistory.builder()
                .userId(request.getUserId())
                .lectureId(request.getLectureId())
                .status("SUCCESS")
                .regDate(LocalDateTime.now())
                .build();
        applyHistoryRepository.save(applyHistory);
    }

    // 강의 목록 조회 API
    public List<Lecture> getAvailableLectures(LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }
        // LocalDate를 LocalDateTime 범위로 변환
        LocalDateTime startDateTime = date.atStartOfDay();
        LocalDateTime endDateTime = date.atTime(LocalTime.MAX);

        return lectureRepository.findAvailableLecturesByDate(startDateTime, endDateTime);
    }

    // 신청 완료 목록 조회 API
    public List<LectureAppliedHistResponse> getAppliedLectures(Long userId) {
        List<ApplyHistory> successHist = applyHistoryRepository.findApplyHistoryByUserId(userId);

        // lectureId 목록이 비어 있으면 빈 리스트 반환
        if (successHist.isEmpty()) {
            return Collections.emptyList();
        }

        return successHist.stream()
                .map(history -> {
                    Lecture lecture = lectureRepository.findById(history.getLectureId())
                            .orElseThrow(() -> new IllegalArgumentException("해당 강의를 찾을 수 없습니다. lectureId: " + history.getLectureId()));
                    return LectureAppliedHistResponse.fromEntities(history, lecture);
                })
                .collect(Collectors.toList());
    }
}
