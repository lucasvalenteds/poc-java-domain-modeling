CREATE TABLE COURSE (
    ID UUID PRIMARY KEY,
    CODE VARCHAR,
    TITLE VARCHAR
);

CREATE TABLE STUDENT (
  ID UUID PRIMARY KEY,
  FIRST_NAME VARCHAR,
  LAST_NAME VARCHAR
);

CREATE TABLE ENROLLMENT (
  ID UUID PRIMARY KEY,
  STUDENT_ID UUID NOT NULL,
  COURSE_ID UUID NOT NULL,

  FOREIGN KEY (STUDENT_ID) REFERENCES STUDENT(ID),
  FOREIGN KEY (COURSE_ID) REFERENCES COURSE(ID)
);

CREATE TABLE RATING (
  STUDENT_ID UUID NOT NULL,
  COURSE_ID UUID NOT NULL,
  RATING SMALLINT,

  FOREIGN KEY (STUDENT_ID) REFERENCES STUDENT(ID),
  FOREIGN KEY (COURSE_ID) REFERENCES COURSE(ID)
);