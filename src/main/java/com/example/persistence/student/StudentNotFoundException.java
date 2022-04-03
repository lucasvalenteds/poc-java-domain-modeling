package com.example.persistence.student;

import com.example.domain.student.StudentId;

import java.io.Serial;

public final class StudentNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -4880228168062763993L;

    private final StudentId studentId;

    public StudentNotFoundException(StudentId studentId, Throwable throwable) {
        super("Student not found by ID", throwable);
        this.studentId = studentId;
    }

    public StudentId getStudentId() {
        return studentId;
    }
}
