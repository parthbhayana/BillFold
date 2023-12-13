package com.nineleaps.expensemanagementproject.controller;

 /**
   UserController

        This controller manages user-related operations, authentication, and profile retrieval.

        ## Endpoints

        ### GET /listTheUser

        - **Description:** Fetches details of all users.
        - **Returns:** A list of Employee objects containing user details.

        ### GET /getProfileData

        - **Description:** Retrieves profile data for the authenticated user.
        - **Returns:** JSON object containing the user's `employeeId`, `firstName`, `lastName`, `imageUrl`, and `email`.

        ### POST /theProfile

        - **Description:** Creates or updates a user profile based on the provided information.
        - **Request Body:** UserDTO object containing user details.
        - **Returns:** JWT TokenResponse object containing access and refresh tokens upon successful creation or update of the user profile.

        ### POST /regenerateToken

        - **Description:** Regenerates an access token using the refresh token.
        - **Request Header:** Bearer token in the authorization header.
        - **Response Header:** `Access_Token` containing the regenerated access token if successful.

        ## Note
        - Endpoints `/listTheUser` and `/getProfileData` require a valid authorization token in the header.
        - For `/theProfile`, if the user does not exist, it creates a new profile. If the user exists, it updates the existing profile.
        - `/regenerateToken` regenerates an access token using the refresh token if it's not expired.
*/

import java.util.List;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.nineleaps.expensemanagementproject.DTO.UserDTO;
import com.nineleaps.expensemanagementproject.service.IEmailService;
import lombok.extern.slf4j.Slf4j;
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
import com.nineleaps.expensemanagementproject.config.JwtUtil;
import com.nineleaps.expensemanagementproject.entity.Employee;
import com.nineleaps.expensemanagementproject.service.IEmployeeService;
import static com.nineleaps.expensemanagementproject.config.JwtUtil.ACCESS_TOKEN_EXPIRATION_TIME;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@Slf4j
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private IEmailService emailService;
    @Autowired
    private JwtUtil jwtUtil;


    @GetMapping("/listTheUser")
    public List<Employee> getAllUserDetails() {
        try {
            log.info("Fetching all user details...");
            List<Employee> userDetails = employeeService.getAllUser();
            log.info("User details fetched successfully");
            return userDetails;
        } catch (Exception e) {
            log.error("Error fetching user details: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch user details");
        }
    }

    @SuppressWarnings("unchecked")
    @GetMapping("/getProfileData")
    public ResponseEntity<JSONObject> sendData(HttpServletRequest request) {
        try {
            String authorisationHeader = request.getHeader("Authorization");

            if (authorisationHeader == null || !authorisationHeader.startsWith("Bearer ")) {
                log.error("Invalid or missing Authorization header");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            String token = authorisationHeader.substring("Bearer ".length());
            DecodedJWT decodedAccessToken = JWT.decode(token);
            String employeeEmailFromToken = decodedAccessToken.getSubject();
            Employee employee1 = employeeService.findByEmailId(employeeEmailFromToken);

            if (employee1 == null) {
                log.error("Employee not found for email: {}", employeeEmailFromToken);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            Long employeeId = employee1.getEmployeeId();
            String email = employee1.getEmployeeEmail();
            String firstName = employee1.getFirstName();
            String lastName = employee1.getLastName();
            String imageUrl = employee1.getImageUrl();
            JSONObject responseJson = new JSONObject();
            responseJson.put("employeeId", employeeId);
            responseJson.put("firstName", firstName);
            responseJson.put("lastName", lastName);
            responseJson.put("imageUrl", imageUrl);
            responseJson.put("email", email);

            return ResponseEntity.ok(responseJson);
        } catch (Exception e) {
            log.error("Error fetching profile data: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/theProfile")
    public ResponseEntity<JwtUtil.TokenResponse> insertUser(@RequestBody UserDTO userDTO, HttpServletResponse response)
            throws MessagingException {
        try {
            String email = userDTO.getEmployeeEmail(); // Get the email from the UserDTO

            Employee employee = employeeService.findByEmailId(email);

            if (employee == null) {
                employeeService.insertUser(userDTO);
                employee = employeeService.findByEmailId(email);
                if (employee == null) {
                    log.error("Failed to insert user: {}", email);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                } else {
                    return jwtUtil.generateTokens(email, employee.getEmployeeId(), employee.getRole(), response);
                }
            } else {
                employeeService.updateUser(userDTO);
                return jwtUtil.generateTokens(email, employee.getEmployeeId(), employee.getRole(), response);
            }
        } catch (Exception e) {
            log.error("Error inserting/updating user: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PostMapping("/regenerateToken")
    public void regenerateToken(HttpServletRequest request, HttpServletResponse response) {
        try {
            String authorisationHeader = request.getHeader(AUTHORIZATION);
            String token = authorisationHeader.substring("Bearer ".length());
            log.info("Refresh token from header: {}", token);

            if (!jwtUtil.isRefreshTokenExpired(token)) {
                DecodedJWT decodedRefreshToken = JWT.decode(token);
                String employeeEmailFromToken = decodedRefreshToken.getSubject();
                Employee employee1 = employeeService.findByEmailId(employeeEmailFromToken);
                String accessToken = jwtUtil.generateToken(employee1.getEmployeeEmail(), employee1.getEmployeeId(), employee1.getRole(), ACCESS_TOKEN_EXPIRATION_TIME);
                response.setHeader("Access_Token", accessToken);
            } else {
                log.info("Refresh Token Expired. Login again");
            }
        } catch (Exception e) {
            log.error("Error while regenerating token: {}", e.getMessage(), e);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
}