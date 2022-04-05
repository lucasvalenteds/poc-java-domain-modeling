package com.example.testing;

import org.mockito.Mockito;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import java.util.Objects;

public final class Testing {

    private Testing() {
    }

    public static String anyInsertQuery() {
        return Mockito.argThat(string -> string.contains("INSERT INTO"));
    }

    public static String anyUpdateQuery() {
        return Mockito.argThat(string -> string.startsWith("UPDATE"));
    }

    public static String anyDeleteQuery() {
        return Mockito.argThat(string -> string.contains("DELETE FROM"));
    }

    public static String anySelectQuery() {
        return Mockito.argThat(string -> string.startsWith("SELECT") && string.contains("FROM"));
    }

    public static String anySelectExistsQuery() {
        return Mockito.argThat(string -> string.startsWith("SELECT EXISTS"));
    }

    private static <T> RowMapper<T> anyRowMapper() {
        return Mockito.argThat(Objects::nonNull);
    }

    private static <T> ResultSetExtractor<T> anyResultSetExtractor() {
        return Mockito.argThat(Objects::nonNull);
    }

    public static void mockInsertToFail(JdbcTemplate jdbcTemplate) {
        Mockito.when(jdbcTemplate.update(
                Testing.anyInsertQuery(),
                Mockito.any(PreparedStatementSetter.class)
            ))
            .thenThrow(new DataAccessExceptionStub());
    }

    public static void mockUpdateToFail(JdbcTemplate jdbcTemplate) {
        Mockito.when(jdbcTemplate.update(
                Testing.anyUpdateQuery(),
                Mockito.any(PreparedStatementSetter.class)
            ))
            .thenThrow(new DataAccessExceptionStub());
    }

    public static void mockDeleteToFail(JdbcTemplate jdbcTemplate) {
        Mockito.when(jdbcTemplate.update(
                Testing.anyDeleteQuery(),
                Mockito.any(PreparedStatementSetter.class)
            ))
            .thenThrow(new DataAccessExceptionStub());
    }

    public static void mockBatchDeleteToFail(JdbcTemplate jdbcTemplate) {
        Mockito.when(jdbcTemplate.batchUpdate(
                Testing.anyDeleteQuery(),
                Mockito.any(BatchPreparedStatementSetter.class)
            ))
            .thenThrow(new DataAccessExceptionStub());
    }

    public static void mockSelectToFail(JdbcTemplate jdbcTemplate) {
        Mockito.when(jdbcTemplate.queryForObject(
                Testing.anySelectQuery(),
                Mockito.any(Object[].class),
                Mockito.any(int[].class),
                Testing.anyRowMapper()
            ))
            .thenThrow(new DataAccessExceptionStub());
    }

    public static void mockSelectListToFail(JdbcTemplate jdbcTemplate) {
        Mockito.when(jdbcTemplate.query(
                Testing.anySelectQuery(),
                Mockito.any(Object[].class),
                Mockito.any(int[].class),
                Testing.anyResultSetExtractor()
            ))
            .thenThrow(new DataAccessExceptionStub());
    }

    public static void mockSelectExistsToFail(JdbcTemplate jdbcTemplate) {
        Mockito.when(jdbcTemplate.queryForObject(
                Testing.anySelectExistsQuery(),
                Mockito.any(Object[].class),
                Mockito.any(int[].class),
                Mockito.eq(Boolean.class)
            ))
            .thenThrow(new DataAccessExceptionStub());
    }

    public static void mockSelectExistsToSucceed(JdbcTemplate jdbcTemplate, boolean exists) {
        Mockito.when(jdbcTemplate.queryForObject(
                Testing.anySelectExistsQuery(),
                Mockito.any(Object[].class),
                Mockito.any(int[].class),
                Mockito.eq(Boolean.class)
            ))
            .thenReturn(exists);
    }

    public static void mockSelectToReturnEmpty(JdbcTemplate jdbcTemplate) {
        Mockito.when(jdbcTemplate.queryForObject(
                Testing.anySelectQuery(),
                Mockito.any(Object[].class),
                Mockito.any(int[].class),
                Testing.anyRowMapper()
            ))
            .thenThrow(new EmptyResultDataAccessException(1));
    }

    public static void verifyInsertFailed(JdbcTemplate jdbcTemplate) {
        Mockito.verify(jdbcTemplate, Mockito.times(1))
            .update(Testing.anyInsertQuery(), Mockito.any(PreparedStatementSetter.class));
    }

    public static void verifyUpdateFailed(JdbcTemplate jdbcTemplate) {
        Mockito.verify(jdbcTemplate, Mockito.times(1))
            .update(Testing.anyUpdateQuery(), Mockito.any(PreparedStatementSetter.class));
    }

    public static void verifyDeleteFailed(JdbcTemplate jdbcTemplate) {
        Mockito.verify(jdbcTemplate, Mockito.times(1))
            .update(Testing.anyDeleteQuery(), Mockito.any(PreparedStatementSetter.class));
    }

    public static void verifyBatchDeleteFailed(JdbcTemplate jdbcTemplate) {
        Mockito.verify(jdbcTemplate, Mockito.times(1))
            .batchUpdate(
                Testing.anyDeleteQuery(),
                Mockito.any(BatchPreparedStatementSetter.class)
            );
    }

    public static void verifySelectFailed(JdbcTemplate jdbcTemplate) {
        Mockito.verify(jdbcTemplate, Mockito.times(1))
            .queryForObject(
                Testing.anySelectQuery(),
                Mockito.any(Object[].class),
                Mockito.any(int[].class),
                Testing.anyRowMapper()
            );
    }

    public static void verifySelectListFailed(JdbcTemplate jdbcTemplate) {
        Mockito.verify(jdbcTemplate, Mockito.times(1))
            .query(
                Testing.anySelectQuery(),
                Mockito.any(Object[].class),
                Mockito.any(int[].class),
                Testing.anyResultSetExtractor()
            );
    }

    private static void verifySelectExistsCalled(JdbcTemplate jdbcTemplate) {
        Mockito.verify(jdbcTemplate, Mockito.times(1))
            .queryForObject(
                Testing.anySelectExistsQuery(),
                Mockito.any(Object[].class),
                Mockito.any(int[].class),
                Mockito.eq(Boolean.class)
            );
    }

    public static void verifySelectExistsFailed(JdbcTemplate jdbcTemplate) {
        Testing.verifySelectExistsCalled(jdbcTemplate);
    }

    public static void verifySelectExistsSucceeded(JdbcTemplate jdbcTemplate) {
        Testing.verifySelectExistsCalled(jdbcTemplate);
    }
}
