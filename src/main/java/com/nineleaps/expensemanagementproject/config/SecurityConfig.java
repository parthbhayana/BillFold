package com.nineleaps.expensemanagementproject.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.nineleaps.expensemanagementproject.filter.UserAuthorizationFilter;

import lombok.RequiredArgsConstructor;

@SuppressWarnings("deprecation")
@Configuration @EnableWebSecurity @RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	
	 @Override
	    protected void configure(HttpSecurity http) throws Exception {
	        http.csrf().disable()
	                .addFilterBefore(new UserAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
	                .authorizeRequests()
	                //.antMatchers("/v2/api-docs\", \"/configuration/ui\", \"/swagger-resources/**\", \"/configuration/**\", \"/swagger-ui.html\", \"/webjars/**")
//	                .antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources/**", "/configuration/**", "/swagger-ui.html", "/webjars/**")

	                .antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-ui/**", "/configuration/security",
	                	    "/*/swagger-ui.html", "/webjars/**")
	                .permitAll();
    
	 http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	 
	 //http.authorizeRequests().anyRequest().permitAll();
//	 http.authorizeRequests().antMatchers("/api/endpoint").hasIpAddress("127.0.0.1");
	 http.authorizeRequests().antMatchers("/getprofiledata").hasAnyAuthority("EMPLOYEE", "FINANCE_ADMIN");
	 http.authorizeRequests().antMatchers("/getexpensebyemployeeid/{empid}").hasAnyAuthority("EMPLOYEE", "FINANCE_ADMIN");
	 http.authorizeRequests().antMatchers("/updatecategory/{catid}").hasAnyAuthority("FINANCE_ADMIN");
	 http.authorizeRequests().antMatchers("/showallcategories").hasAnyAuthority("EMPLOYEE","FINANCE_ADMIN");
	 http.authorizeRequests().antMatchers("/hidecategory/{categoryId}").hasAnyAuthority("FINANCE_ADMIN");
	 http.authorizeRequests().antMatchers("/categorytotalamount").hasAnyAuthority("FINANCE_ADMIN");
	 http.authorizeRequests().antMatchers("/insertcategory").hasAnyAuthority("FINANCE_ADMIN");
	 http.authorizeRequests().antMatchers("/currencylist").hasAnyAuthority("EMPLOYEE","FINANCE_ADMIN");
	 http.authorizeRequests().antMatchers("/insertexpenses/{empid}").hasAnyAuthority("EMPLOYEE","FINANCE_ADMIN");
	 http.authorizeRequests().antMatchers("/getexpensebyreportid/{reportid}").hasAnyAuthority("EMPLOYEE","FINANCE_ADMIN");
	 http.authorizeRequests().antMatchers("/getreportbyemployeeid/{employeeId}").hasAnyAuthority("EMPLOYEE","FINANCE_ADMIN");
	 http.authorizeRequests().antMatchers("/addreport/{empId}").hasAnyAuthority("EMPLOYEE","FINANCE_ADMIN");
	 http.authorizeRequests().antMatchers("/getreportssubmittedtouser/{manageremail}").hasAnyAuthority("EMPLOYEE","FINANCE_ADMIN");
	 http.authorizeRequests().antMatchers("/getallreportsapprovedbymanager").hasAnyAuthority("FINANCE_ADMIN");
	 http.authorizeRequests().antMatchers("/submitReport/{reportId}").hasAnyAuthority("EMPLOYEE","FINANCE_ADMIN");
	 http.authorizeRequests().antMatchers("/approvereportbyfinance/{reportId}").hasAnyAuthority("FINANCE_ADMIN");
	 http.authorizeRequests().antMatchers("/approvereportbymanager/{reportId}").hasAnyAuthority("EMPLOYEE","FINANCE_ADMIN");
	 http.authorizeRequests().antMatchers("/rejectreportbyfinance/{reportId}").hasAnyAuthority("FINANCE_ADMIN");
	 http.authorizeRequests().antMatchers("/rejectreportbymanager/{reportId}").hasAnyAuthority("EMPLOYEE","FINANCE_ADMIN");
	 http.authorizeRequests().antMatchers("/updateexpenses/{expId}").hasAnyAuthority("EMPLOYEE","FINANCE_ADMIN");
	 http.authorizeRequests().antMatchers("/updatereport/{reportId}").hasAnyAuthority("EMPLOYEE","FINANCE_ADMIN");
	 http.authorizeRequests().antMatchers("/hideexpense/{expId}").hasAnyAuthority("EMPLOYEE","FINANCE_ADMIN");
	 http.authorizeRequests().antMatchers("/hidereport/{reportId}").hasAnyAuthority("EMPLOYEE","FINANCE_ADMIN");
	 http.authorizeRequests().antMatchers("/getreportsindaterange").hasAnyAuthority("FINANCE_ADMIN");
	 http.authorizeRequests().antMatchers("/openpdf/export/{reportId}").hasAnyAuthority("EMPLOYEE","FINANCE_ADMIN");
	 http.authorizeRequests().antMatchers("/excel/allsubmissionsstatus").hasAnyAuthority("FINANCE_ADMIN");
	 http.authorizeRequests().antMatchers("/excel/categorybreakup").hasAnyAuthority("FINANCE_ADMIN");
	 http.authorizeRequests().antMatchers("/getreportssubmittedtouserindaterange").hasAnyAuthority("FINANCE_ADMIN");
	 }
	 
	 
}
