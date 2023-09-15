package com.nineleaps.expensemanagementproject.controller;

import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nineleaps.expensemanagementproject.DTO.UserDTO;
import com.nineleaps.expensemanagementproject.service.IEmailService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.nineleaps.expensemanagementproject.config.JwtUtil;
import com.nineleaps.expensemanagementproject.entity.Employee;
import com.nineleaps.expensemanagementproject.service.IEmployeeService;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping()
public class UserController {
    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private IEmailService emailService;
    @Autowired
    private JwtUtil jwtUtil;
    JSONObject responseJson;

    public UserController(IEmployeeService employeeService) {

    }

    @GetMapping("/listTheUser")
    public List<Employee> getAllUserDetails() {
        return employeeService.getAllUser();
    }

    @SuppressWarnings("unchecked")
    @GetMapping("/getProfileData")
    public ResponseEntity<?> sendData(HttpServletRequest request) {
        String authorisationHeader = request.getHeader(AUTHORIZATION);
        String token = authorisationHeader.substring("Bearer ".length());
        DecodedJWT decodedAccessToken = JWT.decode(token);
        String employeeEmailFromToken = decodedAccessToken.getSubject();
        Employee employee1 = employeeService.findByEmailId(employeeEmailFromToken);
        Long employeeId = employee1.getEmployeeId();
        String email = employee1.getEmployeeEmail();
        String firstName = employee1.getFirstName();
        String lastName = employee1.getLastName();
        @SuppressWarnings("unused")
        String fullName = firstName + " " + lastName;
        String imageUrl = employee1.getImageUrl();
        JSONObject responseJson = new JSONObject();
        responseJson.put("employeeId", employeeId);
        responseJson.put("firstName", firstName);
        responseJson.put("lastName", lastName);
        responseJson.put("imageUrl", imageUrl);
        responseJson.put("email", email);
        return ResponseEntity.ok(responseJson);
    }

    @PostMapping("/theProfile")
    public ResponseEntity<JwtUtil.TokenResponse> insertUser(@RequestBody UserDTO userDTO, HttpServletResponse response)
            throws MessagingException {
        Employee employee = employeeService.findByEmailId(userDTO.getEmployeeEmail());
        if (employee == null) {
            employeeService.insertUser(userDTO);
            employee = employeeService.findByEmailId(userDTO.getEmployeeEmail());
            String email = employee.getEmployeeEmail();
            emailService.welcomeEmail(email);
            return jwtUtil.generateTokens(email, employee.getEmployeeId(), employee.getRole(), response);

        } else {
            String email = employee.getEmployeeEmail();
            employeeService.updateUser(userDTO);
            return jwtUtil.generateTokens(email, employee.getEmployeeId(), employee.getRole(), response);

        }
    }
}