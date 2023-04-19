package com.nineleaps.expensemanagementproject.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "category_finance")
public class CategoryFinance {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "cat_Id")
	private Long catId;

	@Column(name = "cat_decription")
	private String catDescription;

	public CategoryFinance() {

	}

	public CategoryFinance(Long catId, String catDescription) {
		super();
		this.catId = catId;
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

}
