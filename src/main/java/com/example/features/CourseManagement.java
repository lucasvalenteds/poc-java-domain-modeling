package com.example.features;

import com.example.domain.course.Code;
import com.example.domain.course.Course;
import com.example.domain.course.CourseId;
import com.example.domain.course.Title;

public interface CourseManagement {

    Course createCourse(Code code, Title title);

    void removeCourse(CourseId courseId);

    Course findCourseById(CourseId courseId);
}
