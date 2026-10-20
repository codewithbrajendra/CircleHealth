package com.circles.health.patient.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

@Component
public class JwtTokenFilter extends GenericFilterBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenFilter.class);

    @Autowired
    private AppUserDetailsService appUserDetailsService;

    @Autowired
    private JwtProvider jwtProvider;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        String headerValue = ((HttpServletRequest) servletRequest).getHeader("Authorization");
        String userUniqueId = ((HttpServletRequest) servletRequest).getHeader("UserUniqueId");

        LOGGER.info("Request uri: {}", ((HttpServletRequest) servletRequest).getRequestURI());

        Optional<String> token = jwtProvider.getBearerToken(headerValue);
        LOGGER.info("token value from getBearerToken method : "+token);

        if (token.isPresent() && userUniqueId != null &&
                !userUniqueId.isEmpty() && jwtProvider.isValidToken(token.get())) {
        	LOGGER.info("Inside token isPresent condition.");

            String tokenUserUniqueId = jwtProvider.getUserSubject(token.get());
            LOGGER.info("tokenUserUniqueId value from getUserSubject method : "+tokenUserUniqueId);
            if (tokenUserUniqueId.equals(userUniqueId)) {
            	LOGGER.info("Inside tokenUserUniqueId condition.");
                LOGGER.info("both uniqueIds same");

                UserDetails userDetails = appUserDetailsService.loadUserByUsername(userUniqueId);
                LOGGER.info("userDetails value from loadUserByUsername. : "+userDetails);
                PreAuthenticatedAuthenticationToken preAuthenticatedAuthenticationToken =
                        new PreAuthenticatedAuthenticationToken(userDetails, userDetails.getAuthorities());

                preAuthenticatedAuthenticationToken.setAuthenticated(true);

                SecurityContextHolder.getContext()
                        .setAuthentication(preAuthenticatedAuthenticationToken);
                LOGGER.info("preAuthenticatedAuthenticationToken done.");
            }
        }
        LOGGER.info("before doFilter method call.");
        filterChain.doFilter(servletRequest, servletResponse);
    }
}