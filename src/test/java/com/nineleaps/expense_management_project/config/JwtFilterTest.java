package com.nineleaps.expense_management_project.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import org.apache.catalina.connector.Response;
import org.apache.catalina.connector.ResponseFacade;
import org.apache.struts.mock.MockEnumeration;
import org.apache.struts.mock.MockHttpServletRequest;
import org.apache.struts.mock.MockHttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.support.StandardServletEnvironment;
import org.springframework.mock.web.MockFilterChain;
import static org.mockito.Mockito.*;



@ContextConfiguration(classes = {JwtFilter.class, JwtUtil.class})
@WebAppConfiguration
@ExtendWith(SpringExtension.class)
class JwtFilterTest {


    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    void testConstructor() {
        // Arrange and Act
        JwtFilter actualJwtFilter = new JwtFilter(jwtUtil);

        // Assert
        assertTrue(actualJwtFilter.getEnvironment() instanceof StandardServletEnvironment);
        assertNull(actualJwtFilter.getFilterConfig());
    }

    @Test
    void testDoFilterInternal() throws IOException, ServletException {
        // Arrange
        JwtFilter jwtFilter = new JwtFilter(new JwtUtil());
        MockHttpServletRequest request = new MockHttpServletRequest("https://example.org/example", "/theProfile",
                "https://example.org/example", "https://example.org/example");

        Response response = new Response();
        FilterChain chain = mock(FilterChain.class);
        doNothing().when(chain).doFilter(Mockito.<ServletRequest>any(), Mockito.<ServletResponse>any());

        // Act
        jwtFilter.doFilterInternal(request, response, chain);

        // Assert that nothing has changed
        verify(chain).doFilter(Mockito.<ServletRequest>any(), Mockito.<ServletResponse>any());
        assertTrue(request.getAttributeNames() instanceof MockEnumeration);
        assertTrue(request.getSession() instanceof MockHttpSession);
        assertEquals("/theProfile", request.getServletPath());
        assertEquals("https://example.org/example", request.getQueryString());
        assertEquals("https://example.org/example", request.getPathInfo());
        assertEquals("https://example.org/example", request.getContextPath());
        assertTrue(request.getParameterMap().isEmpty());
        HttpServletResponse response2 = response.getResponse();
        assertTrue(response2 instanceof ResponseFacade);
        assertSame(response.getOutputStream(), response2.getOutputStream());
        assertTrue(jwtFilter.getEnvironment() instanceof StandardServletEnvironment);
    }

    @Test
    void testDoFilterInternal2() throws IOException, ServletException {
        // Arrange
        JwtFilter jwtFilter = new JwtFilter(new JwtUtil());
        MockHttpServletRequest request = new MockHttpServletRequest("https://example.org/example", "/theProfile",
                "https://example.org/example", "https://example.org/example");

        Response response = new Response();
        FilterChain chain = mock(FilterChain.class);
        doThrow(new ServletException("An error occurred")).when(chain)
                .doFilter(Mockito.<ServletRequest>any(), Mockito.<ServletResponse>any());

        // Act and Assert
        assertThrows(ServletException.class, () -> jwtFilter.doFilterInternal(request, response, chain));
        verify(chain).doFilter(Mockito.<ServletRequest>any(), Mockito.<ServletResponse>any());
    }


    @Test
    void testDoFilterInternal3() throws IOException, ServletException {
        // Arrange
        JwtFilter jwtFilter = new JwtFilter(new JwtUtil());
        org.springframework.mock.web.MockHttpServletRequest request = new org.springframework.mock.web.MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Act
        jwtFilter.doFilterInternal(request, response, mock(FilterChain.class));

        // Assert
        assertTrue(response.isCommitted());
        assertEquals(403, response.getStatus());
        assertEquals("header was null", response.getErrorMessage());
    }

//    @Test
//    void testDoFilterInternal_ValidToken() throws Exception {
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        MockHttpServletResponse response = new MockHttpServletResponse();
//
//        request.setRequestURI("/someEndpoint");
//        request.addHeader("Authorization", "Bearer validToken");
//
//        when(mockJwtUtil.validateToken("validToken")).thenReturn(true);
//        when(mockJwtUtil.isAccessTokenExpired("validToken")).thenReturn(false);
//
//        jwtFilter.doFilterInternal(request, response, new MockFilterChain());
//
//        assertEquals(200, response.getStatus());
//    }
//
//
//    @Test
//    void testDoFilterInternal_InvalidToken() throws Exception {
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        MockHttpServletResponse response = new MockHttpServletResponse();
//
//        request.setRequestURI("/someEndpoint");
//        request.addHeader("Authorization", "Bearer invalidToken");
//
//        when(mockJwtUtil.validateToken("invalidToken")).thenReturn(false);
//
//        jwtFilter.doFilterInternal(request, response, new MockFilterChain());
//
//        assertEquals(401, response.getStatus());
//    }
//
//    @Test
//    void testDoFilterInternal_AccessTokenExpired() throws Exception {
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        MockHttpServletResponse response = new MockHttpServletResponse();
//
//        request.setRequestURI("/someEndpoint");
//        request.addHeader("Authorization", "Bearer expiredToken");
//
//        when(mockJwtUtil.validateToken("expiredToken")).thenReturn(true);
//        when(mockJwtUtil.isAccessTokenExpired("expiredToken")).thenReturn(true);
//
//        jwtFilter.doFilterInternal(request, response, new MockFilterChain());
//
//        assertEquals(401, response.getStatus());
//    }
//
//    @Test
//    void testDoFilterInternal_NoAuthorizationHeader() throws Exception {
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        MockHttpServletResponse response = new MockHttpServletResponse();
//
//        request.setRequestURI("/someEndpoint");
//
//        jwtFilter.doFilterInternal(request, response, new MockFilterChain());
//
//        assertEquals(403, response.getStatus());
//    }
//
//    @Test
//    void testDoFilterInternal_UnprotectedPath() throws Exception {
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        MockHttpServletResponse response = new MockHttpServletResponse();
//
//        request.setRequestURI("/theProfile");
//        request.addHeader("Authorization", "Bearer validToken");
//
//        jwtFilter.doFilterInternal(request, response, new MockFilterChain());
//
//        assertEquals(200, response.getStatus());
//    }
//
//    @Test
//    void testDoFilterInternal_UnprotectedPath_RegenerateToken() throws Exception {
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        MockHttpServletResponse response = new MockHttpServletResponse();
//
//        request.setRequestURI("/regenerateToken");
//        request.addHeader("Authorization", "Bearer validToken");
//
//        jwtFilter.doFilterInternal(request, response, new MockFilterChain());
//
//        assertEquals(200, response.getStatus());
//    }
}

