package com.nineleaps.expensemanagementproject.filter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import org.springframework.web.filter.OncePerRequestFilter;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nineleaps.expensemanagementproject.config.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


public class UserAuthorizationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtil jwtutil;
	

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
	        throws ServletException, IOException {
	    if (request.getServletPath().equals("/theprofile")||request.getServletPath().equals("/swagger-ui/**")) {
	        filterChain.doFilter(request, response);
	    } else {
	        String authorizationHeader = request.getHeader(AUTHORIZATION);
	        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
	            try {
	                String token = authorizationHeader.substring("Bearer ".length());
	                System.out.println("access_token received "+token);
	                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
	                JWTVerifier verifier = JWT.require(algorithm).build();
	                DecodedJWT decodedJWT = verifier.verify(token);
	                String email = decodedJWT.getSubject();
	                String role = decodedJWT.getClaim("role").asString(); 

	                Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
	                authorities.add(new SimpleGrantedAuthority(role));

	                UsernamePasswordAuthenticationToken authenticationToken =
	                        new UsernamePasswordAuthenticationToken(email, null, authorities);
	                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

	                filterChain.doFilter(request, response);
	            } catch (Exception exception) {
	                exception.printStackTrace();
	                response.setStatus(FORBIDDEN.value());
	                Map<String, String> error = new HashMap<>();
	                error.put("error_message", exception.getMessage());
	                response.setContentType(APPLICATION_JSON_VALUE);
	                new ObjectMapper().writeValue(response.getOutputStream(), error);
	            }
	        } else {
	            response.setStatus(FORBIDDEN.value());
	            Map<String, String> error = new HashMap<>();
	            error.put("error_message", "Authorization token is missing or invalid");
	            response.setContentType(APPLICATION_JSON_VALUE);
	            new ObjectMapper().writeValue(response.getOutputStream(), error);
	        }
	    }
	}


}
