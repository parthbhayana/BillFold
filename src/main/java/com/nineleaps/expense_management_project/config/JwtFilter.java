package com.nineleaps.expense_management_project.config;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.NonNull;
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
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull  FilterChain chain)
            throws ServletException, IOException {
        String servletPath = request.getServletPath();
        if (isPublicPath(servletPath)) {
            chain.doFilter(request, response);
        } else {
            handleAuthorizationHeader(request, response, chain);
        }
    }

    private boolean isPublicPath(String servletPath) {
        return servletPath.equals("/theProfile") || servletPath.equals("/regenerateToken")|| servletPath.startsWith("/swagger")|| servletPath.equals("/v2/api-docs");
    }

    private void handleAuthorizationHeader(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            if (isValidToken(token)) {
                Claims claims = jwtUtil.getClaimsFromToken(token);
                String emailId = claims.getSubject();
                String role = (String) claims.get("Role");
                if (isValidRole(role)) {
                    Authentication authentication = createAuthentication(emailId, role);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    chain.doFilter(request, response);
                } else {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access token Expired");
                }
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access token Expired");
            }
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Header was null");
        }
    }

    private boolean isValidToken(String token) {
        return jwtUtil.validateToken(token) && !jwtUtil.isAccessTokenExpired(token);
    }

    private boolean isValidRole(String role) {
        return role != null && !role.isEmpty();
    }

    private Authentication createAuthentication(String emailId, String role) {
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(role));
        return new UsernamePasswordAuthenticationToken(emailId, null, authorities);
    }



}