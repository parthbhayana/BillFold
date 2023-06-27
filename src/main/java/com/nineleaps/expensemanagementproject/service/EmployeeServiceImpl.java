package com.nineleaps.expensemanagementproject.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nineleaps.expensemanagementproject.entity.Employee;
import com.nineleaps.expensemanagementproject.repository.EmployeeRepository;

@Service
public class EmployeeServiceImpl implements IEmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public List<Employee> getAllEmployeeDetails() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee saveEmployeeDetails(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public Employee getEmployeeById(Long employeeId) {
        return employeeRepository.findById(employeeId).get();
    }

    @Override
    public void deleteEmployeeDetailsById(Long employeeId) {
        employeeRepository.deleteById(employeeId);
    }

    @Override
    public Employee updateEmployeeDetails(Employee newemployee, Long employeeId) {
        Employee employee = getEmployeeById(employeeId);
        employee.setEmployeeEmail(newemployee.getEmployeeEmail());
        employee.setFirstName(newemployee.getFirstName());
        employee.setLastName(newemployee.getLastName());
        employee.setMiddleName(newemployee.getMiddleName());
        return employeeRepository.save(employee);
    }

    @Override
    public void additionalEmployeeDetails(Long employeeId, String officialEmployeeId, String managerEmail, Long mobileNumber) {
        Employee employee = getEmployeeById(employeeId);
        employee.setOfficialEmployeeId(officialEmployeeId);
        employee.setManagerEmail(managerEmail);
        employee.setMobileNumber(mobileNumber);
        employeeRepository.save(employee);
    }

    @Override
    public Employee getEmployeeByEmail(String emailToVerify) {
        return null;
    }

    @Override
    public Employee getUserByEmail(String emailToVerify) {
        return null;
    }

    @Override
    public List<Employee> getAllUser() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee insertuser(Employee newUser) {
        return employeeRepository.save(newUser);
    }

    @Override
    public Employee findByEmailId(String emailId) {
        return employeeRepository.findByEmployeeEmail(emailId);
    }

    @Override
    public void hideEmployee(Long employeeId) {
        Boolean hidden = true;
        Employee employee = getEmployeeById(employeeId);
        employee.setIsHidden(hidden);
        employeeRepository.save(employee);
    }

    @Override
    public void isFinanceAdmin(Long employeeId) {
        Employee employee = getEmployeeById(employeeId);
        Boolean isAdmin = employee.getIsFinanceAdmin();
    }

    @Override
    public void setFinanceAdmin(Long employeeId) {
        Boolean isAdmin = true;
        String role = "FINANCE_ADMIN";
        Employee employee = getEmployeeById(employeeId);
        employee.setIsFinanceAdmin(isAdmin);
        employee.setRole(role);

        employeeRepository.save(employee);

        List<Employee> emp = employeeRepository.findAll();
        Boolean isAdmins = false;
        String roles = "EMPLOYEE";
        for (Employee emp1 : emp) {
            if (emp1.getEmployeeId() == employeeId)
                continue;
            emp1.setIsFinanceAdmin(isAdmins);
            emp1.setRole(roles);
            employeeRepository.save(emp1);
        }

    }


    @Override
    public Employee insertUser(Employee newUser) {
        return employeeRepository.save(newUser);

    }


}