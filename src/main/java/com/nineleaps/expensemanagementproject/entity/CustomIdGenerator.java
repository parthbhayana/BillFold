package com.nineleaps.expensemanagementproject.entity;

import com.nineleaps.expensemanagementproject.repository.ReportsRepository;
import com.nineleaps.expensemanagementproject.service.IReportsService;
import com.nineleaps.expensemanagementproject.service.ReportsServiceImpl;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.boot.model.relational.Database;
import org.hibernate.boot.model.relational.SqlStringGenerationContext;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class CustomIdGenerator implements IdentifierGenerator {

    @Autowired
    IReportsService reportsService;

    @Autowired
    ReportsRepository reportsRepository;


    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        String yearMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
//        Long reportSerialNumber = reportsRepository.findLatestReportSerialNumber();
        Long reportSerialNumber = reportsService.getNextReportSerialNumber();
                String reportId = yearMonth + String.format("%04d", reportSerialNumber);
        return Long.parseLong(reportId);
    }

    @Override
    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
        IdentifierGenerator.super.configure(type, params, serviceRegistry);
    }

    @Override
    public void registerExportables(Database database) {
        IdentifierGenerator.super.registerExportables(database);
    }

    @Override
    public void initialize(SqlStringGenerationContext context) {
        IdentifierGenerator.super.initialize(context);
    }

    @Override
    public boolean supportsJdbcBatchInserts() {
        return IdentifierGenerator.super.supportsJdbcBatchInserts();
    }
}
