package com.nineleaps.expensemanagementproject.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
public class SecurityConfig {

    @Autowired
    private JwtUtil jwtUtil;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-ui/**", "/configuration/security",
                                        "/swagger-ui.html", "/webjars/**")
                                .permitAll()
                                .antMatchers("/theProfile").permitAll()
                                .antMatchers("/getProfileData").hasAnyAuthority("EMPLOYEE", "FINANCE_ADMIN")
                                .antMatchers("/getExpenseByEmployeeId/{employeeId}").hasAnyAuthority("EMPLOYEE", "FINANCE_ADMIN")
                                .antMatchers("/updateCategory/{categoryId}").hasAuthority("FINANCE_ADMIN")
                                .antMatchers("/showAllCategories").hasAnyAuthority("EMPLOYEE", "FINANCE_ADMIN")
                                .antMatchers("/hideCategory/{categoryId}").hasAuthority("FINANCE_ADMIN")
                                .antMatchers("/categoryTotalAmount").hasAuthority("FINANCE_ADMIN").antMatchers("/insertCategory")
                                .hasAuthority("FINANCE_ADMIN").antMatchers("/currencyList")
                                .hasAnyAuthority("EMPLOYEE", "FINANCE_ADMIN").antMatchers("/insertExpenses/{employeeId}")
                                .hasAnyAuthority("EMPLOYEE", "FINANCE_ADMIN").antMatchers("/getExpenseByReportId/{reportId}")
                                .hasAnyAuthority("EMPLOYEE", "FINANCE_ADMIN").antMatchers("/getReportByEmployeeId/{employeeId}")
                                .hasAnyAuthority("EMPLOYEE", "FINANCE_ADMIN").antMatchers("/addReport/{employeeId}")
                                .hasAnyAuthority("EMPLOYEE", "FINANCE_ADMIN")
                                .antMatchers("/getReportsSubmittedToUser/{managerEmail}")
                                .hasAnyAuthority("EMPLOYEE", "FINANCE_ADMIN").antMatchers("/getAllReportsApprovedByManager")
                                .hasAuthority("FINANCE_ADMIN").antMatchers("/submitReport/{reportId}")
                                .hasAnyAuthority("EMPLOYEE", "FINANCE_ADMIN").antMatchers("/approveReportByFinance/{reportId}")
                                .hasAuthority("FINANCE_ADMIN").antMatchers("/approveReportByManager/{reportId}")
                                .hasAnyAuthority("EMPLOYEE", "FINANCE_ADMIN").antMatchers("/rejectReportByFinance/{reportId}")
                                .hasAuthority("FINANCE_ADMIN").antMatchers("/rejectReportByManager/{reportId}")
                                .hasAnyAuthority("EMPLOYEE", "FINANCE_ADMIN").antMatchers("/updateExpenses/{expenseId}")
                                .hasAnyAuthority("EMPLOYEE", "FINANCE_ADMIN").antMatchers("/editReport/{reportId}")
                                .hasAnyAuthority("EMPLOYEE", "FINANCE_ADMIN").antMatchers("/hideExpense/{expenseId}")
                                .hasAnyAuthority("EMPLOYEE", "FINANCE_ADMIN").antMatchers("/hideReport/{reportId}")
                                .hasAnyAuthority("EMPLOYEE", "FINANCE_ADMIN").antMatchers("/getReportsInDateRange")
                                .hasAuthority("FINANCE_ADMIN").antMatchers("/openpdf/export/{reportId}")
                                .hasAnyAuthority("EMPLOYEE", "FINANCE_ADMIN").antMatchers("/excel/reports")
                                .hasAuthority("FINANCE_ADMIN").antMatchers("/excel/categoryBreakup").hasAuthority("FINANCE_ADMIN")
                                .antMatchers("/getReportsSubmittedToUserInDateRange").hasAuthority("FINANCE_ADMIN")
                );
        return http.build();
    }
}