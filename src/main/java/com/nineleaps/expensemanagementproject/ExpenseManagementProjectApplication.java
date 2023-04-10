package com.nineleaps.expensemanagementproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class ExpenseManagementProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExpenseManagementProjectApplication.class, args);
	}

}
