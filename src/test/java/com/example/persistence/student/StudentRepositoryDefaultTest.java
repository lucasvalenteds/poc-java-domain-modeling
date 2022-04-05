package com.example.persistence.student;

import com.example.domain.student.Student;
import com.example.testing.DataAccessExceptionStub;
import com.example.testing.Testing;
import com.example.testing.StudentTestBuilder;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import static org.junit.jupiter.api.Assertions.*;

class StudentRepositoryDefaultTest {

    private final JdbcTemplate jdbcTemplate = Mockito.mock(JdbcTemplate.class);
    private final RowMapper<Student> studentRowMapper = new StudentRowMapper();
    private final StudentRepository studentRepository = new StudentRepositoryDefault(jdbcTemplate, studentRowMapper);

    @Test
    void handlingInsertErrorsAsPersistenceException() {
        Testing.mockInsertToFail(jdbcTemplate);

        final var student = StudentTestBuilder.student();

        final var exception = assertThrows(
            StudentPersistenceException.class,
            () -> studentRepository.insert(student)
        );

        assertEquals("Error inserting student", exception.getMessage());
        assertEquals(DataAccessExceptionStub.MESSAGE, exception.getCause().getMessage());
        assertEquals(student.id(), exception.getStudentId());

        Testing.verifyInsertFailed(jdbcTemplate);
    }

    @Test
    void handlingDeleteErrorsAsPersistenceException() {
        Testing.mockDeleteToFail(jdbcTemplate);

        final var studentId = StudentTestBuilder.id();

        final var exception = assertThrows(
            StudentPersistenceException.class,
            () -> studentRepository.delete(studentId)
        );

        assertEquals("Error deleting student", exception.getMessage());
        assertEquals(DataAccessExceptionStub.MESSAGE, exception.getCause().getMessage());
        assertEquals(studentId, exception.getStudentId());

        Testing.verifyDeleteFailed(jdbcTemplate);
    }

    @Test
    void handlingSelectErrorsAsPersistenceException() {
        Testing.mockSelectToFail(jdbcTemplate);

        final var studentId = StudentTestBuilder.id();

        final var exception = assertThrows(
            StudentPersistenceException.class,
            () -> studentRepository.findById(studentId)
        );

        assertEquals("Error finding student", exception.getMessage());
        assertEquals(DataAccessExceptionStub.MESSAGE, exception.getCause().getMessage());
        assertEquals(studentId, exception.getStudentId());

        Testing.verifySelectFailed(jdbcTemplate);
    }

    @Test
    void handlingSelectEmptyResultAsResourceNotFoundException() {
        Testing.mockSelectToReturnEmpty(jdbcTemplate);

        final var studentId = StudentTestBuilder.id();

        final var exception = assertThrows(
            StudentNotFoundException.class,
            () -> studentRepository.findById(studentId)
        );

        assertEquals("Student not found by ID", exception.getMessage());
        assertEquals(EmptyResultDataAccessException.class, exception.getCause().getClass());
        assertEquals(studentId, exception.getStudentId());

        Testing.verifySelectFailed(jdbcTemplate);
    }
}