package hhplus.project.architecture.lecture;

import hhplus.project.architecture.lecture.domain.entity.Lecture;
import hhplus.project.architecture.lecture.domain.repository.LectureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

public class ApplyLectureServiceTest {

    @Mock
    private LectureRepository lectureRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this); // Mock 객체 초기화
    }

    @Test
    void 강의_저장_조회_테스트() {
        Lecture lecture = new Lecture();
        lecture.setLectureId(1L);
        lecture.setLectureName("Spring 강의");

        when(lectureRepository.findById(1L)).thenReturn(Optional.of(lecture));

        Optional<Lecture> result = lectureRepository.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getLectureName()).isEqualTo("Spring 강의");
    }

    @Test
    void 강의_정원_확인() {
        Lecture lecture = new Lecture();
        lecture.setCapacity(30);
        lecture.setAppliedCnt(30);

        assertThat(lecture.isAvailable()).isFalse();
    }

    @Test
    void 강의_신청_성공() {
        Lecture lecture = new Lecture();
        lecture.setCapacity(30);
        lecture.setAppliedCnt(29);

        lecture.apply();

        assertThat(lecture.getAppliedCnt()).isEqualTo(30);
        assertThat(lecture.isAvailable()).isFalse();
    }

    @Test
    void 강의_정원_초과_확인() {
        Lecture lecture = new Lecture();
        lecture.setCapacity(30);
        lecture.setAppliedCnt(30);

        boolean canApply = lecture.isAvailable();

        assertThat(canApply).isFalse();

        // 강의 신청을 강제로 호출 시 예외 발생
        assertThatThrownBy(lecture::apply)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("강의 신청 인원이 마감되었습니다.");
    }
}
