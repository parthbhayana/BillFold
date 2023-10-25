package com.nineleaps.expense_management_project.entity;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

public class CustomIdGenerator implements IdentifierGenerator {


    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;

        String formattedYear = String.format("%04d", year);
        String formattedMonth = String.format("%02d", month);

        int serialNumber = getSerialNumberForYearAndMonth(session.connection(), formattedYear + formattedMonth);

        String formattedSerialNumber = String.format("%04d", serialNumber);

        return Long.parseLong(formattedYear + formattedMonth + formattedSerialNumber);
    }

    int getSerialNumberForYearAndMonth(Connection connection, String yearAndMonth) throws HibernateException {
        String query = "SELECT COUNT(*) FROM reports WHERE report_id LIKE ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, yearAndMonth + "%");
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) + 1;
                }
            }
        } catch (SQLException e) {
            throw new HibernateException("Could not generate serial number", e);
        }
        return 1;
    }

}