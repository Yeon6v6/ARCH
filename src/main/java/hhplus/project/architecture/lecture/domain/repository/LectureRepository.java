package hhplus.project.architecture.lecture.domain.repository;

import hhplus.project.architecture.lecture.domain.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Long> {

    // 날짜별로 신청 가능한 강의 목록 조회
    @Query("SELECT l FROM Lecture l " +
            "WHERE l.lectureDate BETWEEN :startDateTime AND :endDateTime " +
            "AND l.appliedCnt < l.capacity")
    List<Lecture> findAvailableLecturesByDate(@Param("startDateTime") LocalDateTime startDateTime,
                                              @Param("endDateTime") LocalDateTime endDateTime);
}