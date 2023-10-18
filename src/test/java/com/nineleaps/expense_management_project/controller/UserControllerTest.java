package com.nineleaps.expense_management_project.controller;

import static com.nineleaps.expense_management_project.config.JwtUtil.ACCESS_TOKEN_EXPIRATION_TIME;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.json.simple.JSONObject;
import org.springframework.http.HttpHeaders;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nineleaps.expense_management_project.config.JwtUtil;
import com.nineleaps.expense_management_project.dto.UserDTO;
import com.nineleaps.expense_management_project.entity.Employee;
import com.nineleaps.expense_management_project.service.EmployeeServiceImpl;
import com.nineleaps.expense_management_project.service.IEmailService;
import com.nineleaps.expense_management_project.service.IEmployeeService;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.Response;
import org.apache.struts.mock.MockHttpServletRequest;
import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {UserController.class})
@ExtendWith(SpringExtension.class)
class UserControllerTest {

    @MockBean
    private IEmailService iEmailService;

    @MockBean
    private IEmployeeService iEmployeeService;

    @MockBean
    private JwtUtil jwtUtil;
    private MockMvc mockMvc;

    @Autowired
    private UserController userController;



    @Test
    void testGetAllUserDetails() throws Exception {
        // Arrange
        when(iEmployeeService.getAllUser()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/listTheUser");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }


//    @Test
//    void testInsertUser() throws Exception {
//        // Arrange
//        Employee employee = new Employee();
//        employee.setEmployeeEmail("jane.doe@example.org");
//        employee.setEmployeeId(1L);
//        employee.setExpenseList(new ArrayList<>());
//        employee.setFirstName("Jane");
//        employee.setHrEmail("jane.doe@example.org");
//        employee.setHrName("Hr Name");
//        employee.setImageUrl("https://example.org/example");
//        employee.setIsFinanceAdmin(true);
//        employee.setIsHidden(true);
//        employee.setLastName("Doe");
//        employee.setLndEmail("jane.doe@example.org");
//        employee.setLndName("Lnd Name");
//        employee.setManagerEmail("jane.doe@example.org");
//        employee.setManagerName("Manager Name");
//        employee.setMiddleName("Middle Name");
//        employee.setMobileNumber(1L);
//        employee.setOfficialEmployeeId("42");
//        employee.setRole("Role");
//        employee.setToken("ABC123");
//
//        Employee employee2 = new Employee();
//        employee2.setEmployeeEmail("jane.doe@example.org");
//        employee2.setEmployeeId(1L);
//        employee2.setExpenseList(new ArrayList<>());
//        employee2.setFirstName("Jane");
//        employee2.setHrEmail("jane.doe@example.org");
//        employee2.setHrName("Hr Name");
//        employee2.setImageUrl("https://example.org/example");
//        employee2.setIsFinanceAdmin(true);
//        employee2.setIsHidden(true);
//        employee2.setLastName("Doe");
//        employee2.setLndEmail("jane.doe@example.org");
//        employee2.setLndName("Lnd Name");
//        employee2.setManagerEmail("jane.doe@example.org");
//        employee2.setManagerName("Manager Name");
//        employee2.setMiddleName("Middle Name");
//        employee2.setMobileNumber(1L);
//        employee2.setOfficialEmployeeId("42");
//        employee2.setRole("Role");
//        employee2.setToken("ABC123");
//
//        Employee employee3 = new Employee();
//        employee3.setEmployeeEmail("jane.doe@example.org");
//        employee3.setEmployeeId(1L);
//        employee3.setExpenseList(new ArrayList<>());
//        employee3.setFirstName("Jane");
//        employee3.setHrEmail("jane.doe@example.org");
//        employee3.setHrName("Hr Name");
//        employee3.setImageUrl("https://example.org/example");
//        employee3.setIsFinanceAdmin(true);
//        employee3.setIsHidden(true);
//        employee3.setLastName("Doe");
//        employee3.setLndEmail("jane.doe@example.org");
//        employee3.setLndName("Lnd Name");
//        employee3.setManagerEmail("jane.doe@example.org");
//        employee3.setManagerName("Manager Name");
//        employee3.setMiddleName("Middle Name");
//        employee3.setMobileNumber(1L);
//        employee3.setOfficialEmployeeId("42");
//        employee3.setRole("Role");
//        employee3.setToken("ABC123");
//        when(iEmployeeService.updateUser(Mockito.<UserDTO>any())).thenReturn(employee3);
//        when(iEmployeeService.findByEmailId(Mockito.<String>any())).thenReturn(employee);
//        when(iEmployeeService.insertUser(Mockito.<UserDTO>any())).thenReturn(employee2);
//        when(jwtUtil.generateTokens(Mockito.<String>any(), Mockito.<Long>any(), Mockito.<String>any(),
//                Mockito.<HttpServletResponse>any())).thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));
//
//        UserDTO userDTO = new UserDTO();
//        userDTO.setEmployeeEmail("jane.doe@example.org");
//        userDTO.setFcmToken("ABC123");
//        userDTO.setFirstName("Jane");
//        userDTO.setImageUrl("https://example.org/example");
//        userDTO.setLastName("Doe");
//        userDTO.setMiddleName("Middle Name");
//        String content = (new ObjectMapper()).writeValueAsString(userDTO);
//        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/theProfile")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(content);
//
//        // Act
//        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController)
//                .build()
//                .perform(requestBuilder);
//
//        // Assert
//        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(100));
//    }


//    @Test
//    void testGetAllUserDetails2() throws Exception {
//        // Arrange
//        when(iEmployeeService.getAllUser()).thenReturn(new ArrayList<>());
//        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/listTheUser");
//        requestBuilder.characterEncoding("Encoding");
//
//        // Act and Assert
//        MockMvcBuilders.standaloneSetup(userController)
//                .build()
//                .perform(requestBuilder)
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
//                .andExpect(MockMvcResultMatchers.content().string("[]"));
//    }

    @Test
    void testSendDataWithValidToken() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        String validToken = generateToken("employee@example.com",1L,"EMPLOYEE",ACCESS_TOKEN_EXPIRATION_TIME); // Replace with a valid token
        String validEmail = "employee@example.com";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);
        DecodedJWT decodedJWT = mock(DecodedJWT.class);
        when(decodedJWT.getSubject()).thenReturn(validEmail);


        Employee expectedEmployee = new Employee();
        expectedEmployee.setEmployeeId(1L);
        expectedEmployee.setEmployeeEmail(validEmail);
        expectedEmployee.setFirstName("John");
        expectedEmployee.setLastName("Doe");
        expectedEmployee.setImageUrl("image.jpg");

        when(iEmployeeService.findByEmailId(validEmail)).thenReturn(expectedEmployee);

        ResponseEntity<JSONObject> responseEntity = userController.sendData(request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        JSONObject responseBody = responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals(1L, responseBody.get("employeeId"));
        assertEquals("John", responseBody.get("firstName"));
        assertEquals("Doe", responseBody.get("lastName"));
        assertEquals("image.jpg", responseBody.get("imageUrl"));
        assertEquals(validEmail, responseBody.get("email"));
    }

    @Test
    void testSendDataWithMissingAuthorizationHeader() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getHeader("Authorization")).thenReturn(null);

        ResponseEntity<JSONObject> responseEntity = userController.sendData(request);

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }

    @Test
    void testRegenerateTokenWithValidToken() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        String validToken =  generateToken("employee@example.com",1L,"EMPLOYEE",ACCESS_TOKEN_EXPIRATION_TIME); // Replace with a valid token
        String validEmail = "employee@example.com";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);
        when(jwtUtil.isRefreshTokenExpired(validToken)).thenReturn(false);

        DecodedJWT decodedJWT = mock(DecodedJWT.class);
        when(decodedJWT.getSubject()).thenReturn(validEmail);

        Employee expectedEmployee = new Employee();
        expectedEmployee.setEmployeeEmail(validEmail);
        expectedEmployee.setEmployeeId(1L);
        expectedEmployee.setRole("EMPLOYEE");

        when(iEmployeeService.findByEmailId(validEmail)).thenReturn(expectedEmployee);

        String expectedAccessToken = "newAccessToken"; // Replace with the expected access token
        when(jwtUtil.generateToken(validEmail, 1L, "EMPLOYEE", jwtUtil.ACCESS_TOKEN_EXPIRATION_TIME)).thenReturn(expectedAccessToken);

        userController.regenerateToken(request, response);

        verify(response).setHeader("Access_Token", expectedAccessToken);
    }

    @Test
    void testRegenerateTokenWithExpiredToken() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        String expiredToken = "expiredToken"; // Replace with an expired token

        when(request.getHeader("Authorization")).thenReturn("Bearer " + expiredToken);
        when(jwtUtil.isRefreshTokenExpired(expiredToken)).thenReturn(true);

        userController.regenerateToken(request, response);

        // Verify that the response is not modified in this case (no Access_Token header is set)
        verify(response, never()).setHeader(eq("Access_Token"), anyString());
    }

    @Test
    void testInsertUserNewUser() throws MessagingException {
        UserDTO newUserDTO = new UserDTO();
        newUserDTO.setEmployeeEmail("newuser@example.com");
        String validToken =  generateToken("employee@example.com",1L,"EMPLOYEE",ACCESS_TOKEN_EXPIRATION_TIME);

        Employee existingEmployee = null;

        when(iEmployeeService.findByEmailId(newUserDTO.getEmployeeEmail())).thenReturn(existingEmployee);

        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        JwtUtil.TokenResponse tokenResponse = new JwtUtil.TokenResponse(validToken,validToken);

        ResponseEntity<JwtUtil.TokenResponse> responseEntity= ResponseEntity.ok(tokenResponse);
        when(jwtUtil.generateTokens(newUserDTO.getEmployeeEmail(),1L, "EMPLOYEE", mockResponse)).thenReturn(responseEntity);
        userController.insertUser(newUserDTO, mockResponse);

        verify(iEmployeeService).insertUser(newUserDTO); // Ensure insertUser method is called


        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testInsertUserExistingUser() throws MessagingException {
        UserDTO existingUserDTO = new UserDTO();
        existingUserDTO.setEmployeeEmail("existinguser@example.com");
        HttpServletResponse response = mock(HttpServletResponse.class);
        Employee existingEmployee = new Employee(); // An existing employee with the same email
        existingEmployee.setEmployeeId(1L);
        existingEmployee.setEmployeeEmail("existinguser@example.com");
        when(iEmployeeService.findByEmailId(existingUserDTO.getEmployeeEmail())).thenReturn(existingEmployee);

        jwtUtil.generateTokens(existingEmployee.getEmployeeEmail(),existingEmployee.getEmployeeId(), "EMPLOYEE", response);

        ResponseEntity<JwtUtil.TokenResponse> responseEntity = userController.insertUser(existingUserDTO, mock(HttpServletResponse.class));

        verify(iEmployeeService).updateUser(existingUserDTO); // Ensure updateUser method is called

        assertEquals("existinguser@example.com", existingEmployee.getEmployeeEmail());
    }


//
    private String generateToken(String emailId, Long employeeId, String role, long expirationTime) {
        return Jwts.builder()
                .setSubject(emailId)
                .claim("EmployeeID", employeeId)
                .claim("Role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, generateRandomSecretKey())
                .compact();
    }

    private static String generateRandomSecretKey() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] keyBytes = new byte[32]; // 256 bits
        secureRandom.nextBytes(keyBytes);

        // Convert the random bytes to a hexadecimal string
        StringBuilder keyBuilder = new StringBuilder();
        for (byte b : keyBytes) {
            keyBuilder.append(String.format("%02x", b));
        }

        return keyBuilder.toString();
    }

}

