package com.example.persistence.student;

import com.example.domain.student.LastName;
import com.example.domain.student.Student;
import com.example.domain.student.StudentId;
import com.example.infrastructure.validation.Validatable;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Types;

public class StudentRepositoryDefault implements StudentRepository, Validatable {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Student> studentRowMapper;

    @Inject
    public StudentRepositoryDefault(JdbcTemplate jdbcTemplate, RowMapper<Student> studentRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.studentRowMapper = studentRowMapper;
    }

    @Override
    public void insert(@Valid Student student) throws StudentPersistenceException {
        final var query = """
            INSERT INTO STUDENT (ID, FIRST_NAME, LAST_NAME)
             VALUES (?,?,?)
            """;

        try {
            jdbcTemplate.update(query, preparedStatement -> {
                preparedStatement.setObject(1, student.id().value(), Types.CHAR);
                preparedStatement.setObject(2, student.firstName().value(), Types.CHAR);
                preparedStatement.setObject(3, student.lastName().map(LastName::value).orElseGet(() -> null), Types.CHAR);
            });
        } catch (DataAccessException exception) {
            throw new StudentPersistenceException(student.id(), "Error inserting student", exception);
        }
    }

    @Override
    public void delete(@Valid StudentId studentId) throws StudentPersistenceException {
        final var query = "DELETE FROM STUDENT WHERE ID = ?";

        try {
            jdbcTemplate.update(
                query,
                preparedStatement -> preparedStatement.setObject(1, studentId.value(), Types.CHAR)
            );
        } catch (DataAccessException exception) {
            throw new StudentPersistenceException(studentId, "Error deleting student", exception);
        }
    }

    @Override
    public Student findById(@Valid StudentId studentId) throws StudentNotFoundException, StudentPersistenceException {
        final var query = """
            SELECT ID, FIRST_NAME, LAST_NAME
             FROM STUDENT
             WHERE ID = ?
            """;

        try {
            return jdbcTemplate.queryForObject(
                query,
                new Object[]{studentId.value()},
                new int[]{Types.CHAR},
                this.studentRowMapper
            );
        } catch (EmptyResultDataAccessException exception) {
            throw new StudentNotFoundException(studentId, exception);
        } catch (DataAccessException exception) {
            throw new StudentPersistenceException(studentId, "Error finding customer", exception);
        }
    }
}
