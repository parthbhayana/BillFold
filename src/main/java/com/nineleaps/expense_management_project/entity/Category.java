package com.nineleaps.expense_management_project.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;

@Getter
@Entity
@Table(name = "category")
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "category_id")
	@ApiModelProperty(hidden = true)
	private Long categoryId;

	@Column(name = "category_name")
	private String categoryDescription;

	@Column(name = "category_total")
	@ApiModelProperty(hidden = true)
	private long categoryTotal;

	@Column(name = "is_hidden")
	@ApiModelProperty(hidden = true)
	private Boolean isHidden = false;

	@JsonIgnore
	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
	@OnDelete(action = OnDeleteAction.NO_ACTION)
	private List<Expense> expenseList = new ArrayList<>();

	public Category() {

	}

	public Category(Long categoryId, String categoryDescription, long categoryTotal, Boolean isHidden,
					List<Expense> expenseList) {
		super();
		this.categoryId = categoryId;
		this.categoryDescription = categoryDescription;
		this.categoryTotal = categoryTotal;
		this.isHidden = isHidden;
		this.expenseList = expenseList;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public void setCategoryDescription(String categoryDescription) {
		this.categoryDescription = categoryDescription;
	}

	public void setCategoryTotal(long categoryTotal) {
		this.categoryTotal = categoryTotal;
	}

	public void setIsHidden(Boolean isHidden) {
		this.isHidden = isHidden;
	}

	public void setExpenseList(List<Expense> expenseList) {
		this.expenseList = expenseList;
	}

}