package com.example.persistence.course;

import com.example.domain.course.Code;
import com.example.domain.course.Course;
import com.example.domain.course.CourseId;
import com.example.domain.course.Title;
import com.example.persistence.rating.RatingRepository;
import jakarta.inject.Inject;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public final class CourseRowMapper implements RowMapper<Course> {

    private final RatingRepository ratingRepository;

    @Inject
    public CourseRowMapper(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    @Override
    public Course mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        final var courseId = new CourseId(UUID.fromString(resultSet.getObject(1, String.class)));
        final var code = new Code(resultSet.getObject(2, String.class));
        final var title = new Title(resultSet.getObject(3, String.class));
        final var rating = ratingRepository.sum(courseId);

        return new Course(courseId, code, title, rating);
    }
}
