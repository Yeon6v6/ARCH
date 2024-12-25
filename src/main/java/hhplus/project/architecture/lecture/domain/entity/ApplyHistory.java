package hhplus.project.architecture.lecture.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name="ApplyHistory")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ApplyHistory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applyId;

    @Column(nullable = false, length = 50)
    private Long lectureId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(nullable = false)
    private LocalDateTime regdate;

    // 강의 Entity와 물리적으로 연결하지 않도록 설정
    @ManyToOne(optional = true)
    @JoinColumn(insertable = false, updatable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Lecture lecture;

    // 관계를 끊더라도 lectureId를 통해 별도로 강의를 갖고오도록 연결
    public void linkLecture(Lecture lecture) {
        if (lecture != null) {
            this.lectureId = lecture.getLectureId();
            this.lecture = lecture;
        }
    }
}


