package com.photoApp.api.zuul.security;


import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import io.jsonwebtoken.Jwts;

public class AuthorizationFilter extends BasicAuthenticationFilter {
    
	Environment environment;

    public AuthorizationFilter(AuthenticationManager authManager, Environment environment) {
        super(authManager);
        this.environment = environment;
    }
    
    
    @Override
    protected void doFilterInternal(HttpServletRequest req,
            HttpServletResponse res,
            FilterChain chain) throws IOException, ServletException {

        String authorizationHeader = req.getHeader(environment.getProperty("authorization.token.header.name"));

        if (authorizationHeader == null || !authorizationHeader.startsWith(environment.getProperty("authorization.token.header.prefix"))) {
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
       
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }  
    
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest req) {
        String authorizationHeader = req.getHeader(environment.getProperty("authorization.token.header.name"));
   
         if (authorizationHeader == null) {
             return null;
         }

         String token = authorizationHeader.replace(environment.getProperty("authorization.token.header.prefix"), "");

         String userId = Jwts.parser()
                 .setSigningKey(environment.getProperty("token.secret"))
                 .parseClaimsJws(token)
                 .getBody()
                 .getSubject();

         if (userId == null) {
             return null;
         }
   
         return new UsernamePasswordAuthenticationToken(userId, null, new ArrayList<>());

     }
}



//import java.io.IOException;
//import java.util.ArrayList;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.core.env.Environment;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
//
//import io.jsonwebtoken.Jwts;
//
//public class AuthorizationFilter extends BasicAuthenticationFilter {
//
//	
//	Environment environment;
//
//    public AuthorizationFilter(AuthenticationManager authManager, Environment environment) {
//        super(authManager);
//        this.environment = environment;
//    }
//    
//    
//    @Override
//    protected void doFilterInternal(HttpServletRequest req,
//            HttpServletResponse res,
//            FilterChain chain) throws IOException, ServletException {
//
//        String authorizationHeader = req.getHeader(environment.getProperty("authorization.token.header.name"));
//
//        if (authorizationHeader == null || !authorizationHeader.startsWith(environment.getProperty("authorization.token.header.prefix"))) {
//            chain.doFilter(req, res);
//            return;
//        }
//
//        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
//       
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        chain.doFilter(req, res);
//    }  
//    
//    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest req) {
//        String authorizationHeader = req.getHeader(environment.getProperty("authorization.token.header.name"));
//   
//         if (authorizationHeader == null) {
//             return null;
//         }
//
//         String token = authorizationHeader.replace(environment.getProperty("authorization.token.header.prefix"), "");
//
//         String userId = Jwts.parser()
//                 .setSigningKey(environment.getProperty("token.secret"))
//                 .parseClaimsJws(token)
//                 .getBody()
//                 .getSubject();
//
//         if (userId == null) {
//             return null;
//         }
//   
//         return new UsernamePasswordAuthenticationToken(userId, null, new ArrayList<>());
//
//     }
//	
//	
//	
//	
	
	
	
	
	
	
	
	
//	private Environment environment;
//
//	public AuthorizationFilter(AuthenticationManager authenticationManager, 
//								Environment environment) {
//		super(authenticationManager);
//		this.environment = environment;
//	}
//
//	@Override
//	protected void doFilterInternal(HttpServletRequest request,
//									HttpServletResponse response, 
//									FilterChain chain) 	throws IOException, ServletException {
//		
//		String authorizationHeader = request.getHeader(environment.getProperty("authorization.token.header.name"));
//
//		if (authorizationHeader == null
//				|| !authorizationHeader.startsWith(environment.getProperty("authorization.token.header.prefix"))) {
//			chain.doFilter(request, response);
//			return;
//		}
//
//		UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
//
//		SecurityContextHolder.getContext().setAuthentication(authentication);
//		chain.doFilter(request, response);
//	}
//
//	
//	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
//		
//		String authorizationHeader = request.getHeader(environment.getProperty("authorization.token.header.name"));
//
//		if (authorizationHeader == null) {
//			return null;
//		}
//
//		// I replace the prefix "Bearer" token with empty string 
//		// So we have now a clean authorization token
//		String token = authorizationHeader.replace(environment.getProperty("authorization.token.header.prefix"), "");
//
//		/*
//		 * I need to set the signingKey
//		 * The same signingKey which was used in the "token.secret" value from the users micro service
//		 */
//		String userId = Jwts.parser()
//				.setSigningKey(environment.getProperty("token.secret"))
//				.parseClaimsJws(token)
//				.getBody()
//				.getSubject();
//		
//		if (userId == null) {
//			return null;
//		}
//
//		return new UsernamePasswordAuthenticationToken(userId, null, new ArrayList<>());
//
//	}
//
//}
