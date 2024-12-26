package hhplus.project.architecture.lecture.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name="t_apply_history")
@Getter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ApplyHistory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="apply_id")
    private Long applyId;

    @Column(name="lecture_id", nullable = false)
    private Long lectureId;

    @Column(name="user_id", nullable = false)
    private Long userId;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(name="reg_date", nullable = false)
    private LocalDateTime regDate;

}


