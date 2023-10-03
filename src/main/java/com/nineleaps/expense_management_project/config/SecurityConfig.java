package com.nineleaps.expense_management_project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {


    @SuppressWarnings("unused")
    private static final String CONSTANT1 = "EMPLOYEE";
    @SuppressWarnings("unused")
    private static final String CONSTANT2 = "FINANCE_ADMIN";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,JwtUtil jwtUtil) throws Exception {
        http.csrf().disable().addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests(authorizeRequests -> authorizeRequests
                                        .antMatchers("/theProfile").permitAll()
                                        .antMatchers("/getProfileData").hasAnyAuthority(CONSTANT1, CONSTANT2)
                                        .antMatchers("/getExpenseByEmployeeId/{employeeId}").hasAnyAuthority(CONSTANT1,CONSTANT2)
                                        .antMatchers("/updateCategory/{categoryId}").hasAuthority(CONSTANT2)
                                        .antMatchers("/showAllCategories").hasAnyAuthority(CONSTANT1, CONSTANT2)
                                        .antMatchers("/hideCategory/{categoryId}").hasAuthority(CONSTANT2)
                                        .antMatchers("/categoryTotalAmount").hasAuthority(CONSTANT2)
                                        .antMatchers("/insertCategory").hasAuthority(CONSTANT2)
                                        .antMatchers("/currencyList").hasAnyAuthority(CONSTANT1, CONSTANT2)
                                        .antMatchers("/insertExpenses/{employeeId}").hasAnyAuthority(CONSTANT1, CONSTANT2)
                                        .antMatchers("/getExpenseByReportId/{reportId}").hasAnyAuthority(CONSTANT1, CONSTANT2)
                                        .antMatchers("/getReportByEmployeeId/{employeeId}").hasAnyAuthority(CONSTANT1, CONSTANT2)
                                        .antMatchers("/addReport/{employeeId}").hasAnyAuthority(CONSTANT1, CONSTANT2)
                                        .antMatchers("/getReportsSubmittedToUser/{managerEmail}").hasAnyAuthority(CONSTANT1, CONSTANT2)
                                        .antMatchers("/getAllReportsApprovedByManager").hasAuthority(CONSTANT2)
                                        .antMatchers("/submitReport/{reportId}").hasAnyAuthority(CONSTANT1, CONSTANT2)
                                        .antMatchers("/approveReportByFinance/{reportId}").hasAuthority(CONSTANT2)
                                        .antMatchers("/approveReportByManager/{reportId}").hasAnyAuthority(CONSTANT1, CONSTANT2)
                                        .antMatchers("/rejectReportByFinance/{reportId}").hasAuthority(CONSTANT2)
                                        .antMatchers("/rejectReportByManager/{reportId}").hasAnyAuthority(CONSTANT1, CONSTANT2)
                                        .antMatchers("/updateExpenses/{expenseId}").hasAnyAuthority(CONSTANT1, CONSTANT2)
                                        .antMatchers("/editReport/{reportId}").hasAnyAuthority(CONSTANT1, CONSTANT2)
                                        .antMatchers("/hideExpense/{expenseId}").hasAnyAuthority(CONSTANT1, CONSTANT2)
                                        .antMatchers("/hideReport/{reportId}").hasAnyAuthority(CONSTANT1, CONSTANT2)
                                        .antMatchers("/getReportsInDateRange").hasAuthority(CONSTANT2)
                                        .antMatchers("/openpdf/export/{reportId}").hasAnyAuthority(CONSTANT1, CONSTANT2)
                                        .antMatchers("/excel/reports").hasAuthority(CONSTANT2)
                                        .antMatchers("/excel/categoryBreakup").hasAuthority(CONSTANT2)
                                        .antMatchers("/getReportsSubmittedToUserInDateRange").hasAuthority(CONSTANT2)
                                        .antMatchers("/getCategoryAnalyticsYearly/{categoryId}").hasAuthority(CONSTANT2)
                                        .antMatchers("/getCategoryAnalyticsMonthly/{categoryId}").hasAuthority(CONSTANT2)
                                        .antMatchers("/getAllCategoryAnalyticsYearly").hasAuthority(CONSTANT2)
                                        .antMatchers("/getAllCategoryAnalyticsMonthly").hasAuthority(CONSTANT2)
                                        .antMatchers("/updateExpenseStatus/{reportId}").hasAuthority(CONSTANT1)

                );
        return http.build();
    }

    private static final String[] AUTH_WHITELIST = { "/v3/api-docs/**", "/swagger-ui/**", "/v2/api-docs/**",
            "/swagger-resources/**", "/api/v1/auth/**", "/v3/api-docs/.yaml", "/swagger-ui.html" };
}