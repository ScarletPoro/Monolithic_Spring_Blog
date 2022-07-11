package it.cgmconsulting.myblogc9.security;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import it.cgmconsulting.myblogc9.repository.UserRepository;


public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

/*
    @Autowired
    private UserDetailsService userDetailsService;
    */
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.header}")
    private String tokenHeader;
    
   
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        	
    	String authToken = request.getHeader(this.tokenHeader);

        UserDetails userDetails = null;

        if(authToken != null){
            userDetails = jwtTokenUtil.getUserDetails(authToken);
        }

        // ctrl if user is banned. The ctrl doesen't start if the call is 'public'
        if(authToken != null && userDetails != null){
	        if(!userRepository.findByUsername(userDetails.getUsername()).get().isEnabled()) {
	        	userDetails = null;
			}
        }
        
        
        if (userDetails != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // controllo integrita' token
            if (jwtTokenUtil.validateToken(authToken, userDetails)) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        chain.doFilter(request, response);
    }
}