--DELETE FROM Lecture;

INSERT INTO Lecture (lecture_id, lecture_name, coach, lecture_date, applied_cnt, capacity)
VALUES
(1, 'Spring Boot 실습', 'John Doe', '2024-12-25 10:00:00', 0, 30),
(2, 'Java 기초', 'Jane Smith', '2024-12-26 14:00:00', 15, 30),
(3, 'Microservices 아키텍처', 'Alice Johnson', '2024-12-27 09:00:00', 30, 30),
(4, 'DevOps 워크샵', 'Bob Lee', '2024-12-28 13:00:00', 10, 20),
(5, 'Kubernetes 소개', 'Eve Kim', '2024-12-29 16:00:00', 5, 15),
(6, 'Docker 초급', 'Charlie Brown', '2024-12-30 10:00:00', 25, 30);

INSERT INTO ApplyHistory (apply_id, lecture_id, user_id, status, reg_date)
VALUES
(1, 1, 101, 'SUCCESS', '2024-12-20 09:00:00'),
(2, 2, 101, 'PENDING', '2024-12-21 10:30:00'),
(3, 3, 102, 'SUCCESS', '2024-12-22 14:45:00'),
(4, 4, 103, 'FAIL', '2024-12-23 08:15:00'),
(5, 5, 104, 'SUCCESS', '2024-12-24 18:00:00'),
(6, 6, 101, 'CANCEL', '2024-12-25 11:30:00'),
(7, 3, 104, 'PENDING', '2024-12-22 14:50:00'),
(8, 5, 105, 'FAIL', '2024-12-24 15:00:00'),
(9, 6, 105, 'SUCCESS', '2024-12-25 11:45:00'),
(10, 4, 106, 'PENDING', '2024-12-27 13:30:00');
