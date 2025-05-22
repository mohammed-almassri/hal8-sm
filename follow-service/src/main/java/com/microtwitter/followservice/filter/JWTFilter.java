package com.microtwitter.followservice.filter;


import com.microtwitter.followservice.util.JWTUtil;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();
        boolean excluded = false;
        if(!excluded){

            var token = extractTokenFromRequest(request);

            if (token != null && jwtUtil.validateToken(token)) {
                SecurityContextHolder.getContext().setAuthentication(jwtUtil.getAuthentication(token));
            }
            else{
                throw new JwtException("Invalid JWT token");
            }

        }
        filterChain.doFilter(request, response);
    }



    /**
     * Helper method
     *
     * @param request
     * @return
     */
    public String extractTokenFromRequest(HttpServletRequest request) {
        final String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            var token = authorizationHeader.substring(7);
            return token;
        }
        return null;
    }
}
