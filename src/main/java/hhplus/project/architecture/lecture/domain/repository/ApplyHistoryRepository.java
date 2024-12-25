package hhplus.project.architecture.lecture.domain.repository;

import hhplus.project.architecture.lecture.domain.entity.ApplyHistory;
import hhplus.project.architecture.lecture.domain.entity.Lecture;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplyHistoryRepository extends JpaRepository<ApplyHistory, Long> {

    // 사용자의 강의 신청 여부 확인
    @Query("SELECT COUNT(a) > 0 FROM ApplyHistory a WHERE a.userId = :userId AND a.lectureId = :lectureId")
    boolean isAppliedLectureByUserId(@Param("userId") Long userId, @Param("lectureId") Long lectureId);

    // 신청 내역 조회
    @Query("SELECT a FROM ApplyHistory a WHERE a.userId = :userId")
    List<ApplyHistory> findApplyHistoryByUserId(@Param("userId") Long userId);

    // 특정 강의에 대한 신청 내역 조회
    @Query("SELECT a FROM ApplyHistory a WHERE a.lectureId = :lectureId")
    List<ApplyHistory> findApplyHistoryByLectureId(@Param("lectureId") Long lectureId);
}
