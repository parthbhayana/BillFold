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

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "category_finance")
public class CategoryFinance {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cat_Id")
	@ApiModelProperty(hidden = true)
	private Long catId;

	@Column(name = "cat_decription")
	private String catDescription;

	@JsonIgnore
	@OneToMany(mappedBy = "categoryfinance", cascade = CascadeType.ALL)
	@OnDelete(action = OnDeleteAction.NO_ACTION)
	private List<Expense> expenseList = new ArrayList<>();

	public CategoryFinance(String catDescription, List<Expense> expenseList) {
		super();
		this.catDescription = catDescription;
		this.expenseList = expenseList;
	}

	public CategoryFinance() {

	}

	public CategoryFinance(Long catId, String catDescription) {
		super();
		this.catId = catId;
		this.catDescription = catDescription;
	}

	public CategoryFinance(String catDescription) {
		super();
		this.catDescription = catDescription;
	}

	public Long getCatId() {
		return catId;
	}

	public void setCatId(Long catId) {
		this.catId = catId;
	}

	public String getCatDescription() {
		return catDescription;
	}

	public void setCatDescription(String catDescription) {
		this.catDescription = catDescription;
	}

	public List<Expense> getExpenseList() {
		return expenseList;
	}

	public void setExpenseList(List<Expense> expenseList) {
		this.expenseList = expenseList;
	}

}
