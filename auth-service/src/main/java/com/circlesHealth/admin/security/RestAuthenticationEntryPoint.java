package com.circlesHealth.admin.security;

import com.circlesHealth.admin.utils.DateUtil;
import com.circlesHealth.admin.utils.constants.ResponseMessage;
import com.circlesHealth.admin.utils.enums.ResponseStatus;
import com.circlesHealth.admin.wrapper.BaseResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    public RestAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpServletResponse.getOutputStream().println(objectMapper.writeValueAsString(
                new BaseResponse(
                HttpStatus.UNAUTHORIZED.value(),
                ResponseStatus.FAILURE.value(),
                ResponseMessage.ACCESS_DENIED,
                DateUtil.getCurrentDateTime(),
                Collections.emptyList(),
                null
        )));
    }
}
