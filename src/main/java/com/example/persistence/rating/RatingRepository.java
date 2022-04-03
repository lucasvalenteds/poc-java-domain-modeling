package com.example.persistence.rating;

import com.example.domain.course.CourseId;
import com.example.domain.course.Rating;
import com.example.domain.student.StudentId;

public interface RatingRepository {

    void upsert(StudentId studentId, CourseId courseId, Rating rating) throws RatingPersistenceException;

    void delete(CourseId courseId) throws RatingPersistenceException;

    void delete(StudentId studentId) throws RatingPersistenceException;

    Rating sum(CourseId courseId) throws RatingPersistenceException;
}
