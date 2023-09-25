package com.nineleaps.expense_management_project.service;


import com.nineleaps.expense_management_project.entity.Employee;
import com.nineleaps.expense_management_project.entity.Reports;
import com.nineleaps.expense_management_project.repository.EmployeeRepository;
import com.nineleaps.expense_management_project.repository.ExpenseRepository;
import com.nineleaps.expense_management_project.repository.ReportsRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;

import com.nineleaps.expense_management_project.dto.CategoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nineleaps.expense_management_project.entity.Category;
import com.nineleaps.expense_management_project.entity.Expense;
import com.nineleaps.expense_management_project.repository.CategoryRepository;
import com.nineleaps.expense_management_project.repository.ExpenseRepository;

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
    private EmployeeRepository employeeRepository;

    @Autowired
    private IExpenseService expenseService;
    @Autowired
    private IEmployeeService employeeService;
    @Autowired
    private ReportsRepository reportsRepository;
    @Autowired
    private IPdfManagerGeneratorService pdfGeneratorService;
    @Autowired
    private IPdfEmployeeGeneratorService pdfEmployeeGeneratorService;

    private final JavaMailSender javaMailSender;
    private static final String CONSTANT1 = "billfold.noreply@gmail.com";
    private static final String CONSTANT2 = "\n\nReport Title: ";
    private static final String CONSTANT3 = "\n\nThis is an automated message. Please do not reply to this email.";
    private static final String CONSTANT4 = "Document.pdf";
    private static final String CONSTANT5 = "No expenses are added to the report ";
    private static final String CONSTANT6 = "Dear ";
    private static final String CONSTANT7 = "\n\nBest Regards,";
    private static final String CONSTANT8 = "\nBillFold";
    private static final String CONSTANT9 = "\n\nThank you for your cooperation and timely submission of the expense report.";

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.javaMailSender = mailSender;
    }

    @Override
    public void welcomeEmail(String employeeEmail) throws MessagingException {
        Employee employee = employeeService.getEmployeeByEmail(employeeEmail);
        if (employeeEmail != null) {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper eMail = new MimeMessageHelper(message, true);
            eMail.setFrom(CONSTANT1);
            eMail.setTo(employeeEmail);
            eMail.setSubject("Welcome to BillFold!"); //
            eMail.setText("Dear " + employee.getFirstName() + ",\n\n"
                    + "Welcome to BillFold! We're thrilled to have you onboard. "
                    + "This app is designed to simplify and streamline the process of managing your expenses and reimbursement requests. To get started, follow the steps below:\n\n"
                    + "Step 1: Complete Your Profile\n\n"
                    + "Before you start submitting expenses, please make sure your profile is complete. This is crucial for efficient processing of your reimbursement requests. Here's what you need to do:\n\n"
                    + "- Add your manager's name and email.\n" + "- Provide your official employee ID.\n"
                    + "- Enter your phone number.\n\n"
                    + "Please note that expenses will not be considered for reimbursement unless your profile details are filled in completely.\n\n"
                    + "Step 2: Add Individual Expenses\n\n"
                    + "When you incur an eligible expense, use the app to add it. Make sure to provide all the necessary details such as:\n\n"
                    + "- Merchant name.\n" + "- Description of the expense.\n" + "- Date of the transaction.\n"
                    + "- Attach a clear image of the receipt.\n\n"
                    + "This step ensures that each expense is accurately recorded and documented.\n\n"
                    + "Step 3: Create an Expense Report\n\n"
                    + "To simplify the approval process, group related expenses into a single expense report. Here's how:\n\n"
                    + "- Create a new report and give it a meaningful title.\n"
                    + "- Attach individual expenses to the report.\n"
                    + "- Once all relevant expenses are added, submit the report to your manager for approval.\n\n"
                    + "Step 4: Manager Approval\n\n"
                    + "Your manager will review the expense report and either approve or reject it. If approved, the report will then be sent to the finance admin for final review.\n\n"
                    + "Step 5: Finance Admin Review and Reimbursement\n\n"
                    + "The finance admin will conduct a final review of the approved report. Upon successful review, your reimbursement will be processed, and you'll receive the reimbursed amount as per the company's policy.\n\n"
                    + "Feel free to explore the app's features and familiarize yourself with its functionalities. If you have any questions or encounter any issues, don't hesitate to reach out to our support team at @billfold.noreply@gmail.com.\n\n"
                    + "Thank you for using BillFold. We look forward to making your expense management experience seamless and efficient."
                    + CONSTANT3 + "\n\nThanks!");
            javaMailSender.send(message);
        } else {
            throw new IllegalStateException("Employee does not exist!");
        }
    }

    @Override
    public void notifyHr(Long reportId, String hrEmail, String hrName) throws MessagingException {
        Reports report = reportsService.getReportById(reportId);
        if (reportId != null) {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper eMail = new MimeMessageHelper(message, true);
            eMail.setFrom(CONSTANT1);
            eMail.setTo(hrEmail);
            eMail.setSubject("Expense Report: " + report.getReportTitle());
            eMail.setText("Dear " + hrName + ",\n\n"
                    + "CONTENT HERE!!"
                    + CONSTANT3 + "\n\nThanks!");
            javaMailSender.send(message);
        } else {
            throw new IllegalStateException("Employee does not exist!");
        }
    }

    @Override
    public void notifyLnD(Long reportId, String lndEmail, String lndName) throws MessagingException {
        Reports report = reportsService.getReportById(reportId);
        if (reportId != null) {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper eMail = new MimeMessageHelper(message, true);
            eMail.setFrom(CONSTANT1);
            eMail.setTo(lndEmail);
            eMail.setSubject("Expense Report: " + report.getReportTitle()); //
            eMail.setText("Dear " + lndName + ",\n\n"
                    + "CONTENT HERE!!"
                    + CONSTANT3 + "\n\nThanks!");
            javaMailSender.send(message);
        } else {
            throw new IllegalStateException("Employee does not exist!");
        }
    }

    @Override
    public void managerNotification(Long reportId, List<Long> expenseIds, HttpServletResponse response)
            throws IOException, MessagingException {
        Reports report = reportsService.getReportById(reportId);
        List<Expense> expenseList = expenseService.getExpenseByReportId(reportId);
        if (!expenseList.isEmpty()) {
            Expense expense = expenseList.get(0);
            Employee employee = expense.getEmployee();
            if (employee != null && employee.getManagerEmail() != null) {
                MimeMessage message = javaMailSender.createMimeMessage();
                MimeMessageHelper eMail = new MimeMessageHelper(message, true);
                eMail.setFrom(CONSTANT1);
                eMail.setTo(employee.getManagerEmail());
                eMail.setSubject("BillFold - " + employee.getFirstName() + " " + employee.getLastName());
                eMail.setText(CONSTANT6 + employee.getManagerName() + ".\n\n"
                        + employee.getFirstName() + " " + employee.getLastName()
                        + " has submitted you a report for approval. As a designated manager, we kindly request your prompt attention to review and take necessary action on the report."
                        + "\n\nBelow are the details of the report submission:" + CONSTANT2 + report.getReportTitle()
                        + "\nSubmitter's Name: " + employee.getFirstName() + " " + employee.getLastName()
                        + "\nSubmission Date: " + report.getDateSubmitted() + "\nTotal Amount: ₹"
                        + report.getTotalAmount()
                        + "\n\nPlease log in to your Billfold account to access the report and review its contents. We kindly request you to carefully evaluate the report and take appropriate action based on your assessment."
                        + CONSTANT3 + "\n\nThanks!");
                byte[] fileData = pdfGeneratorService.export(reportId, expenseIds, response);
                ByteArrayResource resource = new ByteArrayResource(fileData);
                eMail.addAttachment(CONSTANT4, resource);
                javaMailSender.send(message);
            } else {
                throw new IllegalStateException(CONSTANT5 + report.getReportTitle());
            }
        } else {
            throw new IllegalStateException(CONSTANT5 + report.getReportTitle());
        }
    }

    @Override
    public void managerNotification(Long reportId, List<Long> expenseIds, String managerEmail,
                                    HttpServletResponse response) throws IOException, MessagingException {

        Reports report = reportsService.getReportById(reportId);
        List<Expense> expenseList = expenseService.getExpenseByReportId(reportId);
        if (!expenseList.isEmpty()) {
            Expense expense = expenseList.get(0);
            Employee employee = expense.getEmployee();
            if (employee != null && employee.getManagerEmail() != null) {
                MimeMessage message = javaMailSender.createMimeMessage();
                MimeMessageHelper eMail = new MimeMessageHelper(message, true);
                eMail.setFrom(CONSTANT1);
                eMail.setTo(managerEmail);
                eMail.setSubject("BillFold - " + employee.getFirstName() + " " + employee.getLastName());
                eMail.setText(CONSTANT6 + employee.getManagerName() + ",\n\n"
                        + employee.getFirstName() + " " + employee.getLastName()
                        + " has submitted you a report for approval. As a designated manager, we kindly request your prompt attention to review and take necessary action on the report."
                        + "\n\nBelow are the details of the report submission:" + CONSTANT2 + report.getReportTitle()
                        + "\nSubmitter's Name: " + employee.getFirstName() + " " + employee.getLastName()
                        + "\nSubmission Date: " + report.getDateSubmitted() + "\nTotal Amount: ₹"
                        + report.getTotalAmount()
                        + "\n\nPlease log in to your Billfold account to access the report and review its contents. We kindly request you to carefully evaluate the report and take appropriate action based on your assessment."
                        + CONSTANT3 + "\n\nThanks!");
                byte[] fileData = pdfGeneratorService.export(reportId, expenseIds, response);
                ByteArrayResource resource = new ByteArrayResource(fileData);
                eMail.addAttachment(CONSTANT4, resource);
                javaMailSender.send(message);
            } else {
                throw new IllegalStateException(CONSTANT5 + report.getReportTitle());
            }
        } else {
            throw new IllegalStateException(CONSTANT5 + report.getReportTitle());
        }
    }

    @Override
    public void userRejectedNotification(Long reportId, List<Long> expenseIds, HttpServletResponse response) throws IOException, MessagingException {
        Reports report = reportsService.getReportById(reportId);
        List<Expense> expenseList = expenseService.getExpenseByReportId(reportId);

        if (!expenseList.isEmpty()) {
            Expense expense = expenseList.get(0);
            Employee employee = expense.getEmployee();
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper eMail = new MimeMessageHelper(message, true);
            eMail.setFrom(CONSTANT1);
            eMail.setTo(employee.getEmployeeEmail());
            eMail.setSubject("[REJECTED] Expense Report: " + report.getReportTitle());
            eMail.setText(CONSTANT6 + employee.getFirstName() + " " + employee.getLastName() + ","
                    + "\n\nWe regret to inform you that your expense report has been rejected by your manager. The details of the rejection are as follows:"
                    + CONSTANT2 + report.getReportTitle() + "\nRejection Reason: " + report.getManagerComments()
                    + "\nRejection Date: " + report.getManagerActionDate()
                    + "\n\nPlease review the feedback provided by your manager and make the necessary revisions to your expense report. Once you have made the required changes, resubmit the report for further processing."
                    + "\n\nIf you have any questions or need clarification regarding the rejection, please reach out to your manager or the HR department."
                    + "\n\nThank you for your understanding and cooperation." + CONSTANT7 + CONSTANT8 + CONSTANT3);
//            byte[] fileData = pdfEmployeeGeneratorService.export(reportId, expenseIds, response);
            List<Expense> expenses = expenseService.getExpenseByReportId(reportId);
            List<Long> expIds = new ArrayList<>();
            for(Expense exp : expenses){
                expIds.add(exp.getExpenseId());
            }
            byte[] fileData = pdfEmployeeGeneratorService.export(reportId, expIds, response);
            ByteArrayResource resource = new ByteArrayResource(fileData);
            eMail.addAttachment(CONSTANT4, resource);
            this.javaMailSender.send(message);
        } else {
            throw new IllegalStateException(CONSTANT5 + report.getReportTitle());
        }
    }

    @Override
    public void userApprovedNotification(Long reportId, List<Long> expenseIds, HttpServletResponse response) throws IOException, MessagingException {
        Reports report = reportsService.getReportById(reportId);
        List<Expense> expenseList = expenseService.getExpenseByReportId(reportId);
        if (!expenseList.isEmpty()) {
            Expense expense = expenseList.get(0);
            Employee employee = expense.getEmployee();
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper eMail = new MimeMessageHelper(message, true);
            eMail.setFrom(CONSTANT1);
            eMail.setTo(employee.getEmployeeEmail());
            eMail.setSubject("[APPROVED] Expense Report: " + report.getReportTitle());
            eMail.setText(CONSTANT6 + employee.getFirstName() + " " + employee.getLastName() + ","
                    + "\n\nCongratulations! Your expense report has been approved by your manager. The details of the approval are as follows:"
                    + CONSTANT2 + report.getReportTitle() + "\nApproved Amount: ₹" + report.getTotalApprovedAmount()
                    + "\nApproval Date: " + report.getManagerActionDate()
                    + "\n\nIf you have any questions or need further assistance, please feel free to contact your manager or the HR department."
                    + CONSTANT9 + CONSTANT7 + CONSTANT8 + CONSTANT3);

            byte[] fileData = pdfEmployeeGeneratorService.export(reportId, expenseIds, response);
            ByteArrayResource resource = new ByteArrayResource(fileData);
            eMail.addAttachment(CONSTANT4, resource);
            this.javaMailSender.send(message);
        } else {
            throw new IllegalStateException(CONSTANT5 + report.getReportTitle());
        }
    }

    @Override
    public void userReimbursedNotification(Long reportId) {
        Reports report = reportsService.getReportById(reportId);
        List<Expense> expenseList = expenseService.getExpenseByReportId(reportId);
        if (!expenseList.isEmpty()) {
            Expense expense = expenseList.get(0);
            Employee employee = expense.getEmployee();
            SimpleMailMessage eMail = new SimpleMailMessage();
            eMail.setFrom(CONSTANT1);
            eMail.setTo(employee.getEmployeeEmail());
            eMail.setSubject("[PUSHED TO REIMBURSEMENT] Expense Report: " + report.getReportTitle());
            eMail.setText(CONSTANT6 + employee.getFirstName() + " " + employee.getLastName() + ","
                    + "\n\nCongratulations! Your expense report has been pushed to reimbursement by the finance department. The details of the reimbursement are as follows:"
                    + CONSTANT2 + report.getReportTitle() + "\n\nAmount Reimbursed: ₹" + report.getTotalApprovedAmount()
                    + "\nReimbursement Date: " + report.getFinanceActionDate()
                    + "\n\nPlease check your bank account for the credited amount. If you have any questions or concerns regarding the reimbursement, please reach out to the finance department."
                    + CONSTANT9 + CONSTANT7 + CONSTANT8 + CONSTANT3);

            this.javaMailSender.send(eMail);
        } else {
            throw new IllegalStateException(CONSTANT5 + report.getReportTitle());
        }
    }

    @Override
    public void userRejectedByFinanceNotification(Long reportId) {
        Reports report = reportsService.getReportById(reportId);
        List<Expense> expenseList = expenseService.getExpenseByReportId(reportId);

        if (!expenseList.isEmpty()) {
            Expense expense = expenseList.get(0);
            Employee employee = expense.getEmployee();
            SimpleMailMessage email = new SimpleMailMessage();
            email.setFrom(CONSTANT1);
            email.setTo(employee.getEmployeeEmail());
            email.setSubject("[REJECTED] Expense Report: " + report.getReportTitle());
            email.setText(CONSTANT6 + employee.getFirstName() + " " + employee.getLastName() + ","
                    + "\n\nWe regret to inform you that your expense report has been rejected by the finance department. The details of the rejection are as follows:"
                    + CONSTANT2 + report.getReportTitle() + "\n\nRejection Reason: " + report.getFinanceComments()
                    + "\nRejection Date: " + report.getFinanceActionDate()
                    + "\n\nPlease review the feedback provided by the finance department and make the necessary revisions to your expense report. Once you have made the required changes, resubmit the report for further processing."
                    + "\n\nIf you have any questions or need clarification regarding the rejection, please reach out to the finance department or your manager."
                    + "\n\nThank you for your understanding and cooperation." + CONSTANT7 + CONSTANT8 + CONSTANT3);

            this.javaMailSender.send(email);
        } else {
            throw new IllegalStateException(CONSTANT5 + report.getReportTitle());
        }
    }

    @Override
    public void financeNotification(Long reportId, List<Long> expenseIds, HttpServletResponse response)
            throws IOException, MessagingException {
        Reports report = reportsService.getReportById(reportId);
        List<Expense> expenseList = expenseService.getExpenseByReportId(reportId);
        Employee admin = employeeRepository.findByRole("FINANCE_ADMIN");
        String adminEmail = admin.getEmployeeEmail();

        if (!expenseList.isEmpty()) {
            Expense expense = expenseList.get(0);
            Employee employee = expense.getEmployee();
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper eMail = new MimeMessageHelper(message, true);
            eMail.setFrom(CONSTANT1);
            eMail.setTo(adminEmail);
            eMail.setSubject("Expense Reimbursement Request: " + report.getReportTitle());
            eMail.setText(CONSTANT6 + admin.getFirstName() + ","
                    + "\n\nYou have received a request for expense reimbursement from employee "
                    + employee.getFirstName() + " " + employee.getLastName()
                    + ". The details of the expense report are as follows:" + CONSTANT2 + report.getReportTitle()
                    + "\nEmployee: " + employee.getFirstName() + " " + employee.getLastName() + "\nTotal Amount: ₹"
                    + report.getTotalApprovedAmount() + " "
                    + "\n\nPlease review the attached expense report and process the reimbursement accordingly. If there are any additional details required or if you have any questions, please reach out to the employee or the HR department."
                    + "\n\nThank you for your attention to this matter." + CONSTANT7 + CONSTANT8 + CONSTANT3);

            byte[] fileData = pdfGeneratorService.export(reportId, expenseIds, response);
            ByteArrayResource resource = new ByteArrayResource(fileData);
            eMail.addAttachment(CONSTANT4, resource);
            this.javaMailSender.send(message);
            System.out.println("Finance Email Sent to" + adminEmail);
        } else {
            throw new IllegalStateException(CONSTANT5 + report.getReportTitle());
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
            eMail.setFrom(CONSTANT1);
            eMail.setTo(employee.getEmployeeEmail());
            eMail.setSubject("[PARTIAL APPROVAL] Expense Report: " + report.getReportTitle());
            eMail.setText(CONSTANT6 + employee.getFirstName() + " " + employee.getLastName() + ","
                    + "\n\nCongratulations! Your expense report has been partially approved by your manager. The details of the partial approval are as follows:"
                    + CONSTANT2 + report.getReportTitle() + "\nApproval Date: " + report.getManagerActionDate() + ""
                    + "\n\nPlease note that not all expenses have been approved. Some expenses are partially approved according to the company policy. Please login to your BillFold account for detailed information of your expense report."
                    + "\n\nIf you have any questions or need further assistance, please feel free to contact your manager or the HR department."
                    + CONSTANT9 + CONSTANT7 + CONSTANT8 + CONSTANT3);

            this.javaMailSender.send(eMail);
        } else {
            throw new IllegalStateException(CONSTANT5 + report.getReportTitle());
        }
    }

    public void reminderMailToEmployee(List<Long> expenseIds) {
        for (Long expenseId : expenseIds) {
            Optional<Expense> expense = expenseRepository.findById(expenseId);
            Employee employee = expense.get().getEmployee();
            if (employee != null) {
                SimpleMailMessage email = new SimpleMailMessage();
                email.setFrom(CONSTANT1);
                email.setTo(employee.getEmployeeEmail());
                email.setSubject("[ACTION REQUIRED] Expense Report Submission Reminder");
                email.setText(CONSTANT6 + employee.getFirstName() + " " + employee.getLastName() + ","
                        + "\n\nWe hope this email finds you well. We would like to remind you that you have not yet reported some of your expenses.It is important to submit your expense report in a timely manner to ensure accurate reimbursement and compliance with company policies."
                        + "\n\nPlease note that if your expenses are not reported within 60 days from the end of the reporting period, they will not be eligible for reimbursement. Therefore, we kindly request you to submit your expense report by the submission deadline. This will allow us to review and process your expenses promptly."
                        + "\n\nTo report your expenses, please access your BillFold account and follow the instructions provided. Ensure that all receipts and necessary supporting documents are attached for proper validation."
                        + "\n\nIf you have any questions or need assistance with the expense reporting process, please reach out to our HR department or your manager. We are here to help and ensure a smooth reimbursement process for you."
                        + "Thank you for your attention to this matter. Your cooperation in submitting your expense report within the specified deadline is greatly appreciated."
                        + CONSTANT7 + CONSTANT8 + CONSTANT3);

                this.javaMailSender.send(email);
            }
        }
    }

    @Override
    public void reminderMailToManager(List<Long> reportIds) {

        for (Long reportId : reportIds) {
            Optional<Reports> report = reportsRepository.findById(reportId);
            Long employeeId = report.get().getEmployeeId();
            Employee employee = employeeService.getEmployeeById(employeeId);
            SimpleMailMessage email = new SimpleMailMessage();
            email.setFrom(CONSTANT1);
            email.setTo(employee.getManagerEmail());
            email.setSubject("[REMINDER] Pending Action on Expense Reports");
            email.setText(CONSTANT6 + employee.getManagerName() + ","
                    + "\n\nThis is a friendly reminder that you have pending expense reports awaiting your action. The reports have been submitted more than 30 days ago and are still pending approval."
                    + "\n\nPlease review and take appropriate action on the following report:" + "\n\n"
                    + report.get().getReportTitle()
                    + "\n\nTo take action on these reports, please log in to the BillFold app."
                    + "\n\nThank you for your attention to this matter." + CONSTANT7 + CONSTANT8 + CONSTANT3);
            this.javaMailSender.send(email);
        }
    }
}