package com.nineleaps.expensemanagementproject.repository;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.nineleaps.expensemanagementproject.entity.Reports;


@RunWith(MockitoJUnitRunner.class)
@DataJpaTest
public class ReportsRepositoryTests {

    @Mock
    private ReportsRepository reportsRepository;

    private Reports reports;

    @Before
    public void setUp() {
        reports = new Reports();
        reports.setReportId(1L);
        Mockito.when(reportsRepository.getReportByReportId(Mockito.anyLong())).thenReturn(reports);

    }

    @Test
    public void testGetReportByReportId() {
        Reports retrievedReports = reportsRepository.getReportByReportId(1L);
        assertNotNull(retrievedReports);
        assertEquals(reports.getReportId(), retrievedReports.getReportId());

    }



}
