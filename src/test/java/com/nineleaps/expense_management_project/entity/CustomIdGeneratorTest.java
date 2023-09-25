package com.nineleaps.expense_management_project.entity;


import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CustomIdGeneratorTest {

    @Mock
    private SharedSessionContractImplementor session;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    private CustomIdGenerator customIdGenerator;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        customIdGenerator = new CustomIdGenerator();
    }

    @Test
    public void testGenerate() throws HibernateException, SQLException {
        // Arrange
        when(session.connection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(3);

        // Act
        Serializable generatedId = customIdGenerator.generate(session, null);

        // Assert
        assertEquals(2023090004L, generatedId);
    }

    @Test
    public void testGenerateFirstIdForMonth() throws HibernateException, SQLException {
        // Arrange
        when(session.connection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false); // No existing records for the month

        // Act
        Serializable generatedId = customIdGenerator.generate(session, null);

        // Assert
        assertEquals(2023090001L, generatedId); // Updated expected value
    }


    @Test
    public void testGenerateSQLException() throws HibernateException, SQLException {
        // Arrange
        when(session.connection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenThrow(new SQLException("Test SQL Exception"));

        // Act and Assert
        HibernateException exception = org.junit.jupiter.api.Assertions.assertThrows(
                HibernateException.class,
                () -> customIdGenerator.generate(session, null)
        );

        assertEquals("Could not generate serial number", exception.getMessage());
        assertEquals("Test SQL Exception", exception.getCause().getMessage());
    }
}

