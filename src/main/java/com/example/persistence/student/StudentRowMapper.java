package com.example.persistence.student;

import com.example.domain.student.FirstName;
import com.example.domain.student.LastName;
import com.example.domain.student.Student;
import com.example.domain.student.StudentId;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public final class StudentRowMapper implements RowMapper<Student> {

    @Override
    public Student mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        final var studentId = UUID.fromString(resultSet.getObject(1, String.class));
        final var firstName = resultSet.getObject(2, String.class);
        final var lastName = resultSet.getObject(3, String.class);

        return new Student(
            new StudentId(studentId),
            new FirstName(firstName),
            Optional.ofNullable(lastName)
                .map(LastName::new)
        );
    }
}
