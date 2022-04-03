package com.example.features;

import com.example.domain.course.CourseId;
import com.example.domain.course.Rating;
import com.example.domain.student.StudentId;

public interface RatingManagement {

    Rating rate(StudentId studentId, CourseId courseId, Rating rating);
}
