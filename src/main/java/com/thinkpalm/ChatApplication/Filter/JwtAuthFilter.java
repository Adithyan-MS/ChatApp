package com.thinkpalm.ChatApplication.Filter;

import com.thinkpalm.ChatApplication.Util.AppContext;
import com.thinkpalm.ChatApplication.Repository.TokenRepository;
import com.thinkpalm.ChatApplication.Repository.UserRepository;
import com.thinkpalm.ChatApplication.Service.JwtService;
import com.thinkpalm.ChatApplication.Service.UserInfoService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private UserRepository userRepository;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;
        if(authHeader!=null && authHeader.startsWith("Bearer ")){
            token=authHeader.substring(7);
            username=jwtService.extractUsername(token);
        }

        if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){
            UserDetails userDetails = userInfoService.loadUserByUsername(username);
            boolean isTokenValid = tokenRepository.findByToken(token)
                    .map(t->!t.isExpired() && !t.isRevoked())
                    .orElse(false);
            if(jwtService.validateToken(token,userDetails) && isTokenValid){
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                AppContext.setUserName(username);
            }
        }
        try{
            filterChain.doFilter(request,response);
        }finally{
            AppContext.clearContext();
        }

    }
}
