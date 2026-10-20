package com.circlesHealth.admin.security;

import com.circlesHealth.admin.repo.UserRepository;
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

    @Autowired
    private UserRepository userRepository;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        LOGGER.info("Request uri: {}", ((HttpServletRequest) servletRequest).getRequestURI());

        String headerValue = ((HttpServletRequest) servletRequest).getHeader("Authorization");
        LOGGER.info("Authorization in header value : "+headerValue);
        Optional<String> token = jwtProvider.getBearerToken(headerValue);
        LOGGER.info("token value from getBearerToken method : "+token);
        if (token.isPresent() && isUserUniqueIdPresent(token.get())) {
        	LOGGER.info("Inside token isPresent condition.");
            Optional<UserDetails> details = appUserDetailsService.loadUserByJwtTokenAndDatabase(token.get());
            LOGGER.info("details value from loadUserByJwtTokenAndDatabase : "+details);
            if (details.isPresent()) {
            	LOGGER.info("Inside details isPresent condition.");
                UserDetails userDetails = details.get();
                PreAuthenticatedAuthenticationToken preAuthenticatedAuthenticationToken =
                        new PreAuthenticatedAuthenticationToken(userDetails, userDetails.getAuthorities());

                preAuthenticatedAuthenticationToken.setAuthenticated(true);
                SecurityContextHolder.getContext().setAuthentication(preAuthenticatedAuthenticationToken);
                LOGGER.info("preAuthenticatedAuthenticationToken done.");
            }
        }
        LOGGER.info("before doFilter method call.");
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private boolean isUserUniqueIdPresent(String token) {
        LOGGER.info("check user unique id is present or not");

        if (jwtProvider.isValidToken(token)) {
            String userUniqueId = jwtProvider.getUserSubject(token);
            LOGGER.info("userUniqueId : " + userUniqueId);
            if (userRepository.existsByUserUniqueId(userUniqueId)) {
                LOGGER.info("user present");
                return true;
            } else {
                LOGGER.info("user not present");
                return false;
            }
        } else {
            LOGGER.info("isUserUniqueIdPresent:" + false);
            return false;
        }
    }
}