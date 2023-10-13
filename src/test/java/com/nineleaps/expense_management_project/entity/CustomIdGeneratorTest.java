package com.nineleaps.expense_management_project.entity;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomIdGeneratorTest {

    private CustomIdGenerator customIdGenerator;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @BeforeEach
    void setUp() throws SQLException {
        customIdGenerator = new CustomIdGenerator();
        connection = mock(Connection.class);
        preparedStatement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    }

    @Test
    void testGenerate() throws SQLException {
        SharedSessionContractImplementor session = mock(SharedSessionContractImplementor.class);
        when(session.connection()).thenReturn(connection);

        when(resultSet.next()).thenReturn(false);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        Serializable generatedId = customIdGenerator.generate(session, null);

        assertEquals(Long.class, generatedId.getClass());
    }

    @Test
    void testGenerateWithExistingReports() throws SQLException {
        SharedSessionContractImplementor session = mock(SharedSessionContractImplementor.class);
        when(session.connection()).thenReturn(connection);

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(3); // Assuming there are 3 reports with the same year and month
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        Serializable generatedId = customIdGenerator.generate(session, null);

        assertEquals(Long.class, generatedId.getClass());


        assertEquals("2023100004", generatedId.toString());
    }

    @Test
    void testGetSerialNumberForYearAndMonth() throws SQLException {
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(2);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        int serialNumber = customIdGenerator.getSerialNumberForYearAndMonth(connection, "YYYYMM");

        assertEquals(3, serialNumber);
    }

    @Test
    void testGetSerialNumberForYearAndMonthNoExistingReports() throws SQLException {
        when(resultSet.next()).thenReturn(false);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        int serialNumber = customIdGenerator.getSerialNumberForYearAndMonth(connection, "YYYYMM");

        assertEquals(1, serialNumber);
    }

    @Test
    void testGetSerialNumberForYearAndMonthSQLException() throws SQLException {
        when(preparedStatement.executeQuery()).thenThrow(new SQLException("SQL Error"));

        // Ensure that a HibernateException is thrown in case of an SQL error
        assertThrows(HibernateException.class, () -> {
            customIdGenerator.getSerialNumberForYearAndMonth(connection, "YYYYMM");
        });
    }


}
