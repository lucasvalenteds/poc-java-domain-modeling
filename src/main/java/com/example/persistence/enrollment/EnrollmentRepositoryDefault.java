package com.example.persistence.enrollment;

import com.example.domain.course.CourseId;
import com.example.domain.enrollment.Enrollment;
import com.example.domain.enrollment.EnrollmentId;
import com.example.domain.student.StudentId;
import com.example.infrastructure.validation.Validatable;
import com.example.persistence.course.CourseRepository;
import com.example.persistence.student.StudentRepository;
import jakarta.inject.Inject;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.List;

public class EnrollmentRepositoryDefault implements EnrollmentRepository, Validatable {

    private final JdbcTemplate jdbcTemplate;
    private final ResultSetExtractor<List<Enrollment>> enrollmentResultSetExtractor;

    @Inject
    public EnrollmentRepositoryDefault(DataSource dataSource,
                                       StudentRepository studentRepository,
                                       CourseRepository courseRepository) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.enrollmentResultSetExtractor = new EnrollmentResultSetExtractor(studentRepository, courseRepository);
    }

    @Override
    public void insert(Enrollment enrollment) throws EnrollmentPersistenceException {
        final var query = """
            INSERT INTO ENROLLMENT (ID, STUDENT_ID, COURSE_ID)
             VALUES (?,?,?)
            """;

        try {
            jdbcTemplate.update(query, preparedStatement -> {
                preparedStatement.setObject(1, enrollment.id().value(), Types.CHAR);
                preparedStatement.setObject(2, enrollment.student().id().value(), Types.CHAR);
                preparedStatement.setObject(3, enrollment.course().id().value(), Types.CHAR);
            });
        } catch (DataAccessException exception) {
            throw new EnrollmentPersistenceException("Error inserting enrollment", exception);
        }
    }

    @Override
    public void delete(List<EnrollmentId> enrollmentIds) throws EnrollmentPersistenceException {
        final var query = "DELETE FROM ENROLLMENT WHERE ID = ?";

        try {
            jdbcTemplate.batchUpdate(query, new EnrollmentBatchDeleteSetter(enrollmentIds));
        } catch (DataAccessException exception) {
            throw new EnrollmentPersistenceException("Error deleting enrollments", exception);
        }
    }

    @Override
    public boolean exists(StudentId studentId, CourseId courseId) throws EnrollmentPersistenceException {
        final var query = """
            SELECT EXISTS(
              SELECT ID
              FROM ENROLLMENT
              WHERE STUDENT_ID = ? AND COURSE_ID = ?
            )
            """;

        try {
            return Boolean.TRUE.equals(
                jdbcTemplate.queryForObject(
                    query,
                    new Object[]{studentId.value(), courseId.value()},
                    new int[]{Types.CHAR, Types.CHAR},
                    Boolean.class
                )
            );
        } catch (DataAccessException exception) {
            throw new EnrollmentPersistenceException("Error verifying enrollment exists", exception);
        }
    }

    @Override
    public List<Enrollment> findByStudentId(StudentId studentId) throws EnrollmentPersistenceException {
        final var query = """
            SELECT ID, STUDENT_ID, COURSE_ID
             FROM ENROLLMENT
             WHERE STUDENT_ID = ?
            """;

        try {
            return jdbcTemplate.query(
                query,
                new Object[]{studentId.value()},
                new int[]{Types.CHAR},
                this.enrollmentResultSetExtractor
            );
        } catch (DataAccessException exception) {
            throw new EnrollmentPersistenceException("Error finding enrollments by student", exception);
        }
    }

    @Override
    public List<Enrollment> findByCourseId(CourseId courseId) throws EnrollmentPersistenceException {
        final var query = """
            SELECT ID, STUDENT_ID, COURSE_ID
             FROM ENROLLMENT
             WHERE COURSE_ID = ?
            """;

        try {
            return jdbcTemplate.query(
                query,
                new Object[]{courseId.value()},
                new int[]{Types.CHAR},
                this.enrollmentResultSetExtractor
            );
        } catch (DataAccessException exception) {
            throw new EnrollmentPersistenceException("Error finding enrollments by course", exception);
        }
    }
}
