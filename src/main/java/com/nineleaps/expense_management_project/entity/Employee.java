package com.nineleaps.expense_management_project.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;


@Getter
@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(hidden = true)
    @Column(name = "employee_id")
    private Long employeeId;

    @Column(name = "official_id")
    private String officialEmployeeId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "employee_email")
    private String employeeEmail;

    @Column(name = "manager_email")
    private String managerEmail;

    @Column(name = "manager_name")
    @ApiModelProperty(hidden = true)
    private String managerName;

    @Column(name = "hr_name")
    @ApiModelProperty(hidden = true)
    private String hrName;

    @Column(name = "hr_email")
    private String hrEmail;

    @Column(name = "lnd_name")
    @ApiModelProperty(hidden = true)
    private String lndName = "Vrusha";

    @Column(name = "lnd_email")
    @ApiModelProperty(hidden = true)
    private String lndEmail = "billfoldfinancejsr@gmail.com";

    @Column(name = "mobile_number")
    private Long mobileNumber;

    @Column(name = "is_admin")
    @ApiModelProperty(hidden = true)
    private Boolean isFinanceAdmin = false;

    @Column(name = "image_url")
    @ApiModelProperty(hidden = true)
    private String imageUrl;

    @Column(name = "is_hidden")
    @ApiModelProperty(hidden = true)
    private Boolean isHidden = false;

    @Column(name = "date_created")
    @ApiModelProperty(hidden = true)
    private LocalDateTime dateCreated;

    @JsonIgnore
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<Expense> expenseList = new ArrayList<>();

    @Column(name = "role")
    @ApiModelProperty(hidden = true)
    private String role = "EMPLOYEE";

    @Column(name = "fcm_token")
    @ApiModelProperty(hidden = true)
    private String token;

    public Employee() {

    }

    public Employee(Long employeeId, String officialEmployeeId, String firstName, String middleName, String lastName, String employeeEmail, String managerEmail, String managerName, String hrName, String hrEmail, String lndName, String lndEmail, Long mobileNumber, Boolean isFinanceAdmin, String imageUrl, Boolean isHidden, LocalDateTime dateCreated, List<Expense> expenseList, String role, String token) {
        this.employeeId = employeeId;
        this.officialEmployeeId = officialEmployeeId;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.employeeEmail = employeeEmail;
        this.managerEmail = managerEmail;
        this.managerName = managerName;
        this.hrName = hrName;
        this.hrEmail = hrEmail;
        this.lndName = lndName;
        this.lndEmail = lndEmail;
        this.mobileNumber = mobileNumber;
        this.isFinanceAdmin = isFinanceAdmin;
        this.imageUrl = imageUrl;
        this.isHidden = isHidden;
        this.dateCreated = Timestamp.getCurrentDateTime();
        this.expenseList = expenseList;
        this.role = role;
        this.token = token;
    }

    public Employee(Long employeeId, String officialEmployeeId, String firstName, String middleName, String lastName,
                    String employeeEmail, String managerEmail) {
        super();
        this.employeeId = employeeId;
        this.officialEmployeeId = officialEmployeeId;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.employeeEmail = employeeEmail;
        this.managerEmail = managerEmail;


    }

    public Employee(String managerName, String hrName, String hrEmail,
                    String lndName, String lndEmail, Long mobileNumber, Boolean isFinanceAdmin) {
        this.managerName = managerName;
        this.hrName = hrName;
        this.hrEmail = hrEmail;
        this.lndName = lndName;
        this.lndEmail = lndEmail;
        this.mobileNumber = mobileNumber;
        this.isFinanceAdmin = isFinanceAdmin;
    }

    public Employee(String imageUrl,
                    Boolean isHidden, List<Expense> expenseList, String role, String token) {
        this.imageUrl = imageUrl;
        this.isHidden = isHidden;
        this.expenseList = expenseList;
        this.role = role;
        this.token = token;
    }


    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public void setOfficialEmployeeId(String officialEmployeeId) {
        this.officialEmployeeId = officialEmployeeId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }

    public void setManagerEmail(String managerEmail) {
        this.managerEmail = managerEmail;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public void setHrName(String hrName) {
        this.hrName = hrName;
    }

    public void setHrEmail(String hrEmail) {
        this.hrEmail = hrEmail;
    }

    public void setLndName(String lndName) {
        this.lndName = lndName;
    }

    public void setLndEmail(String lndEmail) {
        this.lndEmail = lndEmail;
    }

    public void setMobileNumber(Long mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public void setIsFinanceAdmin(Boolean isFinanceAdmin) {
        this.isFinanceAdmin = isFinanceAdmin;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setIsHidden(Boolean isHidden) {
        this.isHidden = isHidden;
    }

    public void setExpenseList(List<Expense> expenseList) {
        this.expenseList = expenseList;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setDateCreated(LocalDateTime createdAt) {
        this.dateCreated = createdAt;
    }


}