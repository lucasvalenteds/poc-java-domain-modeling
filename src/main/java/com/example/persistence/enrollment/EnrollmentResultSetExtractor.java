package com.example.persistence.enrollment;

import com.example.domain.course.CourseId;
import com.example.domain.enrollment.Enrollment;
import com.example.domain.enrollment.EnrollmentId;
import com.example.domain.student.StudentId;
import com.example.persistence.course.CourseRepository;
import com.example.persistence.student.StudentRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public final class EnrollmentResultSetExtractor implements ResultSetExtractor<List<Enrollment>> {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public EnrollmentResultSetExtractor(StudentRepository studentRepository, CourseRepository courseRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public List<Enrollment> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        final var enrollments = new LinkedList<Enrollment>();

        while (resultSet.next()) {
            final var enrollmentId = new EnrollmentId(UUID.fromString(resultSet.getObject(1, String.class)));
            final var studentId = new StudentId(UUID.fromString(resultSet.getObject(2, String.class)));
            final var courseId = new CourseId(UUID.fromString(resultSet.getObject(3, String.class)));

            final var student = studentRepository.findById(studentId);
            final var course = courseRepository.findById(courseId);

            enrollments.add(new Enrollment(enrollmentId, student, course));
        }

        return enrollments;
    }
}
