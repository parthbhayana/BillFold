package com.nineleaps.expensemanagementproject.service;

import org.springframework.stereotype.Service;
import com.nineleaps.expensemanagementproject.entity.Employee;
import com.nineleaps.expensemanagementproject.entity.Reports;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@Service
public class EmailSenderServiceImpl implements IEmailSenderService {

	@Autowired
	private IReportsService reportsService;
	@Autowired
	private IEmployeeService employeeService;

	private final JavaMailSender mailSender;

	public EmailSenderServiceImpl(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	@Override
	public void sendEmail(Long employeeId) {
		List<Reports> reportList = reportsService.getReportByEmpId(employeeId);
		Reports report = reportList.get(0);
		Employee employee = employeeService.getEmployeeDetailsById(employeeId);
		SimpleMailMessage eMail = new SimpleMailMessage();
		eMail.setFrom("karthikdemoemail@gmail.com");
		eMail.setTo(report.getManagerEmail());
		eMail.setSubject("BillFold -" + employee.getFirstName() + " " + employee.getLastName());
		eMail.setText(employee.getFirstName() + " " + employee.getLastName() + " submitted you a report for approval!"
				+ "\n\n\n\n\n[NO_REPLY] This is an auto-generated email.");
		System.out.print("-----------------------------------------------------------------------------@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@+" +report.getManagerEmail()+employee.getFirstName()+employee.getFirstName() + " " + employee.getLastName() + " submitted you a report for approval!"
				+ "\n\n\n\n\n[NO_REPLY] This is an auto-generated email.");
		
		this.mailSender.send(eMail);
	}
}