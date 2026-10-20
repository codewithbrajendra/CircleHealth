package com.circles.health.patient.service;

import java.time.LocalDate;
import java.util.List;

import com.circles.health.patient.entity.User;
import com.circles.health.patient.request.EditTherapistNameRequest;
import com.circles.health.patient.request.FilterUserRequest;
import com.circles.health.patient.response.AnalyticsUserRequestData;
import com.circles.health.patient.response.CreateEditUserResponse;
import com.circles.health.patient.response.PatientDetailResponse;
import com.circles.health.patient.response.PatientWellnessCheckDetails;
import com.circles.health.patient.response.ProfileDetailResponse;
import com.circles.health.patient.response.UserFormData;
import com.circles.health.patient.response.WellnessCheckDetailResponse;

public interface UserService {

	public CreateEditUserResponse createUser(User user);
	
	public User getUserByEmailAndDOB(String email, LocalDate dob);
	
    public List<User> getUsers();
	
	public CreateEditUserResponse editUser(User user);
	
	public String deleteUser(Long id);
	
	public String checkAccessCodeAvailabilty(String accessCode);
	
	public String checkEmailAvailabilty(String email);
	
	public User getUserById(Long id);
	
	public UserFormData addUserFormData();
	
	public User getByUserIdAndAccessCode(Long userId, String accessCode);
	
	public User getByUserIdAndEmail(Long userId, String email);

	public PatientDetailResponse fetchPatientDetails(Long userId, Long wellnessId);

	public List<PatientWellnessCheckDetails> getWellnessCheckDates(Long userId);
	
	public Object getBookingUrl(Long userId)  throws Exception;
	
	public Boolean isPatientProfileCompleted(Long userId);
	
	public String editTherapistName(EditTherapistNameRequest editTherapistNameRequest);

    public List<User> filterUsers(FilterUserRequest filterUserRequest);
    
    public WellnessCheckDetailResponse fetchWellnessCheckDetails(Long userId);
    
    public List<ProfileDetailResponse> fetchProfileDetails(Long userId);
	
    public PatientDetailResponse fetchPatientDetailsForPDF(Long userId);
    
    public Long getTherapistIdByEmail(String email);
    
    public User updateUser(User user);
    
    public AnalyticsUserRequestData getAnalyticsUserRequestData(Long userId);
    
    
}
