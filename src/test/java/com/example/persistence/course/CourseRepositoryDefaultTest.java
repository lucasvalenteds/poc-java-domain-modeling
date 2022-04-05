package com.example.persistence.course;

import com.example.domain.course.Course;
import com.example.persistence.rating.RatingRepository;
import com.example.testing.CourseTestBuilder;
import com.example.testing.DataAccessExceptionStub;
import com.example.testing.Testing;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CourseRepositoryDefaultTest {

    private final JdbcTemplate jdbcTemplate = Mockito.mock(JdbcTemplate.class);
    private final RowMapper<Course> courseRowMapper = new CourseRowMapper(Mockito.mock(RatingRepository.class));
    private final CourseRepository courseRepository = new CourseRepositoryDefault(jdbcTemplate, courseRowMapper);

    @Test
    void handlingInsertErrorsAsPersistenceException() {
        Testing.mockInsertToFail(jdbcTemplate);

        final var course = CourseTestBuilder.course();

        final var exception = assertThrows(
            CoursePersistenceException.class,
            () -> courseRepository.insert(course)
        );

        assertEquals("Error inserting course", exception.getMessage());
        assertEquals(DataAccessExceptionStub.MESSAGE, exception.getCause().getMessage());
        assertEquals(course.id(), exception.getCourseId());

        Testing.verifyInsertFailed(jdbcTemplate);
    }

    @Test
    void handlingDeleteErrorsAsPersistenceException() {
        Testing.mockDeleteToFail(jdbcTemplate);

        final var courseId = CourseTestBuilder.id();

        final var exception = assertThrows(
            CoursePersistenceException.class,
            () -> courseRepository.delete(courseId)
        );

        assertEquals("Error deleting course", exception.getMessage());
        assertEquals(DataAccessExceptionStub.MESSAGE, exception.getCause().getMessage());
        assertEquals(courseId, exception.getCourseId());

        Testing.verifyDeleteFailed(jdbcTemplate);
    }

    @Test
    void handlingSelectErrorsAsPersistenceException() {
        Testing.mockSelectToFail(jdbcTemplate);

        final var courseId = CourseTestBuilder.id();

        final var exception = assertThrows(
            CoursePersistenceException.class,
            () -> courseRepository.findById(courseId)
        );

        assertEquals("Error finding course", exception.getMessage());
        assertEquals(DataAccessExceptionStub.MESSAGE, exception.getCause().getMessage());
        assertEquals(courseId, exception.getCourseId());

        Testing.verifySelectFailed(jdbcTemplate);
    }

    @Test
    void handlingSelectEmptyResultAsResourceNotFoundException() {
        Testing.mockSelectToReturnEmpty(jdbcTemplate);

        final var courseId = CourseTestBuilder.id();

        final var exception = assertThrows(
            CourseNotFoundException.class,
            () -> courseRepository.findById(courseId)
        );

        assertEquals("Course not found by ID", exception.getMessage());
        assertEquals(EmptyResultDataAccessException.class, exception.getCause().getClass());
        assertEquals(courseId, exception.getCourseId());

        Testing.verifySelectFailed(jdbcTemplate);
    }
}