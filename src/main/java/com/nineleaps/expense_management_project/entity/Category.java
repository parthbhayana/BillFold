package com.nineleaps.expense_management_project.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;

// Represents a category for expenses
@Entity
@Table(name = "category")
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "category_id")
	@ApiModelProperty(hidden = true) // Swagger annotation to hide this property in documentation
	private Long categoryId;

	@Column(name = "category_name")
	private String categoryDescription; // Description of the category

	@Column(name = "category_total")
	@ApiModelProperty(hidden = true) // Hidden property in Swagger documentation
	private long categoryTotal;

	@Column(name = "is_hidden", nullable = true)
	@ApiModelProperty(hidden = true) // Hidden property in Swagger documentation
	private Boolean isHidden = false;

	@JsonIgnore
	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
	@OnDelete(action = OnDeleteAction.NO_ACTION)
	private List<Expense> expenseList = new ArrayList<>(); // List of expenses associated with this category

	// Default constructor
	public Category() {
	}

	// Parameterized constructor to initialize category information
	public Category(Long categoryId, String categoryDescription, long categoryTotal, Boolean isHidden,
					List<Expense> expenseList) {
		super();
		this.categoryId = categoryId;
		this.categoryDescription = categoryDescription;
		this.categoryTotal = categoryTotal;
		this.isHidden = isHidden;
		this.expenseList = expenseList;
	}


    // Getter method to retrieve the category ID
	public Long getCategoryId() {
		return categoryId;
	}

	// Setter method to set the category ID
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	// Getter method to retrieve the category description
	public String getCategoryDescription() {
		return categoryDescription;
	}

	// Setter method to set the category description
	public void setCategoryDescription(String categoryDescription) {
		this.categoryDescription = categoryDescription;
	}

	// Getter method to retrieve the total amount for the category
	public long getCategoryTotal() {
		return categoryTotal;
	}

	// Setter method to set the total amount for the category
	public void setCategoryTotal(long categoryTotal) {
		this.categoryTotal = categoryTotal;
	}

	// Getter method to check if the category is hidden
	public Boolean getIsHidden() {
		return isHidden;
	}

	// Setter method to set whether the category is hidden
	public void setIsHidden(Boolean isHidden) {
		this.isHidden = isHidden;
	}

	// Getter method to retrieve the list of expenses associated with this category
	public List<Expense> getExpenseList() {
		return expenseList;
	}

	// Setter method to set the list of expenses associated with this category
	public void setExpenseList(List<Expense> expenseList) {
		this.expenseList = expenseList;
	}


}
