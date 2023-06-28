package com.nineleaps.expensemanagementproject.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data

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

    @Column(name = "mobile_number")
    private Long mobileNumber;

    @Column(name = "is_admin", nullable = true)
    @ApiModelProperty(hidden = true)
    private Boolean isFinanceAdmin = false;

    @Column(name = "image_url")
    @ApiModelProperty(hidden = true)
    private String imageUrl;

    @Column(name = "is_hidden", nullable = true)
    @ApiModelProperty(hidden = true)
    private Boolean isHidden = false;

    @JsonIgnore
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<Expense> expenseList = new ArrayList<>();

    @Column(name = "role")
    @ApiModelProperty(hidden = true)
    private String role = "EMPLOYEE";
}