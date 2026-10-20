package com.circles.health.patient.security;

import com.circles.health.patient.utils.RestTemplateUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AppUserDetailsService implements UserDetailsService {

    @Autowired
    RestTemplate restTemplate;

    @Value("${auth.service.base.api.url}")
    String authServiceUrl;

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenFilter.class);

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String s) {

        Object object = RestTemplateUtil.getMethod(
                authServiceUrl + "/user/userDetail?userUniqueId=" + s, restTemplate);
        AppUserDetail userDetails =  null;
        ObjectMapper objectMapper = new ObjectMapper();
        String objectString;
		try {
			objectString = objectMapper.writeValueAsString(object);
			 userDetails = objectMapper.readValue(objectString, AppUserDetail.class);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        

        return (UserDetails) userDetails;
    }

}