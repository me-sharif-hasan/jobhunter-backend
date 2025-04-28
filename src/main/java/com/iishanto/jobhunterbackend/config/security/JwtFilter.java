package com.iishanto.jobhunterbackend.config.security;


import com.iishanto.jobhunterbackend.domain.model.SimpleUserModel;
import com.iishanto.jobhunterbackend.domain.usecase.GetUserUseCase;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

@Component
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final GetUserUseCase getUserByEmailUseCase;
    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        try{
            String authorizationHeader = request.getHeader("Authorization");
            System.out.println("Filtering: "+authorizationHeader+" :::: xx");
            String token;
            String email;
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                token = authorizationHeader.substring(7);
                if(!token.contains(".")) throw new IllegalArgumentException("Invalid token");
                email = jwtUtil.getUsernameFromToken(token);
                SimpleUserModel user = getUserByEmailUseCase.getUserByEmail(email);
                System.out.println("Email: "+email+" "+user.getEmail()+" "+user.getToken()+" "+user.getPassword()+" "+user.getRole());
                if(user.getEmail().equals(email)){
                    UserDetails userDetails = new org.springframework.security.core.userdetails.User(email, user.getPassword(), getGrantedAuthority());
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    System.out.println("Authenticated: "+email);
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        }catch (Exception ignore){}finally {
            ignore.printStackTrace();
            filterChain.doFilter(request, response);
        }
    }

    private Set<SimpleGrantedAuthority> getGrantedAuthority() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
    }
}
