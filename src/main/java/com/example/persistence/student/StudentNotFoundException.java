package com.example.persistence.student;

import com.example.domain.student.StudentId;
import com.example.infrastructure.errors.ResourceNotFoundException;

import java.io.Serial;

public final class StudentNotFoundException extends ResourceNotFoundException {

    @Serial
    private static final long serialVersionUID = -4880228168062763993L;

    private final transient StudentId studentId;

    public StudentNotFoundException(StudentId studentId, Throwable throwable) {
        super("Student not found by ID", throwable);
        this.studentId = studentId;
    }

    public StudentId getStudentId() {
        return studentId;
    }
}
