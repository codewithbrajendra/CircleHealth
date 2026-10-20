package com.circlesHealth.admin.controller;

import com.circlesHealth.admin.dto.LoginDto;
import com.circlesHealth.admin.dto.UserDto;
import com.circlesHealth.admin.form.AdminLoginForm;
import com.circlesHealth.admin.form.PatientLoginForm;
import com.circlesHealth.admin.response.AnalyticsUserRequestData;
import com.circlesHealth.admin.service.AuthService;
import com.circlesHealth.admin.utils.DateUtil;
import com.circlesHealth.admin.utils.constants.EndPoints;
import com.circlesHealth.admin.utils.constants.ResponseMessage;
import com.circlesHealth.admin.utils.enums.ResponseStatus;
import com.circlesHealth.admin.wrapper.BaseResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import java.util.Collections;

@RestController
@RequestMapping(EndPoints.AUTH_PATH)
public class AuthenticationController {

    @Autowired
    private AuthService authService;

    @CrossOrigin("*")
    @PostMapping("/admin-login")
    @ApiOperation(value = "Public api to login admin with credentials. Return accessToken, Refresh Token,"
            + "Session Id and User Details. The information needs to be stored in client side."
            + "The authentication uses Bearer token schema using JWT.")
    public ResponseEntity<BaseResponse> adminLogin(@RequestBody @Valid AdminLoginForm adminLoginForm) {
        LoginDto loginDto = authService.adminLogin(adminLoginForm).orElseThrow(() ->
                new HttpServerErrorException(HttpStatus.UNAUTHORIZED,
                        ResponseMessage.INCORRECT_USERNAME_ACCESS_CODE));
        return ResponseEntity.status(HttpStatus.OK).body(
                new BaseResponse(HttpStatus.OK.value(),
                        ResponseStatus.SUCCESS.value(), ResponseMessage.LOGIN_SUCCESS,
                        DateUtil.getCurrentDateTime(), Collections.emptyList(), loginDto));
    }

    @CrossOrigin("*")
    @PostMapping("/patient-login")
    @ApiOperation(value = "Public api to login patient with credentials. Return accessToken, Refresh Token,"
            + "Session Id and User Details. The information needs to be stored in client side."
            + "The authentication uses Bearer token schema using JWT.")
    public ResponseEntity<BaseResponse> patientLogin(@RequestBody @Valid PatientLoginForm patientLoginForm) {
        LoginDto loginDto = authService.patientLogin(patientLoginForm).orElseThrow(() ->
                new HttpServerErrorException(HttpStatus.UNAUTHORIZED,
                        ResponseMessage.INCORRECT_ACCESS_CODE));
        UserDto userDto = loginDto.getUserDto();
        AnalyticsUserRequestData analyticsUserRequestData = this.authService.getAnalyticsUserRequestData(userDto.getUserId());
        userDto.setDob(analyticsUserRequestData.getDob());
        userDto.setEthnicity(analyticsUserRequestData.getEthnicity());
        userDto.setGender(analyticsUserRequestData.getGender());
        loginDto.setUserDto(userDto);
        return ResponseEntity.status(HttpStatus.OK).body(
                new BaseResponse(HttpStatus.OK.value(),
                        ResponseStatus.SUCCESS.value(), ResponseMessage.LOGIN_SUCCESS,
                        DateUtil.getCurrentDateTime(), Collections.emptyList(), loginDto));
    }
    
    @PatchMapping("/admin-logout")
    @ApiOperation(value = "Api for logging out.")
    public ResponseEntity<BaseResponse> adminLogout(@RequestHeader("Authorization") @NotBlank String token,
                                               @RequestHeader("SessionId") @NotBlank String sessionId) {
        authService.deactivateUserSession(token, sessionId);
        return ResponseEntity.ok(new BaseResponse(HttpStatus.OK.value(),
                ResponseStatus.SUCCESS.value(),
                ResponseMessage.LOGOUT_SUCCESS,
                DateUtil.getCurrentDateTime(),
                Collections.emptyList(),
                null));
    }
    
    @PatchMapping("/patient-logout")
    @ApiOperation(value = "Api for logging out.")
    public ResponseEntity<BaseResponse> patientLogout(@RequestHeader("Authorization") @NotBlank String token,
                                               @RequestHeader("SessionId") @NotBlank String sessionId) {
        authService.deactivateUserSession(token, sessionId);
        return ResponseEntity.ok(new BaseResponse(HttpStatus.OK.value(),
                ResponseStatus.SUCCESS.value(),
                ResponseMessage.LOGOUT_SUCCESS,
                DateUtil.getCurrentDateTime(),
                Collections.emptyList(),
                null));
    }
}