package hhplus.project.architecture.lecture;

import hhplus.project.architecture.lecture.application.dto.ApplyLectureServiceRequest;
import hhplus.project.architecture.lecture.application.service.ApplyLectureService;
import hhplus.project.architecture.lecture.domain.entity.ApplyHistory;
import hhplus.project.architecture.lecture.domain.entity.Lecture;
import hhplus.project.architecture.lecture.domain.repository.ApplyHistoryRepository;
import hhplus.project.architecture.lecture.domain.repository.LectureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class DuplicateApplicationTest {

    @Autowired
    private ApplyLectureService applyLectureService;

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private ApplyHistoryRepository applyHistoryRepository;

    @BeforeEach
    void setUp() {
        // 기존 데이터 삭제
        applyHistoryRepository.deleteAll();
        lectureRepository.deleteAll();

        // 테스트용 초기 데이터 구축
        Lecture lecture = Lecture.builder()
                .lectureName("동일한 사용자 신청 강의")
                .coach("김코치")
                .capacity(30)
                .build();
        lectureRepository.save(lecture);
    }

    @Test
    /**
     * 한 사용자가 한 강의에 대해 중복해서 신청했을 때, 하나만 신청되는 것을 검증하는 통합 테스트
     */
    public void testSingleUserMultipleApplications() throws InterruptedException {
        Lecture lecture = lectureRepository.findAll().get(0);
        Long userId = 1L;

        // 1. 동일한 사용자가 반복 신청하기 위한 스레드 설정
        int threadCount = 5;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executor.execute(() -> {
                try {
                    applyLectureService.applyLecture(
                            new ApplyLectureServiceRequest(userId, lecture.getLectureId()));
                } catch (Exception e) {
                    System.err.println("User " + userId + " failed to apply: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        // 2. 모든 스레드가 완료될 때까지 대기
        latch.await();

        // 3. 신청 기록 확인
        List<ApplyHistory> allHistories = applyHistoryRepository.findAll();
        List<ApplyHistory> filteredHistories = allHistories.stream()
                .filter(history -> history.getLectureId().equals(lecture.getLectureId()) && history.getUserId().equals(userId))
                .toList();

        assertEquals(1, filteredHistories.size());
        assertEquals(userId, filteredHistories.get(0).getUserId());
        assertEquals(lecture.getLectureId(), filteredHistories.get(0).getLectureId());
    }
}
