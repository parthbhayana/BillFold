package com.nineleaps.expensemanagementproject.dto;

import java.util.List;
import com.nineleaps.expensemanagementproject.entity.Reports;

public class ExpensetoReport {
	 private Reports report;
	    private List<Long> expenseIds;
		public ExpensetoReport(Reports report, List<Long> expenseIds) {
			super();
			this.report = report;
			this.expenseIds = expenseIds;
		}
	     public ExpensetoReport()
	     {
	    	 super();
	     }
		public Reports getReport() {
			return report;
		}
		public void setReport(Reports report) {
			this.report = report;
		}
		public List<Long> getExpenseIds() {
			return expenseIds;
		}
		public void setExpenseIds(List<Long> expenseIds) {
			this.expenseIds = expenseIds;
		}
}
	   

