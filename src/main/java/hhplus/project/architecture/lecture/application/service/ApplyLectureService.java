package hhplus.project.architecture.lecture.application.service;

import hhplus.project.architecture.lecture.application.dto.ApplyLectureServiceRequest;
import hhplus.project.architecture.lecture.domain.entity.ApplyHistory;
import hhplus.project.architecture.lecture.domain.entity.Lecture;
import hhplus.project.architecture.lecture.domain.repository.ApplyHistoryRepository;
import hhplus.project.architecture.lecture.domain.repository.LectureRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ApplyLectureService {

    private final LectureRepository lectureRepository;
    private final ApplyHistoryRepository applyHistoryRepository;

    // 강의 신청 API
    public void applyForLecture(ApplyLectureServiceRequest request) {
        Lecture lecture = lectureRepository.findById(request.getLectureId())
                .orElseThrow(() -> new EntityNotFoundException("강의를 찾을 수 없습니다."));
        
        //이미 신청했는지 체크
        if(applyHistoryRepository.isAppliedLectureByUserId(request.getUserId(), request.getLectureId())){
            throw new IllegalArgumentException("이미 신청한 강의입니다.");
        }

        //강의 정원 초과 여부 확인
        if (!lecture.isAvailable()) {
            throw new IllegalStateException("강의 정원이 초과되었습니다.");
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
    }

    // 강의 목록 조회 API
    public List<Lecture> getAvailableLectures(LocalDate date) {
        return lectureRepository.findAvailableLecturesByDate(date);
    }

    // 신청 완료 목록 조회 API
    public List<ApplyHistory> getAppliedLectures(Long userId) {
        return applyHistoryRepository.findApplyHistoryByUserId(userId);
    }
}
