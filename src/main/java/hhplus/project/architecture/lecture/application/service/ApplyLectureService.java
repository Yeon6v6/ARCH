package hhplus.project.architecture.lecture.application.service;

import hhplus.project.architecture.lecture.domain.entity.ApplyHistory;
import hhplus.project.architecture.lecture.domain.entity.Lecture;
import hhplus.project.architecture.lecture.domain.repository.ApplyHistoryRepository;
import hhplus.project.architecture.lecture.domain.repository.LectureRepository;
import hhplus.project.architecture.lecture.interfaces.dto.ApplyHistoryResponse;
import hhplus.project.architecture.lecture.interfaces.dto.ApplyLectureRequest;
import hhplus.project.architecture.lecture.interfaces.dto.ApplyLectureResponse;
import hhplus.project.architecture.lecture.interfaces.dto.LectureListResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ApplyLectureService {

    private final LectureRepository lectureRepository;
    private final ApplyHistoryRepository applyHistoryRepository;

    // 강의 신청 API
    public ApplyLectureResponse applyForLecture(ApplyLectureRequest request) {
        Lecture lecture = lectureRepository.findById(request.getLectureId())
                .orElseThrow(() -> new EntityNotFoundException("강의를 찾을 수 없습니다."));
        
        //이미 신청했는지 체크
        if(applyHistoryRepository.isAppliedLectureByUserId(request.getUserId(), request.getLectureId())){
            return ApplyLectureResponse.failure("이미 신청한 강의입니다.");
        }

        //강의 정원 초과 여부 확인
        if (!lecture.isAvailable()) {
            return ApplyLectureResponse.failure("강의 정원이 초과되었습니다.");
        }

        //신청처리
        ApplyHistory applyHistory = ApplyHistory.builder()
                .userId(request.getUserId())
                .lecture(lecture)
                .regdate(LocalDateTime.now())
                .build();

        applyHistoryRepository.save(applyHistory);
        lecture.apply();
        lectureRepository.save(lecture);
        
        return ApplyLectureResponse.success("강의 신청이 완료되었습니다");
    }

    // 강의 목록 조회 API
    public List<LectureListResponse> getAvailableLectures(LocalDate date) {
        return lectureRepository.findAvailableLecturesByDate(date)
                .stream()
                .map(LectureListResponse::fromEntity) // 메서드 참조 방식
                .collect(Collectors.toList());
    }

    // 신청 완료 목록 조회 API
    public List<ApplyHistoryResponse> getAppliedLectures(Long userId) {
        return applyHistoryRepository.findApplyHistoryByUserId(userId)
                .stream()
                .map(ApplyHistoryResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
