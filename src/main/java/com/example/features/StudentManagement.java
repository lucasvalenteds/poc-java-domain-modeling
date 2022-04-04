package com.example.features;

import com.example.domain.student.Student;
import com.example.domain.student.StudentId;

public interface StudentManagement {

    // TODO: Replace String with FirstName and LastName
    Student createStudent(String firstName, String lastName);

    void removeStudent(StudentId id);

    Student findStudentById(StudentId id);
}
