DROP TABLE IF EXISTS Lecture;
CREATE TABLE Lecture (
     lecture_id BIGINT PRIMARY KEY,
     lecture_name VARCHAR(255) NOT NULL,
     coach VARCHAR(255),
     lecture_date TIMESTAMP,
     applied_cnt INT,
     capacity INT
);

DROP TABLE IF EXISTS ApplyHistory;
CREATE TABLE ApplyHistory(
    apply_id BIGINT PRIMARY KEY,
    lecture_id BIGINT,
    user_id BIGINT,
    status varchar(255),
    reg_date timestamp
);