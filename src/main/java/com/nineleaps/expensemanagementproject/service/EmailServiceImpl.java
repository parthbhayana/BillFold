package com.nineleaps.expensemanagementproject.service;

import com.nineleaps.expensemanagementproject.repository.ExpenseRepository;
import com.nineleaps.expensemanagementproject.repository.ReportsRepository;
import org.springframework.stereotype.Service;
import com.nineleaps.expensemanagementproject.entity.Employee;
import com.nineleaps.expensemanagementproject.entity.Expense;
import com.nineleaps.expensemanagementproject.entity.Reports;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

@Service
public class EmailServiceImpl implements IEmailService {

    @Autowired
    private IReportsService reportsService;
    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private IExpenseService expenseService;
    @Autowired
    private IEmployeeService employeeService;
    @Autowired
    private ReportsRepository reportsRepository;
    @Autowired
    private IPdfGeneratorService pdfGeneratorService;

    private final JavaMailSender javaMailSender;


    public EmailServiceImpl(JavaMailSender mailSender) {
        this.javaMailSender = mailSender;
    }

    @Override
    public void managerNotification(Long reportId, List<Long> expenseIds, HttpServletResponse response) throws IOException, MessagingException {
        Reports report = reportsService.getReportById(reportId);
        List<Expense> expenseList = expenseService.getExpenseByReportId(reportId);

        if (!expenseList.isEmpty()) {
            Expense expense = expenseList.get(0);
            Employee employee = expense.getEmployee();
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper eMail = new MimeMessageHelper(message, true);

            eMail.setFrom("billfold.noreply@gmail.com");
            eMail.setTo(employee.getManagerEmail());
            eMail.setSubject("BillFold - " + employee.getFirstName() + " " + employee.getLastName());
            eMail.setText(employee.getFirstName() + " " + employee.getLastName()
                    + " has submitted you a report for approval. As a designated approver, we kindly request your prompt attention to review and take necessary action on the report."
                    + "\n\nBelow are the details of the report submission:" + "\n\nReport Title: "
                    + report.getReportTitle() + "\nSubmitter's Name: " + employee.getFirstName() + " "
                    + employee.getLastName() + "\nSubmission Date: " + report.getDateSubmitted() + "\nTotal Amount: "
                    + report.getTotalAmountINR() + " INR"
                    + "\n\nPlease log in to your Billfold account to access the report and review its contents. We kindly request you to carefully evaluate the report and take appropriate action based on your assessment."
                    + "\n\nThis is an automated message. Please do not reply to this email." + "\n\nThanks!");

            byte[] fileData = pdfGeneratorService.export(reportId, expenseIds, response);
            ByteArrayResource resource = new ByteArrayResource(fileData);
            eMail.addAttachment("Document.pdf", resource);
            javaMailSender.send(message);
        } else {
            throw new IllegalStateException("No expenses are added to the report " + report.getReportTitle());
        }
    }

    @Override
    public void userRejectedNotification(Long reportId, List<Long> expenseIds, HttpServletResponse response) throws IOException,MessagingException{
        Reports report = reportsService.getReportById(reportId);
        List<Expense> expenseList = expenseService.getExpenseByReportId(reportId);

        if (!expenseList.isEmpty()) {
            Expense expense = expenseList.get(0);
            Employee employee = expense.getEmployee();
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper eMail = new MimeMessageHelper(message, true);
            eMail.setFrom("billfold.noreply@gmail.com");
            eMail.setTo(employee.getManagerEmail());
            eMail.setSubject("[REJECTED] Expense Report: " + report.getReportTitle());
            eMail.setText("Dear " + employee.getFirstName() + " " + employee.getLastName() + ","
                    + "\n\nWe regret to inform you that your expense report has been rejected by your manager. The details of the rejection are as follows:"
                    + "\n\nReport Title: " + report.getReportTitle() + "\nRejection Reason: "
                    + report.getManagerComments() + "\nRejection Date: " + report.getManagerActionDate()
                    + "\n\nPlease review the feedback provided by your manager and make the necessary revisions to your expense report. Once you have made the required changes, resubmit the report for further processing."
                    + "\n\nIf you have any questions or need clarification regarding the rejection, please reach out to your manager or the HR department."
                    + "\n\nThank you for your understanding and cooperation." + "\n\nBest Regards," + "\nBillFold"
                    + "\n\nThis is an automated message. Please do not reply to this email.");
            byte[] fileData = pdfGeneratorService.export(reportId, expenseIds, response);
            ByteArrayResource resource = new ByteArrayResource(fileData);
            eMail.addAttachment("Document.pdf", resource);
            this.javaMailSender.send(message);
        } else {
            throw new IllegalStateException("No expenses are added to the report " + report.getReportTitle());
        }
    }

    @Override
    public void userApprovedNotification(Long reportId, List<Long> expenseIds, HttpServletResponse response) throws IOException,MessagingException {
        Reports report = reportsService.getReportById(reportId);
        List<Expense> expenseList = expenseService.getExpenseByReportId(reportId);
        if (!expenseList.isEmpty()) {
            Expense expense = expenseList.get(0);
            Employee employee = expense.getEmployee();
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper eMail = new MimeMessageHelper(message, true);
            eMail.setFrom("billfold.noreply@gmail.com");
            eMail.setTo(employee.getEmployeeEmail());
            eMail.setSubject("[APPROVED] Expense Report: " + report.getReportTitle());
            eMail.setText("Dear " + employee.getFirstName() + " " + employee.getLastName() + ","
                    + "\n\nCongratulations! Your expense report has been approved by your manager. The details of the approval are as follows:"
                    + "\n\nReport Title: " + report.getReportTitle() + "\nComments: " + report.getManagerComments() + "\nApproval Date: "
                    + report.getManagerActionDate()
                    + "\n\nIf you have any questions or need further assistance, please feel free to contact your manager or the HR department."
                    + "\n\nThank you for your cooperation and timely submission of the expense report."
                    + "\n\nBest Regards," + "\nBillFold"
                    + "\n\nThis is an automated message. Please do not reply to this email.");

            byte[] fileData = pdfGeneratorService.export(reportId, expenseIds, response);
            ByteArrayResource resource = new ByteArrayResource(fileData);
            eMail.addAttachment("Document.pdf", resource);
            this.javaMailSender.send(message);
        } else {
            throw new IllegalStateException("No expenses are added to the report " + report.getReportTitle());
        }
    }

    @Override
    public void financeReimbursedNotification(Long reportId) {
        Reports report = reportsService.getReportById(reportId);
        List<Expense> expenseList = expenseService.getExpenseByReportId(reportId);
        if (!expenseList.isEmpty()) {
            Expense expense = expenseList.get(0);
            Employee employee = expense.getEmployee();
            SimpleMailMessage eMail = new SimpleMailMessage();
            eMail.setFrom("billfold.noreply@gmail.com");
            eMail.setTo(employee.getEmployeeEmail());
            eMail.setSubject("[REIMBURSED] Expense Report: " + report.getReportTitle());
            eMail.setText("Dear " + employee.getFirstName() + " " + employee.getLastName() + ","
                    + "\n\nCongratulations! Your expense report has been reimbursed by the finance department. The details of the reimbursement are as follows:"
                    + "\n\nReport Title: " + report.getReportTitle() + "\n\nAmount Reimbursed: "
                    + report.getTotalAmountCurrency() + report.getCurrency() + "\nReimbursement Date: "
                    + report.getFinanceActionDate()
                    + "\n\nPlease check your bank account for the credited amount. If you have any questions or concerns regarding the reimbursement, please reach out to the finance department."
                    + "\n\nThank you for your cooperation and timely submission of the expense report."
                    + "\n\nBest Regards," + "\nBillFold"
                    + "\n\nThis is an automated message. Please do not reply to this email.");

            this.javaMailSender.send(eMail);
        } else {
            throw new IllegalStateException("No expenses are added to the report " + report.getReportTitle());
        }
    }

    @Override
    public void financeRejectedNotification(Long reportId) {
        Reports report = reportsService.getReportById(reportId);
        List<Expense> expenseList = expenseService.getExpenseByReportId(reportId);

        if (!expenseList.isEmpty()) {
            Expense expense = expenseList.get(0);
            Employee employee = expense.getEmployee();
            SimpleMailMessage email = new SimpleMailMessage();
            email.setFrom("billfold.noreply@gmail.com");
            email.setTo(employee.getEmployeeEmail());
            email.setSubject("[REJECTED] Expense Report: " + report.getReportTitle());
            email.setText("Dear " + employee.getFirstName() + " " + employee.getLastName() + ","
                    + "\n\nWe regret to inform you that your expense report has been rejected by the finance department. The details of the rejection are as follows:"
                    + "\n\nReport Title: " + report.getReportTitle() + "\n\nRejection Reason: "
                    + report.getFinanceComments() + "\nRejection Date: " + report.getFinanceActionDate()
                    + "\n\nPlease review the feedback provided by the finance department and make the necessary revisions to your expense report. Once you have made the required changes, resubmit the report for further processing."
                    + "\n\nIf you have any questions or need clarification regarding the rejection, please reach out to the finance department or your manager."
                    + "\n\nThank you for your understanding and cooperation." + "\n\nBest Regards," + "\nBillFold"
                    + "\n\nThis is an automated message. Please do not reply to this email.");

            this.javaMailSender.send(email);
        } else {
            throw new IllegalStateException("No expenses are added to the report " + report.getReportTitle());
        }
    }
    @Override
    public void financeNotificationToReimburse(Long reportId, List<Long> expenseIds, HttpServletResponse response) throws IOException, MessagingException {
        Reports report = reportsService.getReportById(reportId);
        List<Expense> expenseList = expenseService.getExpenseByReportId(reportId);

        if (!expenseList.isEmpty()) {
            Expense expense = expenseList.get(0);
            Employee employee = expense.getEmployee();
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper eMail = new MimeMessageHelper(message, true);
            eMail.setFrom("billfold.noreply@gmail.com");
            eMail.setTo(employee.getFirstName()+" "+employee.getLastName());
            eMail.setSubject("Expense Reimbursement Request: " + report.getReportTitle());
            eMail.setText("Dear " + employee.getFirstName() + " " + employee.getLastName() + ","
                    + "\n\nYou have received a request for expense reimbursement from employee " + employee.getFirstName() + " " + employee.getLastName() + ". The details of the expense report are as follows:"
                    + "\n\nReport Title: " + report.getReportTitle() + "\nEmployee: " + employee.getFirstName() + " " + employee.getLastName()
                    + "\nTotal Amount: " + report.getTotalAmountINR() + " " + report.getCurrency()
                    + "\n\nPlease review the attached expense report and process the reimbursement accordingly. If there are any additional details required or if you have any questions, please reach out to the employee or the HR department."
                    + "\n\nThank you for your attention to this matter."
                    + "\n\nBest Regards," + "\nBillFold"
                    + "\n\nThis is an automated message. Please do not reply to this email.");

            byte[] fileData = pdfGeneratorService.export(reportId, expenseIds, response);
            ByteArrayResource resource = new ByteArrayResource(fileData);
            eMail.addAttachment("Document.pdf", resource);
            this.javaMailSender.send(message);
        } else {
            throw new IllegalStateException("No expenses are added to the report " + report.getReportTitle());

        }
    }

    @Override
    public void userPartialApprovedExpensesNotification(Long reportId) {
        Reports report = reportsService.getReportById(reportId);
        List<Expense> expenseList = expenseService.getExpenseByReportId(reportId);

        if (!expenseList.isEmpty()) {
            Expense expense = expenseList.get(0);
            Employee employee = expense.getEmployee();
            SimpleMailMessage eMail = new SimpleMailMessage();
            eMail.setFrom("billfoldjsr@gmail.com");
            eMail.setTo(employee.getEmployeeEmail());
            eMail.setSubject("[PARTIAL APPROVAL] Expense Report: " + report.getReportTitle());
            eMail.setText("Dear " + employee.getFirstName() + " " + employee.getLastName() + ","
                    + "\n\nCongratulations! Your expense report has been partially approved by your manager. The details of the partial approval are as follows:"
                    + "\n\nReport Title: " + report.getReportTitle() + "\nApproval Date: " + report.getManagerActionDate()
                    + ""
                    + "\n\nPlease note that not all expenses have been approved. Some expenses are partially approved according to the company policy. Please login to your BillFold account for detailed information of your expense report."
                    + "\n\nIf you have any questions or need further assistance, please feel free to contact your manager or the HR department."
                    + "\n\nThank you for your cooperation and timely submission of the expense report."
                    + "\n\nBest Regards," + "\nBillFold"
                    + "\n\nThis is an automated message. Please do not reply to this email.");

            this.javaMailSender.send(eMail);
        } else {
            throw new IllegalStateException("No expenses are added to the report " + report.getReportTitle());
        }
    }

    public void reminderMailToEmployee(List<Long> expenseIds) {
        for (Long expenseId : expenseIds) {
            Optional<Expense> expense = expenseRepository.findById(expenseId);
            if (expense != null) {
                Employee employee = expense.get().getEmployee();
                if (employee != null) {
                    String employeeEmail = employee.getEmployeeEmail();
                    SimpleMailMessage email = new SimpleMailMessage();
                    email.setFrom("billfold.noreply@gmail.com");
                    email.setTo(employeeEmail);
                    email.setSubject("[ACTION REQUIRED] Expense Report Submission Reminder");
                    email.setText("Dear " + employee.getFirstName() + " " + employee.getLastName() + ","
                            + "\n\nWe hope this email finds you well. We would like to remind you that you have not yet reported some of your expenses.It is important to submit your expense report in a timely manner to ensure accurate reimbursement and compliance with company policies."
                            + "\n\nPlease note that if your expenses are not reported within 60 days from the end of the reporting period, they will not be eligible for reimbursement. Therefore, we kindly request you to submit your expense report by the submission deadline. This will allow us to review and process your expenses promptly."
                            + "\n\nTo report your expenses, please access your BillFold account and follow the instructions provided. Ensure that all receipts and necessary supporting documents are attached for proper validation."
                            + "\n\nIf you have any questions or need assistance with the expense reporting process, please reach out to our HR department or your manager. We are here to help and ensure a smooth reimbursement process for you."
                            + "Thank you for your attention to this matter. Your cooperation in submitting your expense report within the specified deadline is greatly appreciated."
                            + "\n\nBest Regards," + "\nBillFold"
                            + "\n\nThis is an automated message. Please do not reply to this email.");

                    this.javaMailSender.send(email);
                }
            }
        }
    }


    @Override
    public void reminderMailToManager(List<Long> reportIds) {

        for (Long reportId : reportIds) {
            Optional<Reports> report = reportsRepository.findById(reportId);
            Long employeeId = report.get().getEmployeeId();
            Employee employee = employeeService.getEmployeeById(employeeId);
            String managerEmail = employee.getManagerEmail();
            SimpleMailMessage email = new SimpleMailMessage();
            email.setFrom("billfold.noreply@gmail.com");
            email.setTo(managerEmail);
            email.setSubject("[REMINDER] Pending Action on Expense Reports");
            email.setText("Dear " + employee.getManagerName() + ","
                    + "\n\nThis is a friendly reminder that you have pending expense reports awaiting your action. The reports have been submitted more than 30 days ago and are still pending approval."
                    + "\n\nPlease review and take appropriate action on the following report:"
                    + "\n\n" + report.get().getReportTitle()
                    + "\n\nTo take action on these reports, please log in to the BillFold app."
                    + "\n\nThank you for your attention to this matter."
                    + "\n\nBest Regards," + "\nBillFold"
                    + "\n\nThis is an automated message. Please do not reply to this email.");
            this.javaMailSender.send(email);
        }
    }
}

