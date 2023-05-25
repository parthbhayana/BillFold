package com.nineleaps.expensemanagementproject.service;

import org.springframework.stereotype.Service;
import com.nineleaps.expensemanagementproject.entity.Employee;
import com.nineleaps.expensemanagementproject.entity.Expense;
import com.nineleaps.expensemanagementproject.entity.Reports;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@Service
public class EmailServiceImpl implements IEmailService {

	@Autowired
	private IReportsService reportsService;

	@Autowired
	private IExpenseService expenseService;

	private final JavaMailSender mailSender;

	public EmailServiceImpl(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	@Override
	public void sendEmail(Long reportId) {
		Reports report = reportsService.getReportById(reportId);
		List<Expense> expenseList = expenseService.getExpenseByReportId(reportId);
		Expense expense = expenseList.get(0);
		Employee employee = expense.getEmployee();
		SimpleMailMessage eMail = new SimpleMailMessage();
		eMail.setFrom("karthikdemoemail@gmail.com");
		eMail.setTo(report.getManagerEmail());
		eMail.setSubject("BillFold - " + employee.getFirstName() + " " + employee.getLastName());
		eMail.setText(employee.getFirstName() + " " + employee.getLastName()
				+ " has submitted you a report for approval. As a designated approver, we kindly request your prompt attention to review and take necessary action on the report."
				+ "\n\nBelow are the details of the report submission:" + "\n\nReport Title: " + report.getReportTitle()
				+ "\nSubmitter's Name: " + employee.getFirstName() + " " + employee.getLastName()
				+ "\nSubmission Date: " + report.getDateSubmitted() + "\nTotal Amount: " + report.getTotalAmountINR()
				+ " INR"
				+ "\n\nPlease log in to your Billfold account to access the report and review its contents. We kindly request you to carefully evaluate the report and take appropriate action based on your assessment."
				+ "\n\nThis is an automated message. Please do not reply to this email." + "\n\nThanks!");

		this.mailSender.send(eMail);
	}
}