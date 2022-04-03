package com.example.persistence.student;

import com.example.domain.student.Student;
import com.example.domain.student.StudentId;

public interface StudentRepository {

    void insert(Student student) throws StudentPersistenceException;

    void delete(StudentId studentId) throws StudentPersistenceException;

    Student findById(StudentId studentId) throws StudentNotFoundException, StudentPersistenceException;
}
