package com.example.features;

import com.example.domain.student.FirstName;
import com.example.domain.student.Student;
import com.example.domain.student.StudentId;

public interface StudentManagement {

    Student createStudent(FirstName firstName, String lastName);

    void removeStudent(StudentId id);

    Student findStudentById(StudentId id);
}
