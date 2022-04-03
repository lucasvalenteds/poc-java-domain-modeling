package com.example.features;

import com.example.domain.student.Student;
import com.example.domain.student.StudentId;

import java.util.UUID;

public interface StudentManagement {

    Student createStudent(String firstName, String lastName);

    void removeStudent(StudentId id);

    Student findStudentById(StudentId id);
}
