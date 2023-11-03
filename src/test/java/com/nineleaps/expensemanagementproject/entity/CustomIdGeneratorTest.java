package com.nineleaps.expensemanagementproject.entity;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionDelegatorBaseImpl;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CustomIdGeneratorTest {
    /**
     * Method under test: {@link CustomIdGenerator#generate(SharedSessionContractImplementor, Object)}
     */
    @Test
    void testGenerate() throws SQLException, HibernateException {
        // Arrange
        CustomIdGenerator customIdGenerator = new CustomIdGenerator();
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getInt(anyInt())).thenReturn(1);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        doNothing().when(resultSet).close();
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        doNothing().when(preparedStatement).setString(anyInt(), Mockito.<String>any());
        doNothing().when(preparedStatement).close();
        Connection connection = mock(Connection.class);
        when(connection.prepareStatement(Mockito.<String>any())).thenReturn(preparedStatement);
        SessionDelegatorBaseImpl session = mock(SessionDelegatorBaseImpl.class);
        when(session.connection()).thenReturn(connection);

        // Act
        customIdGenerator.generate(session, "Object");

        // Assert
        verify(session).connection();
        verify(connection).prepareStatement(Mockito.<String>any());
        verify(preparedStatement).executeQuery();
        verify(preparedStatement).setString(anyInt(), Mockito.<String>any());
        verify(preparedStatement).close();
        verify(resultSet).next();
        verify(resultSet).getInt(anyInt());
        verify(resultSet).close();
    }

    /**
     * Method under test: {@link CustomIdGenerator#generate(SharedSessionContractImplementor, Object)}
     */
    @Test
    void testGenerate2() throws SQLException, HibernateException {
        // Arrange
        CustomIdGenerator customIdGenerator = new CustomIdGenerator();
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getInt(anyInt())).thenThrow(new SQLException());
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        doNothing().when(resultSet).close();
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        doNothing().when(preparedStatement).setString(anyInt(), Mockito.<String>any());
        doNothing().when(preparedStatement).close();
        Connection connection = mock(Connection.class);
        when(connection.prepareStatement(Mockito.<String>any())).thenReturn(preparedStatement);
        SessionDelegatorBaseImpl session = mock(SessionDelegatorBaseImpl.class);
        when(session.connection()).thenReturn(connection);

        // Act and Assert
        assertThrows(HibernateException.class, () -> customIdGenerator.generate(session, "Object"));
        verify(session).connection();
        verify(connection).prepareStatement(Mockito.<String>any());
        verify(preparedStatement).executeQuery();
        verify(preparedStatement).setString(anyInt(), Mockito.<String>any());
        verify(preparedStatement).close();
        verify(resultSet).next();
        verify(resultSet).getInt(anyInt());
        verify(resultSet).close();
    }

    /**
     * Method under test: {@link CustomIdGenerator#generate(SharedSessionContractImplementor, Object)}
     */
    @Test
    void testGenerate3() throws SQLException, HibernateException {
        // Arrange
        CustomIdGenerator customIdGenerator = new CustomIdGenerator();
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getInt(anyInt())).thenThrow(new HibernateException("An error occurred"));
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        doNothing().when(resultSet).close();
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        doNothing().when(preparedStatement).setString(anyInt(), Mockito.<String>any());
        doNothing().when(preparedStatement).close();
        Connection connection = mock(Connection.class);
        when(connection.prepareStatement(Mockito.<String>any())).thenReturn(preparedStatement);
        SessionDelegatorBaseImpl session = mock(SessionDelegatorBaseImpl.class);
        when(session.connection()).thenReturn(connection);

        // Act and Assert
        assertThrows(HibernateException.class, () -> customIdGenerator.generate(session, "Object"));
        verify(session).connection();
        verify(connection).prepareStatement(Mockito.<String>any());
        verify(preparedStatement).executeQuery();
        verify(preparedStatement).setString(anyInt(), Mockito.<String>any());
        verify(preparedStatement).close();
        verify(resultSet).next();
        verify(resultSet).getInt(anyInt());
        verify(resultSet).close();
    }

    /**
     * Method under test: {@link CustomIdGenerator#generate(SharedSessionContractImplementor, Object)}
     */
    @Test
    void testGenerate4() throws SQLException, HibernateException {
        // Arrange
        CustomIdGenerator customIdGenerator = new CustomIdGenerator();
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.next()).thenReturn(false).thenReturn(true).thenReturn(false);
        doNothing().when(resultSet).close();
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        doNothing().when(preparedStatement).setString(anyInt(), Mockito.<String>any());
        doNothing().when(preparedStatement).close();
        Connection connection = mock(Connection.class);
        when(connection.prepareStatement(Mockito.<String>any())).thenReturn(preparedStatement);
        SessionDelegatorBaseImpl session = mock(SessionDelegatorBaseImpl.class);
        when(session.connection()).thenReturn(connection);

        // Act
        customIdGenerator.generate(session, "Object");

        // Assert
        verify(session).connection();
        verify(connection).prepareStatement(Mockito.<String>any());
        verify(preparedStatement).executeQuery();
        verify(preparedStatement).setString(anyInt(), Mockito.<String>any());
        verify(preparedStatement).close();
        verify(resultSet).next();
        verify(resultSet).close();
    }
}

