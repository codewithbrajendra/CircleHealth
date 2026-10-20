package com.circlesHealth.admin.service;

import com.circlesHealth.admin.dto.LoginDto;
import com.circlesHealth.admin.form.AdminLoginForm;
import com.circlesHealth.admin.form.PatientLoginForm;
import com.circlesHealth.admin.response.AnalyticsUserRequestData;

import java.util.Optional;

public interface AuthService {

    Optional<LoginDto> adminLogin(AdminLoginForm adminLoginForm);

    Optional<LoginDto> patientLogin(PatientLoginForm patientLoginForm);
    
    void deactivateUserSession(String token, String uniqueSessionId);
    
    AnalyticsUserRequestData getAnalyticsUserRequestData(Long userId);
}