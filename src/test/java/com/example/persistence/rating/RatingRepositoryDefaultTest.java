package com.example.persistence.rating;

import com.example.domain.course.Rating;
import com.example.testing.CourseTestBuilder;
import com.example.testing.DataAccessExceptionStub;
import com.example.testing.StudentTestBuilder;
import com.example.testing.Testing;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RatingRepositoryDefaultTest {

    private final JdbcTemplate jdbcTemplate = Mockito.mock(JdbcTemplate.class);
    private final RowMapper<Rating> ratingRowMapper = new RatingRowMapper();
    private final RatingRepository ratingRepository = new RatingRepositoryDefault(jdbcTemplate, ratingRowMapper);

    @Test
    void handlingExistsErrorsAsPersistenceException() {
        Testing.mockSelectExistsToFail(jdbcTemplate);

        final var studentId = StudentTestBuilder.id();
        final var courseId = CourseTestBuilder.id();
        final var rating = CourseTestBuilder.rating();

        final var exception = assertThrows(
            RatingPersistenceException.class,
            () -> ratingRepository.upsert(studentId, courseId, rating)
        );

        assertEquals("Error verifying rating exists", exception.getMessage());
        assertEquals(DataAccessExceptionStub.MESSAGE, exception.getCause().getMessage());

        Testing.verifySelectExistsFailed(jdbcTemplate);
    }

    @Test
    void handlingInsertErrorsAsPersistenceException() {
        Testing.mockSelectExistsToSucceed(jdbcTemplate, false);
        Testing.mockInsertToFail(jdbcTemplate);

        final var studentId = StudentTestBuilder.id();
        final var courseId = CourseTestBuilder.id();
        final var rating = CourseTestBuilder.rating();

        final var exception = assertThrows(
            RatingPersistenceException.class,
            () -> ratingRepository.upsert(studentId, courseId, rating)
        );

        assertEquals("Error inserting rating", exception.getMessage());
        assertEquals(DataAccessExceptionStub.MESSAGE, exception.getCause().getMessage());

        Testing.verifySelectExistsSucceeded(jdbcTemplate);
        Testing.verifyInsertFailed(jdbcTemplate);
    }

    @Test
    void handlingUpdateErrorsAsPersistenceException() {
        Testing.mockSelectExistsToSucceed(jdbcTemplate, true);
        Testing.mockUpdateToFail(jdbcTemplate);

        final var studentId = StudentTestBuilder.id();
        final var courseId = CourseTestBuilder.id();
        final var rating = CourseTestBuilder.rating();

        final var exception = assertThrows(
            RatingPersistenceException.class,
            () -> ratingRepository.upsert(studentId, courseId, rating)
        );

        assertEquals("Error updating rating", exception.getMessage());
        assertEquals(DataAccessExceptionStub.MESSAGE, exception.getCause().getMessage());

        Testing.verifySelectExistsSucceeded(jdbcTemplate);
        Testing.verifyUpdateFailed(jdbcTemplate);
    }

    @Test
    void handlingDeleteByCourseIdErrorsAsPersistenceException() {
        Testing.mockDeleteToFail(jdbcTemplate);

        final var courseId = CourseTestBuilder.id();

        final var exception = assertThrows(
            RatingPersistenceException.class,
            () -> ratingRepository.delete(courseId)
        );

        assertEquals("Error deleting rating", exception.getMessage());
        assertEquals(DataAccessExceptionStub.MESSAGE, exception.getCause().getMessage());

        Testing.verifyDeleteFailed(jdbcTemplate);
    }

    @Test
    void handlingDeleteByStudentIdErrorsAsPersistenceException() {
        Testing.mockDeleteToFail(jdbcTemplate);

        final var studentId = StudentTestBuilder.id();

        final var exception = assertThrows(
            RatingPersistenceException.class,
            () -> ratingRepository.delete(studentId)
        );

        assertEquals("Error deleting rating", exception.getMessage());
        assertEquals(DataAccessExceptionStub.MESSAGE, exception.getCause().getMessage());

        Testing.verifyDeleteFailed(jdbcTemplate);
    }

    @Test
    void handlingSumErrorsAsPersistenceException() {
        Testing.mockSelectToFail(jdbcTemplate);

        final var courseId = CourseTestBuilder.id();

        final var exception = assertThrows(
            RatingPersistenceException.class,
            () -> ratingRepository.sum(courseId)
        );

        assertEquals("Error summing rating", exception.getMessage());
        assertEquals(DataAccessExceptionStub.MESSAGE, exception.getCause().getMessage());

        Testing.verifySelectFailed(jdbcTemplate);
    }
}