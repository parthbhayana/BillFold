package com.nineleaps.expense_management_project.controller;

import java.util.List;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.nineleaps.expense_management_project.dto.UserDTO;
import com.nineleaps.expense_management_project.service.IEmailService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.nineleaps.expense_management_project.config.JwtUtil;
import com.nineleaps.expense_management_project.entity.Employee;
import com.nineleaps.expense_management_project.service.IEmployeeService;
import static com.nineleaps.expense_management_project.config.JwtUtil.ACCESS_TOKEN_EXPIRATION_TIME;
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


    @GetMapping("/listTheUser")
    public List<Employee> getAllUserDetails() {
        return employeeService.getAllUser();
    }

    @SuppressWarnings("unchecked")
    @GetMapping("/getProfileData")
    public ResponseEntity<JSONObject> sendData(HttpServletRequest request) {
        String authorisationHeader = request.getHeader(AUTHORIZATION);

        if (authorisationHeader == null) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authorisationHeader.substring("Bearer ".length());
        DecodedJWT decodedAccessToken = JWT.decode(token);
        String employeeEmailFromToken = decodedAccessToken.getSubject();
        Employee employee1 = employeeService.findByEmailId(employeeEmailFromToken);
        Long employeeId = employee1.getEmployeeId();
        String email = employee1.getEmployeeEmail();
        String firstName = employee1.getFirstName();
        String lastName = employee1.getLastName();
        String imageUrl = employee1.getImageUrl();
        JSONObject responsejson = new JSONObject();
        responsejson.put("employeeId", employeeId);
        responsejson.put("firstName", firstName);
        responsejson.put("lastName", lastName);
        responsejson.put("imageUrl", imageUrl);
        responsejson.put("email", email);
        return ResponseEntity.ok(responsejson);
    }

    @PostMapping("/theProfile")
    public ResponseEntity<JwtUtil.TokenResponse> insertUser(@RequestBody UserDTO userDTO, HttpServletResponse response)
            throws MessagingException {
        String email = userDTO.getEmployeeEmail(); // Get the email from the UserDTO

        Employee employee = employeeService.findByEmailId(email);

        if (employee == null) {
            employeeService.insertUser(userDTO);
            employee = employeeService.findByEmailId(email);
            if(employee == null){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }else{

                return jwtUtil.generateTokens(email, employee.getEmployeeId(), employee.getRole(), response);
            }

        } else {
            employeeService.updateUser(userDTO);
            return jwtUtil.generateTokens(email, employee.getEmployeeId(), employee.getRole(), response);
        }
    }


    @PostMapping("/regenerateToken")
    public void regenerateToken(HttpServletRequest request,HttpServletResponse response) {
        String authorisationHeader = request.getHeader(AUTHORIZATION);
        String token = authorisationHeader.substring("Bearer ".length());
        System.out.println("refresh token from header"+ token);
        if(!jwtUtil.isRefreshTokenExpired(token)){
            DecodedJWT decodedrefreshToken = JWT.decode(token);
            String employeeEmailFromToken = decodedrefreshToken.getSubject();
            Employee employee1 = employeeService.findByEmailId(employeeEmailFromToken);
            String AccessToken = jwtUtil.generateToken(employee1.getEmployeeEmail(),employee1.getEmployeeId(),employee1.getRole(),
                    ACCESS_TOKEN_EXPIRATION_TIME );
            response.setHeader("Access_Token",AccessToken);
        }else{
            System.out.println("refresh Token Expired. Login again");
        }


    }
}