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
@Table(name = "category")
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cat_Id")
	@ApiModelProperty(hidden = true)
	private Long catId;

	@Column(name = "category_name")
	private String catDescription;

	@Column(name = "category_total")
	@ApiModelProperty(hidden = true)
	private long categoryTotal;

	@Column(name = "is_hidden", nullable = true)
	@ApiModelProperty(hidden = true)
	private Boolean isHidden = false;

	@JsonIgnore
	@OneToMany(mappedBy = "categoryfinance", cascade = CascadeType.ALL)
	@OnDelete(action = OnDeleteAction.NO_ACTION)
	private List<Expense> expenseList = new ArrayList<>();

	public Category() {
		// TODO Auto-generated constructor stub
	}

	public Category(Long catId, String catDescription, long categoryTotal, Boolean isHidden) {
		super();
		this.catId = catId;
		this.catDescription = catDescription;
		this.categoryTotal = categoryTotal;
		this.isHidden = isHidden;
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

	public long getCategoryTotal() {
		return categoryTotal;
	}

	public void setCategoryTotal(long categoryTotal) {
		this.categoryTotal = categoryTotal;
	}

	public Boolean getIsHidden() {
		return isHidden;
	}

	public void setIsHidden(Boolean isHidden) {
		this.isHidden = isHidden;
	}

}