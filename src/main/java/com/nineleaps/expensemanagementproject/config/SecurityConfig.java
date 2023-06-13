package com.nineleaps.expensemanagementproject.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtUtil jwtUtil;
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	    http
	        .csrf().disable()
	        .addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
	        .authorizeRequests()
	        .antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-ui/**", "/configuration/security",
	                "/swagger-ui.html", "/webjars/**")
	        .permitAll();
	         http.authorizeRequests()
	        .antMatchers("/get_profile_data").hasAnyAuthority("EMPLOYEE", "FINANCE_ADMIN")
	        .antMatchers("/get_expense_by_employee_id/{employee_id}").hasAnyAuthority("EMPLOYEE", "FINANCE_ADMIN")
	        .antMatchers("/update_category/{catid}").hasAuthority("FINANCE_ADMIN")
	        .antMatchers("/show_all_categories").hasAnyAuthority("EMPLOYEE", "FINANCE_ADMIN")
	        .antMatchers("/hide_category/{category_id}").hasAuthority("FINANCE_ADMIN")
	        .antMatchers("/category_total_amount").hasAuthority("FINANCE_ADMIN")
	        .antMatchers("/insert_category").hasAuthority("FINANCE_ADMIN")
	        .antMatchers("/currency_list").hasAnyAuthority("EMPLOYEE", "FINANCE_ADMIN")
	        .antMatchers("/insert_expenses/{employee_id}").hasAnyAuthority("EMPLOYEE", "FINANCE_ADMIN")
	        .antMatchers("/get_expense_by_report_id/{report_id}").hasAnyAuthority("EMPLOYEE", "FINANCE_ADMIN")
	        .antMatchers("/get_report_by_employee_id/{employee_id}").hasAnyAuthority("EMPLOYEE", "FINANCE_ADMIN")
	        .antMatchers("/add_report/{employee_id}").hasAnyAuthority("EMPLOYEE", "FINANCE_ADMIN")
	        .antMatchers("/get_reports_submitted_to_user/{manager_email}").hasAnyAuthority("EMPLOYEE", "FINANCE_ADMIN")
	        .antMatchers("/get_all_reports_approved_by_manager").hasAuthority("FINANCE_ADMIN")
	        .antMatchers("/submit_report/{report_id}").hasAnyAuthority("EMPLOYEE", "FINANCE_ADMIN")
	        .antMatchers("/approve_report_by_finance/{report_id}").hasAuthority("FINANCE_ADMIN")
	        .antMatchers("/approve_report_by_manager/{report_id}").hasAnyAuthority("EMPLOYEE", "FINANCE_ADMIN")
	        .antMatchers("/reject_report_byfinance/{report_id}").hasAuthority("FINANCE_ADMIN")
	        .antMatchers("/reject_report_by_manager/{report_id}").hasAnyAuthority("EMPLOYEE", "FINANCE_ADMIN")
	        .antMatchers("/update_expenses/{expense_id}").hasAnyAuthority("EMPLOYEE", "FINANCE_ADMIN")
	        .antMatchers("/edit_report/{report_id}").hasAnyAuthority("EMPLOYEE", "FINANCE_ADMIN")
	        .antMatchers("/hide_expense/{expense_id}").hasAnyAuthority("EMPLOYEE", "FINANCE_ADMIN")
	        .antMatchers("/hide_report/{report_id}").hasAnyAuthority("EMPLOYEE", "FINANCE_ADMIN")
	        .antMatchers("/get_reports_in_date_range").hasAuthority("FINANCE_ADMIN")
	        .antMatchers("/openpdf/export/{report_id}").hasAnyAuthority("EMPLOYEE", "FINANCE_ADMIN")
	        .antMatchers("/excel/all_submissions_status").hasAuthority("FINANCE_ADMIN")
	        .antMatchers("/excel/category_breakup").hasAuthority("FINANCE_ADMIN")
	        .antMatchers("/get_reports_submitted_to_user_in_date_range").hasAuthority("FINANCE_ADMIN");
	        
	}
}
	        //http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);


//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		http.csrf().disable().addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
//				.authorizeRequests().antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-ui/**",
//						"/configuration/security", "/swagger-ui.html", "/webjars/**")
//				.permitAll();
//		
//	    http.authorizeRequests().antMatchers("/the_profile").permitAll();
//		http.authorizeRequests().antMatchers("/get_profile_data").hasAnyAuthority("EMPLOYEE", "FINANCE_ADMIN");
//		http.authorizeRequests().antMatchers("/get_expense_by_employee_id/{employee_id}").hasAnyAuthority("EMPLOYEE",
//				"FINANCE_ADMIN");
//		http.authorizeRequests().antMatchers("/update_category/{catid}").hasAnyAuthority("FINANCE_ADMIN");
//		http.authorizeRequests().antMatchers("/show_all_categories").hasAnyAuthority("EMPLOYEE", "FINANCE_ADMIN");
//		http.authorizeRequests().antMatchers("/hide_category/{category_id}").hasAnyAuthority("FINANCE_ADMIN");
//		http.authorizeRequests().antMatchers("/category_total_amount").hasAnyAuthority("FINANCE_ADMIN");
//		http.authorizeRequests().antMatchers("/insert_category").hasAnyAuthority("FINANCE_ADMIN");
//		http.authorizeRequests().antMatchers("/currency_list").hasAnyAuthority("EMPLOYEE", "FINANCE_ADMIN");
//		http.authorizeRequests().antMatchers("/insert_expenses/{employee_id}").hasAnyAuthority("EMPLOYEE", "FINANCE_ADMIN");
//		http.authorizeRequests().antMatchers("/get_expense_by_report_id/{report_id}").hasAnyAuthority("EMPLOYEE",
//				"FINANCE_ADMIN");
//		http.authorizeRequests().antMatchers("/get_report_by_employee_id/{employee_id}").hasAnyAuthority("EMPLOYEE",
//				"FINANCE_ADMIN");
//		http.authorizeRequests().antMatchers("/add_report/{employee_id}").hasAnyAuthority("EMPLOYEE", "FINANCE_ADMIN");
//		http.authorizeRequests().antMatchers("/get_reports_submitted_to_user/{manager_email}").hasAnyAuthority("EMPLOYEE",
//				"FINANCE_ADMIN");
//		http.authorizeRequests().antMatchers("/get_all_reports_approved_by_manager").hasAnyAuthority("FINANCE_ADMIN");
//		http.authorizeRequests().antMatchers("/submit_report/{report_id}").hasAnyAuthority("EMPLOYEE", "FINANCE_ADMIN");
//		http.authorizeRequests().antMatchers("/approve_report_by_finance/{report_id}").hasAnyAuthority("FINANCE_ADMIN");
//		http.authorizeRequests().antMatchers("/approve_report_by_manager/{report_id}").hasAnyAuthority("EMPLOYEE",
//				"FINANCE_ADMIN");
//		http.authorizeRequests().antMatchers("/reject_report_byfinance/{report_id}").hasAnyAuthority("FINANCE_ADMIN");
//		http.authorizeRequests().antMatchers("/reject_report_by_manager/{report_id}").hasAnyAuthority("EMPLOYEE",
//				"FINANCE_ADMIN");
//		http.authorizeRequests().antMatchers("/update_expenses/{expense_id}").hasAnyAuthority("EMPLOYEE", "FINANCE_ADMIN");
//		http.authorizeRequests().antMatchers("/edit_report/{report_id}").hasAnyAuthority("EMPLOYEE", "FINANCE_ADMIN");
//		http.authorizeRequests().antMatchers("/hide_expense/{expense_id}").hasAnyAuthority("EMPLOYEE", "FINANCE_ADMIN");
//		http.authorizeRequests().antMatchers("/hide_report/{report_id}").hasAnyAuthority("EMPLOYEE", "FINANCE_ADMIN");
//		http.authorizeRequests().antMatchers("/get_reports_in_date_range").hasAnyAuthority("FINANCE_ADMIN");
//		http.authorizeRequests().antMatchers("/openpdf/export/{report_id}").hasAnyAuthority("EMPLOYEE", "FINANCE_ADMIN");
//		http.authorizeRequests().antMatchers("/excel/all_submissions_status").hasAnyAuthority("FINANCE_ADMIN");
//		http.authorizeRequests().antMatchers("/excel/category_breakup").hasAnyAuthority("FINANCE_ADMIN");
//		http.authorizeRequests().antMatchers("/get_reports_submitted_to_user_in_date_range").hasAnyAuthority("FINANCE_ADMIN");
//	}
//	}
