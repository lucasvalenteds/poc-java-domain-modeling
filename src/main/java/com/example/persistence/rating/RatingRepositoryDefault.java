package com.example.persistence.rating;

import com.example.domain.course.CourseId;
import com.example.domain.course.Rating;
import com.example.domain.student.StudentId;
import com.example.infrastructure.validation.Validatable;
import jakarta.inject.Inject;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Types;

public class RatingRepositoryDefault implements RatingRepository, Validatable {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Rating> ratingRowMapper;

    @Inject
    public RatingRepositoryDefault(JdbcTemplate jdbcTemplate, RowMapper<Rating> ratingRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.ratingRowMapper = ratingRowMapper;
    }

    @Override
    public void upsert(StudentId studentId, CourseId courseId, Rating rating) throws RatingPersistenceException {
        if (exists(studentId, courseId)) {
            update(studentId, courseId, rating);
        } else {
            insert(studentId, courseId, rating);
        }
    }

    private boolean exists(StudentId studentId, CourseId courseId) throws RatingPersistenceException {
        final var query = """
            SELECT EXISTS(
              SELECT RATING
              FROM RATING
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
            throw new RatingPersistenceException("Error verifying rating exists", exception);
        }
    }

    private void insert(StudentId studentId, CourseId courseId, Rating rating) throws RatingPersistenceException {
        final var query = """
            INSERT INTO RATING (STUDENT_ID, COURSE_ID, RATING)
             VALUES (?,?,?)
            """;

        try {
            jdbcTemplate.update(query, preparedStatement -> {
                preparedStatement.setObject(1, studentId.value(), Types.CHAR);
                preparedStatement.setObject(2, courseId.value(), Types.CHAR);
                preparedStatement.setObject(3, rating.value(), Types.SMALLINT);
            });
        } catch (DataAccessException exception) {
            throw new RatingPersistenceException("Error inserting rating", exception);
        }
    }

    private void update(StudentId studentId, CourseId courseId, Rating rating) throws RatingPersistenceException {
        final var query = """
            UPDATE RATING SET RATING = ?
             WHERE STUDENT_ID = ? AND COURSE_ID = ?
            """;

        try {
            jdbcTemplate.update(query, preparedStatement -> {
                preparedStatement.setObject(1, rating.value(), Types.SMALLINT);
                preparedStatement.setObject(2, studentId.value(), Types.CHAR);
                preparedStatement.setObject(3, courseId.value(), Types.CHAR);
            });
        } catch (DataAccessException exception) {
            throw new RatingPersistenceException("Error updating rating", exception);
        }
    }

    @Override
    public void delete(CourseId courseId) throws RatingPersistenceException {
        final var query = "DELETE FROM RATING WHERE COURSE_ID = ?";

        try {
            jdbcTemplate.update(query, preparedStatement ->
                preparedStatement.setObject(1, courseId.value(), Types.CHAR)
            );
        } catch (DataAccessException exception) {
            throw new RatingPersistenceException("Error deleting rating", exception);
        }
    }

    @Override
    public void delete(StudentId studentId) throws RatingPersistenceException {
        final var query = "DELETE FROM RATING WHERE STUDENT_ID = ?";

        try {
            jdbcTemplate.update(query, preparedStatement ->
                preparedStatement.setObject(1, studentId.value(), Types.CHAR)
            );
        } catch (DataAccessException exception) {
            throw new RatingPersistenceException("Error deleting rating", exception);
        }
    }

    @Override
    public Rating sum(CourseId courseId) throws RatingPersistenceException {
        final var query = "SELECT SUM(RATING) FROM RATING WHERE COURSE_ID = ?";

        try {
            return jdbcTemplate.queryForObject(
                query,
                new Object[]{courseId.value()},
                new int[]{Types.CHAR},
                this.ratingRowMapper
            );
        } catch (DataAccessException exception) {
            throw new RatingPersistenceException("Error summing rating", exception);
        }
    }
}
