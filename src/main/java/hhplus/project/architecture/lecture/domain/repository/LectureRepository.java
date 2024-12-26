package hhplus.project.architecture.lecture.domain.repository;

import hhplus.project.architecture.lecture.domain.entity.Lecture;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Long> {

    // 강의 엔티티를 비관적 락으로 조회
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT l FROM Lecture l WHERE l.lectureId = :lectureId")
    Optional<Lecture> findByLectureWithLock(@Param("lectureId") Long lectureId);

    // 날짜별로 신청 가능한 강의 목록 조회
    @Query("SELECT l FROM Lecture l " +
            "WHERE l.lectureDate BETWEEN :startDateTime AND :endDateTime " +
            "AND l.appliedCnt < l.capacity")
    List<Lecture> findAvailableLecturesByDate(@Param("startDateTime") LocalDateTime startDateTime,
                                              @Param("endDateTime") LocalDateTime endDateTime);
}