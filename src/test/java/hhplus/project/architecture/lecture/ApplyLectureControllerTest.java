package hhplus.project.architecture.lecture;

import hhplus.project.architecture.lecture.interfaces.dto.ApplyLectureRequest;
import hhplus.project.architecture.lecture.interfaces.dto.ApplyLectureResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS) // 중복 실행 방지
public class ApplyLectureControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void 강의_신청_API_E2E_테스트() {
        // Given: 강의 신청 요청 생성
        ApplyLectureRequest request = new ApplyLectureRequest(101L, 1L);
        HttpEntity<ApplyLectureRequest> entity = new HttpEntity<>(request);

        // When: API 호출
        ResponseEntity<ApplyLectureResponse> response = restTemplate.postForEntity("/lectures/1/apply", entity, ApplyLectureResponse.class);

        // Then: 응답 검증
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo("SUCCESS");
        assertThat(response.getBody().getMessage()).isEqualTo("강의 신청이 완료되었습니다.");
    }

    @Test
    void 신청_가능한_강의_조회_API_테스트() {
        // When: 강의 목록 조회 API 호출(없을 경우 오늘 날짜를 기준으로 실행)
        ResponseEntity<String> response = restTemplate.getForEntity("/lectures/available?date=2024-12-25", String.class);

        // Then: 응답 검증
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).contains("lectureId", "lectureName", "coach");
    }

    @Test
    void 신청_완료된_강의_조회_API_테스트() {
        // When: 특정 사용자 신청 완료 목록 조회 API 호출
        ResponseEntity<String> response = restTemplate.getForEntity("/lectures/101/applied", String.class);

        // Then: 응답 검증
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).contains("lectureId", "lectureName", "coach");
    }
}
