package com.example.persistence.course;

import com.example.domain.course.Course;
import com.example.domain.course.CourseId;

public interface CourseRepository {

    void insert(Course course) throws CoursePersistenceException;

    void delete(CourseId courseId) throws CoursePersistenceException;

    Course findById(CourseId courseId) throws CourseNotFoundException, CoursePersistenceException;
}
