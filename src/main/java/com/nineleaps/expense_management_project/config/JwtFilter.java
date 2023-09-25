package com.nineleaps.expense_management_project.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.Claims;

public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        // Extract the Authorization header from the HTTP request
        String header = request.getHeader("Authorization");

        // Check if the header is not null and starts with "Bearer "
        if (header != null && header.startsWith("Bearer ")) {
            // Extract the JWT token (excluding "Bearer ") from the header
            String token = header.substring(7);

            // Validate the JWT token using the JwtUtil
            if (jwtUtil.validateToken(token)) {
                // If the token is valid, extract claims from the token
                Claims claims = jwtUtil.getClaimsFromToken(token);

                // Extract the email ID from the claims
                String emailId = claims.getSubject();

                // Extract the role from the claims
                String role = (String) claims.get("Role");

                // Check if the role is not null and not empty
                if (role != null && !role.isEmpty()) {
                    // Create a list of GrantedAuthority with the user's role
                    List<GrantedAuthority> authorities = new ArrayList<>();
                    authorities.add(new SimpleGrantedAuthority(role));

                    // Create an Authentication object using email ID, null for password, and authorities
                    Authentication authentication = new UsernamePasswordAuthenticationToken(emailId, null, authorities);

                    // Set the Authentication object in the security context
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        // Continue the filter chain
        chain.doFilter(request, response);
    }
}
