package com.example.persistence.enrollment;

import com.example.domain.enrollment.Enrollment;
import com.example.persistence.course.CourseRepository;
import com.example.persistence.student.StudentRepository;
import com.example.testing.CourseTestBuilder;
import com.example.testing.DataAccessExceptionStub;
import com.example.testing.EnrollmentTestBuilder;
import com.example.testing.Testing;
import com.example.testing.StudentTestBuilder;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EnrollmentRepositoryDefaultTest {

    private final StudentRepository studentRepository = Mockito.mock(StudentRepository.class);
    private final CourseRepository courseRepository = Mockito.mock(CourseRepository.class);

    private final JdbcTemplate jdbcTemplate = Mockito.mock(JdbcTemplate.class);
    private final ResultSetExtractor<List<Enrollment>> enrollmentResultSetExtractor =
        new EnrollmentResultSetExtractor(studentRepository, courseRepository);

    private final EnrollmentRepository enrollmentRepository =
        new EnrollmentRepositoryDefault(jdbcTemplate, enrollmentResultSetExtractor);

    @Test
    void handlingInsertErrorsAsPersistenceException() {
        Testing.mockInsertToFail(jdbcTemplate);

        final var enrollment = EnrollmentTestBuilder.enrollment();

        final var exception = assertThrows(
            EnrollmentPersistenceException.class,
            () -> enrollmentRepository.insert(enrollment)
        );

        assertEquals("Error inserting enrollment", exception.getMessage());
        assertEquals(DataAccessExceptionStub.MESSAGE, exception.getCause().getMessage());

        Testing.verifyInsertFailed(jdbcTemplate);
    }

    @Test
    void handlingDeleteByEnrollmentIdsErrorsAsPersistenceException() {
        Testing.mockBatchDeleteToFail(jdbcTemplate);

        final var enrollmentIds = List.of(EnrollmentTestBuilder.id());

        final var exception = assertThrows(
            EnrollmentPersistenceException.class,
            () -> enrollmentRepository.delete(enrollmentIds)
        );

        assertEquals("Error deleting enrollments", exception.getMessage());
        assertEquals(DataAccessExceptionStub.MESSAGE, exception.getCause().getMessage());

        Testing.verifyBatchDeleteFailed(jdbcTemplate);
    }

    @Test
    void handlingExistsErrorsAsPersistenceException() {
        Testing.mockSelectExistsToFail(jdbcTemplate);

        final var studentId = StudentTestBuilder.id();
        final var courseId = CourseTestBuilder.id();

        final var exception = assertThrows(
            EnrollmentPersistenceException.class,
            () -> enrollmentRepository.exists(studentId, courseId)
        );

        assertEquals("Error verifying enrollment exists", exception.getMessage());
        assertEquals(DataAccessExceptionStub.MESSAGE, exception.getCause().getMessage());

        Testing.verifySelectExistsFailed(jdbcTemplate);
    }

    @Test
    void handlingFindByStudentIdAsPersistenceException() {
        Testing.mockSelectListToFail(jdbcTemplate);

        final var studentId = StudentTestBuilder.id();

        final var exception = assertThrows(
            EnrollmentPersistenceException.class,
            () -> enrollmentRepository.findByStudentId(studentId)
        );

        assertEquals("Error finding enrollments by student", exception.getMessage());
        assertEquals(DataAccessExceptionStub.MESSAGE, exception.getCause().getMessage());

        Testing.verifySelectListFailed(jdbcTemplate);
    }

    @Test
    void handlingFindByCourseIdAsPersistenceException() {
        Testing.mockSelectListToFail(jdbcTemplate);

        final var courseId = CourseTestBuilder.id();

        final var exception = assertThrows(
            EnrollmentPersistenceException.class,
            () -> enrollmentRepository.findByCourseId(courseId)
        );

        assertEquals("Error finding enrollments by course", exception.getMessage());
        assertEquals(DataAccessExceptionStub.MESSAGE, exception.getCause().getMessage());

        Testing.verifySelectListFailed(jdbcTemplate);
    }
}