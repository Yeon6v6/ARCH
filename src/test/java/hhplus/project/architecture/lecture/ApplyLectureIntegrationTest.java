package hhplus.project.architecture.lecture;

import hhplus.project.architecture.lecture.application.dto.ApplyLectureServiceRequest;
import hhplus.project.architecture.lecture.application.service.ApplyLectureService;
import hhplus.project.architecture.lecture.domain.entity.Lecture;
import hhplus.project.architecture.lecture.domain.repository.ApplyHistoryRepository;
import hhplus.project.architecture.lecture.domain.repository.LectureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 동시에 동일한 특강에 대해 40명이 신청했을 때, 30명만 성공하는 것을 검증하는 통합 테스트
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplyLectureIntegrationTest {

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
                .appliedCnt(0)
                .build();
        lectureRepository.save(lecture);
    }

    @Test
    public void testConcurrentLectureApplications() throws InterruptedException {
        Lecture lecture = lectureRepository.findAll().get(0);
        // 1. 동시 신청을 위한 스레드 설정
        int threadCount = 40;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (long i = 1; i < threadCount+1; i++) {
            long userId = i;
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

        // 2. 모든 스레드 완료 대기
        latch.await();

        // 3. 강의 신청에 성공한 인원 확인
        Lecture lecutre = lectureRepository.findById(lecture.getLectureId()).orElseThrow();
        assertEquals(30, lecutre.getAppliedCnt()); // 신청 인원 확인
        System.out.println("Applied Count: " + lecutre.getAppliedCnt());

    }
}