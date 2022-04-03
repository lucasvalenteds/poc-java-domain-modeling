package com.example.persistence.rating;

import com.example.domain.course.Rating;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class RatingRowMapper implements RowMapper<Rating> {

    @Override
    public Rating mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        return new Rating(resultSet.getInt(1));
    }
}
