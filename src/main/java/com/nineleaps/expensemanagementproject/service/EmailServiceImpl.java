package com.nineleaps.expensemanagementproject.service;

import com.nineleaps.expensemanagementproject.repository.EmployeeRepository;
import com.nineleaps.expensemanagementproject.repository.ExpenseRepository;
import com.nineleaps.expensemanagementproject.repository.ReportsRepository;
import org.springframework.stereotype.Service;
import com.nineleaps.expensemanagementproject.entity.Employee;
import com.nineleaps.expensemanagementproject.entity.Expense;
import com.nineleaps.expensemanagementproject.entity.Reports;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.time.LocalTime;


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

    public static String dynamicSalutation() {
        LocalTime currentTime = LocalTime.now();
        int currentTimeInSeconds = currentTime.toSecondOfDay();

        int morningStartInSeconds = LocalTime.of(5, 0).toSecondOfDay();
        int afternoonStartInSeconds = LocalTime.of(12, 0).toSecondOfDay();
        int eveningStartInSeconds = LocalTime.of(17, 0).toSecondOfDay();
        int nightStartInSeconds = LocalTime.of(20, 0).toSecondOfDay();

        if (currentTimeInSeconds >= morningStartInSeconds && currentTimeInSeconds < afternoonStartInSeconds) {
            return "Good Morning ";
        } else if (currentTimeInSeconds >= afternoonStartInSeconds && currentTimeInSeconds < eveningStartInSeconds) {
            return "Good Afternoon ";
        } else if (currentTimeInSeconds >= eveningStartInSeconds && currentTimeInSeconds < nightStartInSeconds) {
            return "Good Evening ";
        } else {
            return "Dear ";
        }
    }

    private static final String CONSTANT7 = "\n\nBest Regards,";
    private static final String CONSTANT8 = "\nBillFold";
    private static final String CONSTANT9 =
            "\n\nThank you for your cooperation and timely submission of the expense report.";

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
            eMail.setText(dynamicSalutation() + employee.getFirstName() + ",\n\n" +
                    "We’re thrilled to have you onboard! Your schedule just got a lot lighter, with no more hassle of" +
                    " managing expenses and reimbursement requests!\n\n\n" + "You’re just a few quick steps away:\n\n" +
                    "1. Complete Your Profile\n\n" + "- Add your manager's name and email.\n" +
                    "- Provide your official employee ID & phone number.\n\n" + "2. Add Individual Expenses\n\n" +
                    "With every expense incurred, update the application with the following details:\n\n" +
                    "- Merchant name and transaction date\n" + "- Description of the expense.\n" +
                    "- Attach a clear image of the receipt.\n\n" + "3. Create an Expense Report\n\n" +
                    "Group the related expenses into a single expense report:\n\n" +
                    "- Create a new report with a meaningful title.\n" +
                    "- Compile individual expenses to the report.\n" + "- Submit it to your manager.\n\n" +
                    "And Voila! Your work is done!\n\n" +
                    "From here review and approval process will begin. Upon successful review, your reimbursement " +
                    "will be processed as per the company's policy.\n\n" +
                    "Feel free to explore the app's features and familiarize yourself with its functionalities.\n" +
                    "In case of any queries reach out to us at @billfold.noreply@gmail.com." + CONSTANT3 +
                    "\n\nThanks!");
            javaMailSender.send(message);
        } else {
            throw new IllegalStateException("Employee does not exist!");
        }
    }

    @Override
    public void notifyHr(Long reportId, String hrEmail, String hrName,
                         HttpServletResponse response) throws MessagingException, IOException {
        Reports report = reportsService.getReportById(reportId);
        Long empId = report.getEmployeeId();
        Employee employee = employeeService.getEmployeeById(empId);
        if (reportId != null) {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper eMail = new MimeMessageHelper(message, true);
            eMail.setFrom(CONSTANT1);
            eMail.setTo(hrEmail);
            eMail.setSubject("Report Submission Notification");
            eMail.setText(dynamicSalutation() + hrName + ",\n\n" + "We would like to inform you that " +
                    employee.getFirstName() + " " + employee.getLastName() + " has submitted a " +
                    "report for approval to their " +
                    "designated manager. In our commitment to transparency and efficient workflow, we are providing " +
                    "you with the following details of the report submission:" + CONSTANT2 + report.getReportTitle() +
                    "\nSubmitter's Name: " + employee.getFirstName() + " " + employee.getLastName() +
                    "\nSubmitted To: " + employee.getManagerName() + " (" + report.getManagerEmail() + ")" +
                    "\nSubmission Date: " + report.getDateSubmitted() + "\nTotal Amount: ₹" + report.getTotalAmount() +
                    "\n\nThis report is currently under review by the manager. As a member of the HR team, your " +
                    "involvement may be required at various stages of the approval process.\n\n" +
                    "Your participation in this process ensures that all HR-related aspects are appropriately " +
                    "addressed. If you have any questions or need further information regarding this report, please " +
                    "do not hesitate to reach out to the designated manager or the submitter directly." + CONSTANT3 +
                    "\n\nThank you for your cooperation and commitment to maintaining a smooth workflow within our " +
                    "organization.\n\n" + "Best regards,\n" + "BillFold.");
            List<Expense> expenseList = expenseService.getExpenseByReportId(reportId);
            List<Long> expenseIds = new ArrayList<>();
            for (Expense exp : expenseList) {
                expenseIds.add(exp.getExpenseId());
            }
            byte[] fileData = pdfGeneratorService.export(reportId, expenseIds, response);
            ByteArrayResource resource = new ByteArrayResource(fileData);
            eMail.addAttachment(report.getReportId() + ".pdf", resource);
            javaMailSender.send(message);
        } else {
            throw new IllegalStateException("Employee does not exist!");
        }
    }

    @Override
    public void notifyLnD(Long reportId, String lndEmail, String lndName,
                          HttpServletResponse response) throws MessagingException, IOException {
        Reports report = reportsService.getReportById(reportId);
        Long empId = report.getEmployeeId();
        Employee employee = employeeService.getEmployeeById(empId);
        if (reportId != null) {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper eMail = new MimeMessageHelper(message, true);
            eMail.setFrom(CONSTANT1);
            eMail.setTo(lndEmail);
            eMail.setSubject("Report Submission Notification"); //
            eMail.setText(dynamicSalutation() + lndName + ",\n\n" + "We would like to inform you that " +
                    employee.getFirstName() + " " + employee.getLastName() + " has submitted a " +
                    "report for approval to their " +
                    "designated manager. In our commitment to transparency and efficient workflow, we are providing " +
                    "you with the following details of the report submission:" + CONSTANT2 + report.getReportTitle() +
                    "\nSubmitter's Name: " + employee.getFirstName() + " " + employee.getLastName() +
                    "\nSubmitted To: " + employee.getManagerName() + " (" + report.getManagerEmail() + ")" +
                    "\nSubmission Date: " + report.getDateSubmitted() + "\nTotal Amount: ₹" + report.getTotalAmount() +
                    "\n\nThis report is currently under review by the manager. As a member of the L&D team, your " +
                    "involvement may be required at various stages of the approval process.\n\n" +
                    "Your participation in this process ensures that all L&D-related aspects are appropriately " +
                    "addressed. If you have any questions or need further information regarding this report, please " +
                    "do not hesitate to reach out to the designated manager or the submitter directly." + CONSTANT3 +
                    "\n\nThank you for your cooperation and commitment to maintaining a smooth workflow within our " +
                    "organization.\n\n" + "Best regards,\n" + "BillFold.");
            List<Expense> expenseList = expenseService.getExpenseByReportId(reportId);
            List<Long> expenseIds = new ArrayList<>();
            for (Expense exp : expenseList) {
                expenseIds.add(exp.getExpenseId());
            }
            byte[] fileData = pdfGeneratorService.export(reportId, expenseIds, response);
            ByteArrayResource resource = new ByteArrayResource(fileData);
            eMail.addAttachment(report.getReportId() + ".pdf", resource);
            javaMailSender.send(message);
        } else {
            throw new IllegalStateException("Employee does not exist!");
        }
    }

    @Override
    public void managerNotification(Long reportId, List<Long> expenseIds,
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
                eMail.setTo(employee.getManagerEmail());
                eMail.setSubject("BillFold - " + employee.getFirstName() + " " + employee.getLastName());
                eMail.setText(
                        dynamicSalutation() + employee.getManagerName() + ".\n\n" + employee.getFirstName() + " " +
                                employee.getLastName() +
                                " has submitted you a report for approval. As a designated manager, we kindly request" +
                                " your prompt attention to review and take necessary action on the report." +
                                "\n\nBelow are the details of the report submission:" + CONSTANT2 +
                                report.getReportTitle() + "\nSubmitter's Name: " + employee.getFirstName() + " " +
                                employee.getLastName() + "\nSubmission Date: " + report.getDateSubmitted() +
                                "\nTotal Amount: ₹" + report.getTotalAmount() +
                                "\n\nPlease log in to your Billfold account to access the report and review its " +
                                "contents. We kindly request you to carefully evaluate the report and take " +
                                "appropriate action based on your assessment." + CONSTANT3 + "\n\nThanks!");
                byte[] fileData = pdfGeneratorService.export(reportId, expenseIds, response);
                ByteArrayResource resource = new ByteArrayResource(fileData);
                eMail.addAttachment(report.getReportId() + ".pdf", resource);
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
                eMail.setText(
                        dynamicSalutation() + employee.getManagerName() + ",\n\n" + employee.getFirstName() + " " +
                                employee.getLastName() +
                                " has submitted you a report for approval. As a designated manager, we kindly request" +
                                " your prompt attention to review and take necessary action on the report." +
                                "\n\nBelow are the details of the report submission:" + CONSTANT2 +
                                report.getReportTitle() + "\nSubmitter's Name: " + employee.getFirstName() + " " +
                                employee.getLastName() + "\nSubmission Date: " + report.getDateSubmitted() +
                                "\nTotal Amount: ₹" + report.getTotalAmount() +
                                "\n\nPlease log in to your Billfold account to access the report and review its " +
                                "contents. We kindly request you to carefully evaluate the report and take " +
                                "appropriate action based on your assessment." + CONSTANT3 + "\n\nThanks!");
                byte[] fileData = pdfGeneratorService.export(reportId, expenseIds, response);
                ByteArrayResource resource = new ByteArrayResource(fileData);
                eMail.addAttachment(report.getReportId() + ".pdf", resource);
                javaMailSender.send(message);
            } else {
                throw new IllegalStateException(CONSTANT5 + report.getReportTitle());
            }
        } else {
            throw new IllegalStateException(CONSTANT5 + report.getReportTitle());
        }
    }

    @Override
    public void userRejectedNotification(Long reportId, List<Long> expenseIds,
                                         HttpServletResponse response) throws IOException, MessagingException {
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
            eMail.setText(dynamicSalutation() + employee.getFirstName() + " " + employee.getLastName() + "," +
                    "\n\nWe regret to inform you that your expense report has been rejected by your manager. The " +
                    "details of the rejection are as follows:" + CONSTANT2 + report.getReportTitle() +
                    "\nRejection Reason: " + report.getManagerComments() + "\nRejection Date: " +
                    report.getManagerActionDate() +
                    "\n\nPlease review the feedback provided by your manager and make the necessary revisions to your" +
                    " expense report. Once you have made the required changes, resubmit the report for further " +
                    "processing." +
                    "\n\nIf you have any questions or need clarification regarding the rejection, please reach out to" +
                    " your manager or the HR department." + "\n\nThank you for your understanding and cooperation." +
                    CONSTANT7 + CONSTANT8 + CONSTANT3);
//            byte[] fileData = pdfEmployeeGeneratorService.export(reportId, expenseIds, response);
            List<Expense> expenses = expenseService.getExpenseByReportId(reportId);
            List<Long> expIds = new ArrayList<>();
            for (Expense exp : expenses) {
                expIds.add(exp.getExpenseId());
            }
            byte[] fileData = pdfEmployeeGeneratorService.export(reportId, expIds, response);
            ByteArrayResource resource = new ByteArrayResource(fileData);
            eMail.addAttachment(report.getReportId() + ".pdf", resource);
            this.javaMailSender.send(message);
        } else {
            throw new IllegalStateException(CONSTANT5 + report.getReportTitle());
        }
    }

    @Override
    public void userApprovedNotification(Long reportId, List<Long> expenseIds,
                                         HttpServletResponse response) throws IOException, MessagingException {
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
            eMail.setText(dynamicSalutation() + employee.getFirstName() + " " + employee.getLastName() + "," +
                    "\n\nCongratulations! Your expense report has been approved by your manager. The details of the " +
                    "approval are as follows:" + CONSTANT2 + report.getReportTitle() + "\nApproved Amount: ₹" +
                    report.getTotalApprovedAmount() + "\nApproval Date: " + report.getManagerActionDate() +
                    "\n\nIf you have any questions or need further assistance, please feel free to contact your " +
                    "manager or the HR department." + CONSTANT9 + CONSTANT7 + CONSTANT8 + CONSTANT3);

            byte[] fileData = pdfEmployeeGeneratorService.export(reportId, expenseIds, response);
            ByteArrayResource resource = new ByteArrayResource(fileData);
            eMail.addAttachment(report.getReportId() + ".pdf", resource);
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
            eMail.setText(dynamicSalutation() + employee.getFirstName() + " " + employee.getLastName() + "," +
                    "\n\nCongratulations! Your expense report has been pushed to reimbursement by the finance " +
                    "department. The details of the reimbursement are as follows:" + CONSTANT2 +
                    report.getReportTitle() + "\n\nAmount Reimbursed: ₹" + report.getTotalApprovedAmount() +
                    "\nReimbursement Date: " + report.getFinanceActionDate() +
                    "\n\nPlease check your bank account for the credited amount. If you have any questions or " +
                    "concerns regarding the reimbursement, please reach out to the finance department." + CONSTANT9 +
                    CONSTANT7 + CONSTANT8 + CONSTANT3);

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
            email.setText(dynamicSalutation() + employee.getFirstName() + " " + employee.getLastName() + "," +
                    "\n\nWe regret to inform you that your expense report has been rejected by the finance department" +
                    ". The details of the rejection are as follows:" + CONSTANT2 + report.getReportTitle() +
                    "\n\nRejection Reason: " + report.getFinanceComments() + "\nRejection Date: " +
                    report.getFinanceActionDate() +
                    "\n\nPlease review the feedback provided by the finance department and make the necessary " +
                    "revisions to your expense report. Once you have made the required changes, resubmit the report " +
                    "for further processing." +
                    "\n\nIf you have any questions or need clarification regarding the rejection, please reach out to" +
                    " the finance department or your manager." +
                    "\n\nThank you for your understanding and cooperation." + CONSTANT7 + CONSTANT8 + CONSTANT3);

            this.javaMailSender.send(email);
        } else {
            throw new IllegalStateException(CONSTANT5 + report.getReportTitle());
        }
    }

    @Override
    public void financeNotification(Long reportId, List<Long> expenseIds,
                                    HttpServletResponse response) throws IOException, MessagingException {
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
            eMail.setText(dynamicSalutation() + admin.getFirstName() + "," +
                    "\n\nYou have received a request for expense reimbursement from employee " +
                    employee.getFirstName() + " " + employee.getLastName() +
                    ". The details of the expense report are as follows:" + CONSTANT2 + report.getReportTitle() +
                    "\nEmployee: " + employee.getFirstName() + " " + employee.getLastName() + "\nTotal Amount: ₹" +
                    report.getTotalApprovedAmount() + " " +
                    "\n\nPlease review the attached expense report and process the reimbursement accordingly. If " +
                    "there are any additional details required or if you have any questions, please reach out to the " +
                    "employee or the HR department." + "\n\nThank you for your attention to this matter." + CONSTANT7 +
                    CONSTANT8 + CONSTANT3);

            byte[] fileData = pdfGeneratorService.export(reportId, expenseIds, response);
            ByteArrayResource resource = new ByteArrayResource(fileData);
            eMail.addAttachment(report.getReportId() + ".pdf", resource);
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
            eMail.setText(dynamicSalutation() + employee.getFirstName() + " " + employee.getLastName() + "," +
                    "\n\nCongratulations! Your expense report has been partially approved by your manager. The " +
                    "details of the partial approval are as follows:" + CONSTANT2 + report.getReportTitle() +
                    "\nApproval Date: " + report.getManagerActionDate() + "" +
                    "\n\nPlease note that not all expenses have been approved. Some expenses are partially approved " +
                    "according to the company policy. Please login to your BillFold account for detailed information " +
                    "of your expense report." +
                    "\n\nIf you have any questions or need further assistance, please feel free to contact your " +
                    "manager or the HR department." + CONSTANT9 + CONSTANT7 + CONSTANT8 + CONSTANT3);

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
                email.setText(dynamicSalutation() + employee.getFirstName() + " " + employee.getLastName() + "," +
                        "\n\nWe hope this email finds you well. We would like to remind you that you have not yet " +
                        "reported some of your expenses.It is important to submit your expense report in a timely " +
                        "manner to ensure accurate reimbursement and compliance with company policies." +
                        "\n\nPlease note that if your expenses are not reported within 60 days from the end of the " +
                        "reporting period, they will not be eligible for reimbursement. Therefore, we kindly request " +
                        "you to submit your expense report by the submission deadline. This will allow us to review " +
                        "and process your expenses promptly." +
                        "\n\nTo report your expenses, please access your BillFold account and follow the instructions" +
                        " provided. Ensure that all receipts and necessary supporting documents are attached for " +
                        "proper validation." +
                        "\n\nIf you have any questions or need assistance with the expense reporting process, please " +
                        "reach out to our HR department or your manager. We are here to help and ensure a smooth " +
                        "reimbursement process for you." +
                        "Thank you for your attention to this matter. Your cooperation in submitting your expense " +
                        "report within the specified deadline is greatly appreciated." + CONSTANT7 + CONSTANT8 +
                        CONSTANT3);

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
            email.setText(dynamicSalutation() + employee.getManagerName() + "," +
                    "\n\nThis is a friendly reminder that you have pending expense reports awaiting your action. The " +
                    "reports have been submitted more than 30 days ago and are still pending approval." +
                    "\n\nPlease review and take appropriate action on the following report:" + "\n\n" +
                    report.get().getReportTitle() +
                    "\n\nTo take action on these reports, please log in to the BillFold app." +
                    "\n\nThank you for your attention to this matter." + CONSTANT7 + CONSTANT8 + CONSTANT3);
            this.javaMailSender.send(email);
        }
    }
}
