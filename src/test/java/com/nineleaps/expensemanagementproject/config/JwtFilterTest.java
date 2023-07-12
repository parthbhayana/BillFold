package com.nineleaps.expensemanagementproject.config;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JwtFilterTest {

    @Mock
    private JwtUtil jwtUtil;

    private JwtFilter jwtFilter;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        jwtFilter = new JwtFilter(jwtUtil);
    }

    @Test
    public void doFilterInternal_ValidToken_ShouldSetAuthenticationInSecurityContextHolder() throws ServletException, IOException {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        String token = "valid-token";
        String emailId = "test@example.com";
        String role = "ROLE_USER";

        Claims claims = createClaims(emailId, role);
        when(jwtUtil.validateToken(token)).thenReturn(true);
        when(jwtUtil.getClaimsFromToken(token)).thenReturn(claims);

        // Act
        request.addHeader("Authorization", "Bearer " + token);
        jwtFilter.doFilterInternal(request, response, filterChain);

        // Assert
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertEquals(emailId, authentication.getPrincipal());
        assertNull(authentication.getCredentials());
        assertEquals(1, authentication.getAuthorities().size());
        assertTrue(authentication.getAuthorities().contains(new SimpleGrantedAuthority(role)));

        verify(filterChain).doFilter(request, response);
    }

    @Test
    public void doFilterInternal_InvalidToken_ShouldNotSetAuthenticationInSecurityContextHolder() throws ServletException, IOException {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        String token = "invalid-token";

        when(jwtUtil.validateToken(token)).thenReturn(false);

        // Clear the authentication context
        SecurityContextHolder.clearContext();

        // Act
        request.addHeader("Authorization", "Bearer " + token);
        jwtFilter.doFilterInternal(request, response, filterChain);

        // Assert
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNull(authentication);

        verify(filterChain).doFilter(request, response);
    }

    private Claims createClaims(String emailId, String role) {
        Claims claims = mock(Claims.class);
        when(claims.getSubject()).thenReturn(emailId);
        when(claims.get("Role")).thenReturn(role);
        return claims;
    }
}

