package com.example.persistence.student;

import com.example.domain.student.StudentId;

import java.io.Serial;

public final class StudentPersistenceException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -4792104932246771945L;

    private final StudentId studentId;

    public StudentPersistenceException(StudentId studentId, String message, Throwable throwable) {
        super(message, throwable);
        this.studentId = studentId;
    }

    public StudentId getStudentId() {
        return studentId;
    }
}
