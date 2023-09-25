package com.nineleaps.expense_management_project.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import io.jsonwebtoken.Claims;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class JwtFilterTest {

    private JwtFilter jwtFilter;
    private JwtUtil jwtUtil;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        jwtUtil = mock(JwtUtil.class);
        jwtFilter = new JwtFilter(jwtUtil);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);
    }

    @Test
    void testDoFilterInternalValidToken() throws ServletException, IOException {
        // Mock a valid token and claims
        String validToken = "validToken";
        Claims claims = Mockito.mock(Claims.class);
        when(jwtUtil.validateToken(validToken)).thenReturn(true);
        when(jwtUtil.getClaimsFromToken(validToken)).thenReturn(claims);
        when(claims.getSubject()).thenReturn("test@example.com");
        when(claims.get("Role")).thenReturn("ROLE_USER");

        // Mock the Authorization header with the valid token
        when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);

        // Execute the filter
        jwtFilter.doFilterInternal(request, response, filterChain);

        // Verify that the Authentication object is set in the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertEquals("test@example.com", authentication.getPrincipal());
        assertNull(authentication.getCredentials());
        assertTrue(authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")));

        // Verify that the filter chain is continued
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testDoFilterInternalInvalidToken() throws ServletException, IOException {
        // Mock an invalid token
        String invalidToken = "invalidToken";
        when(jwtUtil.validateToken(invalidToken)).thenReturn(false);

        // Mock the Authorization header with the invalid token
        when(request.getHeader("Authorization")).thenReturn("Bearer " + invalidToken);

        // Execute the filter
        jwtFilter.doFilterInternal(request, response, filterChain);

        // Verify that no Authentication object is set in the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNull(authentication);

        // Verify that the filter chain is continued
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testDoFilterInternalMissingAuthorizationHeader() throws ServletException, IOException {
        // Mock a request without the Authorization header
        when(request.getHeader("Authorization")).thenReturn(null);

        // Execute the filter
        jwtFilter.doFilterInternal(request, response, filterChain);

        // Verify that no Authentication object is set in the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNull(authentication);

        // Verify that the filter chain is continued
        verify(filterChain).doFilter(request, response);
    }
}
