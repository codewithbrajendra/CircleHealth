package com.circlesHealth.admin.service;

import java.io.ByteArrayInputStream;
import java.io.File;

import com.circlesHealth.admin.dto.PatientUserDto;
import com.circlesHealth.admin.entity.User;
import com.circlesHealth.admin.form.add.AddUserForm;
import com.circlesHealth.admin.response.PatientDetailResponse;
import com.circlesHealth.admin.response.PatientDetailResponseForFile;

public interface UserService {

    Object addUser(AddUserForm addUserForm, String token, String sessionId) throws Exception;
    
    String changeAccessCode(Long userId, String oldAccessCode,String newAccessCode);
    
    User findByEmail(String email);
    
    Object createUser(PatientUserDto patientDto, String token, String userUniqueId) throws Exception;
    
    User saveUser(PatientUserDto patientDto);
    
    User editUser(PatientUserDto patientDto);
    
    User savePatientId(User savedUser,Long patientId);
    
    Object editUser(PatientUserDto patientDto, String token, String userUniqueId) throws Exception;
    
    Object deleteUser(Long patientId, String token, String userUniqueId) throws Exception;
    
    void deleteByPatientId(Long patientId);

	Object getPatientWellnessCheckDate(Long userId, String token, String userUniqueId);

	Object getUserQuestionDetails(Long userId, Long wellnessId, String token, String userUniqueId);
    
	Object getPatientProfileDetails(Long userId, String token, String userUniqueId);
	
	PatientDetailResponseForFile getUserQuestionDetailsForPdfGeneration(Long userId);

	File generateQuestionDetailsPdf(PatientDetailResponseForFile patientDetailResponse);
	
	ByteArrayInputStream generateQuestionDetailsCSV(PatientDetailResponseForFile patientDetailResponse);
}
