package hhplus.project.architecture.lecture.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Lecture")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Lecture{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="lecture_id")
    //@GeneratedValue(generator = "UUID") @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private Long lectureId;

    @Column(name="lecture_name", nullable = false, length = 100)
    private String lectureName;

    @Column(nullable = false, length = 100)
    private String coach;

    @Column(name="lecture_date", nullable = false)
    private LocalDateTime lectureDate;  //강의시간

    @Column(name="applied_cnt", nullable = false)
    private int appliedCnt; //현재 신청한 인원 수
    
    @Column(nullable = false)
    private int capacity = 30; //강의 최대 수용 인원

    //강의 남은 자리 확인
    public boolean isAvailable() {
        return appliedCnt < capacity;
    }

    //강의 신청 처리
    public void apply(){
        if(!isAvailable()){
            throw new IllegalStateException("강의 신청 인원이 마감되었습니다.");
        }
        this.appliedCnt++;
    }

}
