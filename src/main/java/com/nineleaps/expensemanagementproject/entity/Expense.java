package com.nineleaps.expensemanagementproject.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;

@NoArgsConstructor
@AllArgsConstructor
@Data

@Entity
@Table(name = "expense")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "expense_id", nullable = false)
    @ApiModelProperty(hidden = true)
    private Long expenseId;

    @Column(name = "merchant_Name", nullable = false)
    private String merchantName;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "created_time")
    @ApiModelProperty(hidden = true)
    private LocalTime time;

    @Column(name = "currency")
    private String currency;

    @Column(name = "amount", nullable = false)
    private Long amount;

    @Column(name = "amount_INR", nullable = false)
    @ApiModelProperty(hidden = true)
    private float amountINR;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "category")
    @ApiModelProperty(hidden = true)
    private String categoryDescription;

    @Column(name = "is_reported", nullable = true)
    @ApiModelProperty(hidden = true)
    private Boolean isReported = false;

    @Column(name = "is_hidden", nullable = true)
    @ApiModelProperty(hidden = true)
    private Boolean isHidden = false;

    @Column(name = "report_title")
    @ApiModelProperty(hidden = true)
    private String reportTitle;

    @Column(name = "amount_approved")
    @ApiModelProperty(hidden = true)
    private float amountApproved;

    @Lob
    @Column(name = "supporting_documents", nullable = true)
    private byte[] supportingDocuments;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @JoinColumn(name = "report_id")
    private Reports reports;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    private Category category;
}