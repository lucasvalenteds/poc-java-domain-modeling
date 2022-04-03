package com.example.persistence.course;

import com.example.domain.course.Course;
import com.example.domain.course.CourseId;
import com.example.persistence.rating.RatingRepository;
import jakarta.inject.Inject;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.Types;

public final class CourseRepositoryDefault implements CourseRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Course> courseRowMapper;

    @Inject
    public CourseRepositoryDefault(DataSource dataSource, RatingRepository ratingRepository) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.courseRowMapper = new CourseRowMapper(ratingRepository);
    }

    @Override
    public void insert(Course course) throws CoursePersistenceException {
        final var query = """
            INSERT INTO COURSE (ID, CODE, TITLE)
             VALUES (?,?,?)
            """;

        try {
            jdbcTemplate.update(query, preparedStatement -> {
                preparedStatement.setObject(1, course.id().value(), Types.CHAR);
                preparedStatement.setObject(2, course.code().value(), Types.CHAR);
                preparedStatement.setObject(3, course.title().value(), Types.CHAR);
            });
        } catch (DataAccessException exception) {
            throw new CoursePersistenceException(course.id(), "Error inserting course", exception);
        }
    }

    @Override
    public void delete(CourseId courseId) throws CoursePersistenceException {
        final var query = "DELETE FROM COURSE WHERE ID = ?";

        try {
            jdbcTemplate.update(
                query,
                preparedStatement -> preparedStatement.setObject(1, courseId.value(), Types.CHAR)
            );
        } catch (DataAccessException exception) {
            throw new CoursePersistenceException(courseId, "Error deleting course", exception);
        }
    }

    @Override
    public Course findById(CourseId courseId) throws CourseNotFoundException, CoursePersistenceException {
        final var query = """
            SELECT ID, CODE, TITLE
             FROM COURSE
             WHERE ID = ?
            """;

        try {
            return jdbcTemplate.queryForObject(
                query,
                new Object[]{courseId.value()},
                new int[]{Types.CHAR},
                this.courseRowMapper
            );
        } catch (EmptyResultDataAccessException exception) {
            throw new CourseNotFoundException(courseId, exception);
        } catch (DataAccessException exception) {
            throw new CoursePersistenceException(courseId, "Error finding couse", exception);
        }
    }
}
