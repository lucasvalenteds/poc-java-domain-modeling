package com.example.persistence.enrollment;

import com.example.domain.enrollment.EnrollmentId;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

public final class EnrollmentBatchDeleteSetter implements BatchPreparedStatementSetter {

    private final List<EnrollmentId> enrollmentIds;

    public EnrollmentBatchDeleteSetter(List<EnrollmentId> enrollmentIds) {
        this.enrollmentIds = enrollmentIds;
    }

    @Override
    public void setValues(PreparedStatement preparedStatement, int index) throws SQLException {
        preparedStatement.setObject(1, enrollmentIds.get(index).value(), Types.CHAR);
    }

    @Override
    public int getBatchSize() {
        return enrollmentIds.size();
    }
}
