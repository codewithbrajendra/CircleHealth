package com.circles.health.patient.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.circles.health.patient.dto.RequestParamDto;
import com.circles.health.patient.email.CircleHealthEmail;
import com.circles.health.patient.entity.User;
import com.circles.health.patient.request.EditTherapistNameRequest;
import com.circles.health.patient.request.FilterUserRequest;
import com.circles.health.patient.request.ForgottenAccessCodeRequest;
import com.circles.health.patient.response.AnalyticsUserRequestData;
import com.circles.health.patient.response.CreateEditUserResponse;
import com.circles.health.patient.response.PatientDetailResponse;
import com.circles.health.patient.response.PatientDetailResponseForFile;
import com.circles.health.patient.response.PatientProfileResponse;
import com.circles.health.patient.response.PatientWellnessCheckDetails;
import com.circles.health.patient.response.ProfileDetailResponse;
import com.circles.health.patient.response.UserFormData;
import com.circles.health.patient.response.WellnessCheckDetailResponse;
import com.circles.health.patient.service.PatientBookingService;
import com.circles.health.patient.service.UserService;
import com.circles.health.patient.service.WellnessCheckService;
import com.circles.health.patient.utils.DateUtil;
import com.circles.health.patient.utils.constants.EndPoints;
import com.circles.health.patient.utils.constants.ResponseMessage;
import com.circles.health.patient.utils.enums.ResponseStatus;
import com.circles.health.patient.wrapper.BaseResponse;
import com.circles.health.patient.wrapper.BaseResponseIsValid;


@RestController
@RequestMapping(EndPoints.BASE_PATH)
public class PatientController {

	private static final Logger LOGGER = LoggerFactory.getLogger(PatientController.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CircleHealthEmail circleHealthEmail;
	
	@Autowired
	private WellnessCheckService wellnessCheckService;

	@Autowired
	private PatientBookingService patientBookingService;
	
//	@PostMapping("/createUser")
//	public ResponseEntity<BaseResponse> createUser(@RequestBody User user) {
//		LOGGER.info("Patient User "+user.getFirstName()+" is to be create.");
//		User savedUser = null;
//		
//		try {
//		
//			savedUser = this.userService.createUser(user);
//		
//		}catch (Exception exception) {
//			LOGGER.error(ResponseMessage.USER_CREATED_ERROR+exception.getMessage());
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
//					new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),ResponseStatus.FAILURE.value(),
//						ResponseMessage.USER_CREATED_ERROR+exception.getMessage(),DateUtil.getCurrentDateTime(),
//					Collections.emptyList(), null));
//		}
//		return ResponseEntity.status(HttpStatus.OK).body(
//				new BaseResponse(HttpStatus.OK.value(),ResponseStatus.SUCCESS.value(),
//					ResponseMessage.USER_CREATED_SUCCESS,DateUtil.getCurrentDateTime(),
//				Collections.emptyList(), savedUser));
//	}
	
	@PostMapping("/createUser")
	public ResponseEntity<BaseResponse> createUser(@RequestBody User user) {
		LOGGER.info("Patient User "+user.getFirstName()+" is to be create.");
		CreateEditUserResponse createUserResponse = null;
		try {
		
			createUserResponse = this.userService.createUser(user);
		}catch (Exception exception) {
			LOGGER.error(ResponseMessage.USER_CREATED_ERROR+exception.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),ResponseStatus.FAILURE.value(),
						ResponseMessage.USER_CREATED_ERROR+exception.getMessage(),DateUtil.getCurrentDateTime(),
					Collections.emptyList(), null));
		}
		return ResponseEntity.status(HttpStatus.OK).body(
				new BaseResponse(HttpStatus.OK.value(),ResponseStatus.SUCCESS.value(),
					ResponseMessage.USER_CREATED_SUCCESS,DateUtil.getCurrentDateTime(),
				Collections.emptyList(), createUserResponse));
	}
	
	@PostMapping("/editUser")
	public ResponseEntity<BaseResponse> editUser(@RequestBody User user) {
		LOGGER.info("Patient User "+user.getFirstName()+" is to be edit.");
		CreateEditUserResponse createUserResponse = null;
		try {
		
			createUserResponse = this.userService.editUser(user);
		
		}catch(Exception exception) {
			LOGGER.error(ResponseMessage.USER_EDITED_ERROR+exception.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
						new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),ResponseStatus.FAILURE.value(),
							ResponseMessage.USER_EDITED_ERROR+exception.getMessage(),DateUtil.getCurrentDateTime(),
						Collections.emptyList(), null));
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(
				new BaseResponse(HttpStatus.OK.value(),ResponseStatus.SUCCESS.value(),
						ResponseMessage.USER_EDITED_SUCCESS,DateUtil.getCurrentDateTime(),
					Collections.emptyList(), createUserResponse));
	}
	
	@GetMapping("/deleteUser/{id}")
	public ResponseEntity<BaseResponse> deleteUser(@PathVariable("id") Long id) {
		LOGGER.info("Patient User id "+id+" is to be delete.");
		try {
		
			String deletedUserMsg = this.userService.deleteUser(id);
		
		}catch(Exception exception) {
			LOGGER.error(ResponseMessage.USER_DELETED_ERROR+exception.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),ResponseStatus.FAILURE.value(),
				ResponseMessage.USER_DELETED_ERROR+exception.getMessage(),DateUtil.getCurrentDateTime(),
					Collections.emptyList(), null));
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(
				new BaseResponse(HttpStatus.OK.value(),ResponseStatus.SUCCESS.value(),
						ResponseMessage.USER_DELETED_SUCCESS,DateUtil.getCurrentDateTime(),
					Collections.emptyList(), null));
	}
	
	@PostMapping("/checkAccessCode")
	public ResponseEntity<BaseResponseIsValid> checkAccessCode(@RequestBody RequestParamDto requestParamDto) {
		LOGGER.info("AccessCode from request ::: "+requestParamDto.getAccessCode());
		  try {
			  if(requestParamDto.getAccessCode() == null || requestParamDto.getAccessCode().equals("")) {
				  return ResponseEntity.status(HttpStatus.OK).body(
							new BaseResponseIsValid(HttpStatus.OK.value(),ResponseStatus.SUCCESS.value(),Boolean.FALSE,
								ResponseMessage.ACCESS_CODE_NOT_BLANK,DateUtil.getCurrentDateTime(),
									Collections.emptyList(), null));
			  }
			  User accessCodeExist = this.userService.getByUserIdAndAccessCode(requestParamDto.getUserId(), requestParamDto.getAccessCode());
			  if(accessCodeExist != null) {
				  return ResponseEntity.status(HttpStatus.OK).body(
							new BaseResponseIsValid(HttpStatus.OK.value(),ResponseStatus.SUCCESS.value(),Boolean.TRUE,
								ResponseMessage.ACCESS_CODE_NOT_EXIST,DateUtil.getCurrentDateTime(),
									Collections.emptyList(), null));
			  }
			  String resultAccessCode = this.userService.checkAccessCodeAvailabilty(requestParamDto.getAccessCode());
			  LOGGER.info("resultAccessCode value is "+ resultAccessCode);
			  if(resultAccessCode.equals(ResponseMessage.ACCESS_CODE_EXIST)) {
				  return ResponseEntity.status(HttpStatus.OK).body(
						new BaseResponseIsValid(HttpStatus.OK.value(),ResponseStatus.SUCCESS.value(),Boolean.FALSE,
							ResponseMessage.ACCESS_CODE_EXIST,DateUtil.getCurrentDateTime(),
								Collections.emptyList(), null));
			} else {
				return ResponseEntity.status(HttpStatus.OK).body(
						new BaseResponseIsValid(HttpStatus.OK.value(),ResponseStatus.SUCCESS.value(),Boolean.TRUE,
							ResponseMessage.ACCESS_CODE_NOT_EXIST,DateUtil.getCurrentDateTime(),
								Collections.emptyList(), null));
			 }
		  }catch(Exception exception) {
			  LOGGER.error(ResponseMessage.ACCESS_CODE_ERROR+exception.getMessage());
			  return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
						new BaseResponseIsValid(HttpStatus.INTERNAL_SERVER_ERROR.value(),ResponseStatus.FAILURE.value(),
					Boolean.FALSE,ResponseMessage.ACCESS_CODE_ERROR+exception.getMessage(),DateUtil.getCurrentDateTime(),
						Collections.emptyList(), null));
		  }
	}
	
	@PostMapping("/checkEmailAvailabilty")
	public ResponseEntity<BaseResponseIsValid> checkEmailAvailabilty(@RequestBody RequestParamDto requestParamDto) {
		LOGGER.info("Email from request ::: "+requestParamDto.getEmail());
		  try {
			  if(requestParamDto.getEmail() == null || requestParamDto.getEmail().equals("")) {
				  return ResponseEntity.status(HttpStatus.OK).body(
							new BaseResponseIsValid(HttpStatus.OK.value(),ResponseStatus.SUCCESS.value(),Boolean.FALSE,
								ResponseMessage.EMAIL_ADDRESS_NOT_BLANK,DateUtil.getCurrentDateTime(),
									Collections.emptyList(), null));
			  }
			  User emailUserExist = this.userService.getByUserIdAndEmail(requestParamDto.getUserId(), requestParamDto.getEmail());
			  if(emailUserExist != null) {
				  return ResponseEntity.status(HttpStatus.OK).body(
							new BaseResponseIsValid(HttpStatus.OK.value(),ResponseStatus.SUCCESS.value(),Boolean.TRUE,
								ResponseMessage.EMAIL_ADDRESS_NOT_EXIST,DateUtil.getCurrentDateTime(),
									Collections.emptyList(), null));
			  }
			  
			  String resultAccessCode = this.userService.checkEmailAvailabilty(requestParamDto.getEmail());
		  if(resultAccessCode.equals(ResponseMessage.EMAIL_ADDRESS_EXIST)) {
			  return ResponseEntity.status(HttpStatus.OK).body(
						new BaseResponseIsValid(HttpStatus.OK.value(),ResponseStatus.SUCCESS.value(),Boolean.FALSE,
							ResponseMessage.EMAIL_ADDRESS_EXIST,DateUtil.getCurrentDateTime(),
								Collections.emptyList(), null));
			} else {
				return ResponseEntity.status(HttpStatus.OK).body(
						new BaseResponseIsValid(HttpStatus.OK.value(),ResponseStatus.SUCCESS.value(),Boolean.TRUE,
							ResponseMessage.EMAIL_ADDRESS_NOT_EXIST,DateUtil.getCurrentDateTime(),
								Collections.emptyList(), null));
			}
		  }catch (Exception exception) {
			    LOGGER.error(ResponseMessage.EMAIL_ADDRESS_ERROR+exception.getMessage());
			    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
						new BaseResponseIsValid(HttpStatus.INTERNAL_SERVER_ERROR.value(),ResponseStatus.FAILURE.value(),
					Boolean.FALSE,ResponseMessage.EMAIL_ADDRESS_ERROR+exception.getMessage(),DateUtil.getCurrentDateTime(),
						Collections.emptyList(), null));
		}
	}
	
	@PostMapping("/forgottenAccessCode")
	public ResponseEntity<BaseResponse> forgottenAccessCode(@RequestBody ForgottenAccessCodeRequest forgottenAccessCodeRequest) {
		LOGGER.info("forgottenAccessCode request email value is : "+forgottenAccessCodeRequest.getEmail());
		LOGGER.info("forgottenAccessCode request dob value is : "+ forgottenAccessCodeRequest.getDob());
		  try {
		  User user = this.userService.getUserByEmailAndDOB(forgottenAccessCodeRequest.getEmail(), forgottenAccessCodeRequest.getDob());
		  if(user == null) {
			  return ResponseEntity.status(HttpStatus.OK).body(
						new BaseResponse(HttpStatus.OK.value(),ResponseStatus.FAILURE.value(),
								ResponseMessage.EMAIL_DOB_INCORRECT,DateUtil.getCurrentDateTime(),
							Collections.emptyList(), null));
		  } else {
			  String result = this.circleHealthEmail.forgottenAccessCode(forgottenAccessCodeRequest.getEmail(), user.getAccessCode());
			  return ResponseEntity.status(HttpStatus.OK).body(
						new BaseResponse(HttpStatus.OK.value(),ResponseStatus.SUCCESS.value(),
								ResponseMessage.ACCESS_CODE_SEND_SUCCESSFULL,DateUtil.getCurrentDateTime(),
							Collections.emptyList(), null));
		  }
		  }catch(Exception exception) {
			  return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
						new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),ResponseStatus.FAILURE.value(),
							ResponseMessage.ACCESS_CODE_SEND_ERROR+exception.getMessage(),DateUtil.getCurrentDateTime(),
						Collections.emptyList(), null));
		  }
		  
		}
	
	@GetMapping("/users")
	public ResponseEntity<BaseResponse> users() {
		LOGGER.info("Inside users method for get users list.");
		try {
			List<User> users = this.userService.getUsers();
			if(users != null && !users.isEmpty()) {
			return ResponseEntity.status(HttpStatus.OK).body(
					new BaseResponse(HttpStatus.OK.value(),ResponseStatus.SUCCESS.value(),
							ResponseMessage.GET_ALL_USERS,DateUtil.getCurrentDateTime(),
						Collections.emptyList(), users));
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(
					new BaseResponse(HttpStatus.OK.value(),ResponseStatus.SUCCESS.value(),
							ResponseMessage.USERS_NOT_FOUND,DateUtil.getCurrentDateTime(),
						Collections.emptyList(), null));
		}
		}catch(Exception exception) {
			LOGGER.error(ResponseMessage.EXCEPTION_ALL_USERS+exception.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),ResponseStatus.FAILURE.value(),
						ResponseMessage.EXCEPTION_ALL_USERS+exception.getMessage(),DateUtil.getCurrentDateTime(),
					Collections.emptyList(), null));
		}
		
	}
	
	@GetMapping("/getUser/{userId}")
	public ResponseEntity<BaseResponse> getUser(@PathVariable("userId") Long userId) {
		LOGGER.info("UserId from request ::: "+userId);
		try {
			User user = this.userService.getUserById(userId);
		if(user == null) { 
			return ResponseEntity.status(HttpStatus.OK).body(
					new BaseResponse(HttpStatus.OK.value(),ResponseStatus.SUCCESS.value(),
							ResponseMessage.USER_NOT_EXIST,DateUtil.getCurrentDateTime(),
						Collections.emptyList(), null));
	    } else { 
	    	return ResponseEntity.status(HttpStatus.OK).body(
					new BaseResponse(HttpStatus.OK.value(),ResponseStatus.SUCCESS.value(),
							ResponseMessage.USER_GET_SUCCESSFULL,DateUtil.getCurrentDateTime(),
						Collections.emptyList(), user));
	    }
		}catch(Exception exception) {
			LOGGER.error(ResponseMessage.GET_USER_ERROR+exception.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),ResponseStatus.FAILURE.value(),
						ResponseMessage.GET_USER_ERROR+exception.getMessage(),DateUtil.getCurrentDateTime(),
					Collections.emptyList(), null)); 
		}
	}
	
	@GetMapping("/addUserForm")
	public ResponseEntity<BaseResponse> addUserForm() {
		LOGGER.info("Inside addUserForm method for return initial user data.");
		try {
			
			UserFormData formDataResponse = this.userService.addUserFormData();
			
			return ResponseEntity.status(HttpStatus.OK).body(
					new BaseResponse(HttpStatus.OK.value(),ResponseStatus.SUCCESS.value(),
							ResponseMessage.INITIAL_USER_DATA,DateUtil.getCurrentDateTime(),
						Collections.emptyList(), formDataResponse));
			
		} catch(Exception exception) {
			LOGGER.error(ResponseMessage.ERROR_INITIAL_USER_DATA+exception.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),ResponseStatus.FAILURE.value(),
						ResponseMessage.ERROR_INITIAL_USER_DATA+exception.getMessage(),DateUtil.getCurrentDateTime(),
					Collections.emptyList(), null));
		}

	}

	@GetMapping("/test")
	public void test() {
		System.out.println("TEST complete");
	}

	@GetMapping("/fetchpatientdetails/{userid}/{wellnessid}")
	public ResponseEntity<BaseResponse> fetchPatientDetails(@PathVariable("userid") Long userId,
			@PathVariable("wellnessid") Long wellnessId) {
		LOGGER.info("Patient details  userid : " + userId);
		PatientDetailResponse detailResponse = null;
		try {
			detailResponse = this.userService.fetchPatientDetails(userId, wellnessId);
		} catch (Exception exception) {
			LOGGER.error(ResponseMessage.SOMETHING_WENT_WRONG + exception.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ResponseStatus.FAILURE.value(),
							ResponseMessage.SOMETHING_WENT_WRONG + exception.getMessage(),
							DateUtil.getCurrentDateTime(), Collections.emptyList(), null));
		}

		return ResponseEntity.status(HttpStatus.OK)
				.body(new BaseResponse(HttpStatus.OK.value(), ResponseStatus.SUCCESS.value(),
						"Data fetched successfully", DateUtil.getCurrentDateTime(), Collections.emptyList(),
						detailResponse));
	}

	@GetMapping("/getwellnesscheckdates/{userid}")
	public ResponseEntity<BaseResponse> getWellnessCheckDates(@PathVariable("userid") Long userId) {
		LOGGER.info("Wellness check dated for patient : " + userId);
		List<PatientWellnessCheckDetails> wellnessCheckDetails = null;
		try {
			wellnessCheckDetails = this.userService.getWellnessCheckDates(userId);
		} catch (Exception exception) {
			LOGGER.error(ResponseMessage.SOMETHING_WENT_WRONG + exception.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ResponseStatus.FAILURE.value(),
							ResponseMessage.SOMETHING_WENT_WRONG + exception.getMessage(),
							DateUtil.getCurrentDateTime(), Collections.emptyList(), null));
		}

		return ResponseEntity.status(HttpStatus.OK)
				.body(new BaseResponse(HttpStatus.OK.value(), ResponseStatus.SUCCESS.value(),
						"Data fetched successfully", DateUtil.getCurrentDateTime(), Collections.emptyList(),
						wellnessCheckDetails));
	}
	
	@GetMapping("/getBookingUrl/{userId}")
	public ResponseEntity<BaseResponse> getBookingUrl(@PathVariable("userId") Long userId) {
		LOGGER.info("UserId from request ::: "+userId);
		
		try {
			Map<String,String> bookingUrl = new HashMap<>();
			Object object = this.userService.getBookingUrl(userId);
			
			bookingUrl.put("calendlyBookingUrl", object.toString());
			
			User user = this.userService.getUserById(userId);
			if(user != null) {
				bookingUrl.put("userName", user.getFirstName()+" "+user.getSurName());
				bookingUrl.put("userId", user.getUserId().toString());
				bookingUrl.put("email", user.getEmail());
			}
			
			
			return ResponseEntity.status(HttpStatus.OK).body(
					new BaseResponse(HttpStatus.OK.value(),ResponseStatus.SUCCESS.value(),
							ResponseMessage.GET_BOOKING_URL_SUCCESS,DateUtil.getCurrentDateTime(),
						Collections.emptyList(), bookingUrl));
			
		}catch(Exception exception) {
			LOGGER.error(ResponseMessage.GET_BOOKING_URL_ERROR + exception.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ResponseStatus.FAILURE.value(),
							ResponseMessage.GET_BOOKING_URL_ERROR + exception.getMessage(),
							DateUtil.getCurrentDateTime(), Collections.emptyList(), null));
		}
	}
	
	@GetMapping("/getPatientProfileDetails/{userId}")
	public ResponseEntity<BaseResponse> getPatientProfileDetails(@PathVariable("userId") Long userId) {
		LOGGER.info("getPatientProfileDetails method userId : " + userId);
		PatientProfileResponse patientProfileResponse = new PatientProfileResponse();
		try {
		
		User user = this.userService.getUserById(userId);
		if(user != null) {
			patientProfileResponse.setUserId(userId);
			patientProfileResponse.setDob(user.getDob());
			patientProfileResponse.setSurName(user.getSurName());
			patientProfileResponse.setFirstName(user.getFirstName());
			patientProfileResponse.setEmail(user.getEmail());
			patientProfileResponse.setEthnicity(user.getEthnicity());
			patientProfileResponse.setGender(user.getGender());
			patientProfileResponse.setAccessCode(user.getAccessCode());
			patientProfileResponse.setTherapistId(user.getTherapistId());
			patientProfileResponse.setTherapistPreference(user.getTherapistPreference());
		}
		patientProfileResponse.setIsPatientProfileCompleted(this.userService.isPatientProfileCompleted(userId));
		patientProfileResponse.setWellnessCheck(Boolean.FALSE);
		LocalDateTime wellnessCheckDate = this.wellnessCheckService.getWellnessCheckDate(userId);
		if(wellnessCheckDate != null) {
			patientProfileResponse.setWellnessCheck(Boolean.TRUE);
			patientProfileResponse.setLastWellnessCheckDate(wellnessCheckDate);
		}
		
		try {
			patientProfileResponse.setIsCalendlyBooking(Boolean.FALSE);
			String object = this.patientBookingService.getPatientBookingDetail(user.getTherapistId(), userId).toString();
			System.out.println("Object ::: "+object);
			String removeOpenBraces = object.replace("{", "");
		    String finalString = removeOpenBraces.replace("}", "");
		    String[] patientBookingDetails = finalString.split(",");
		    if(patientBookingDetails != null) {
				String isCalendlyBookingValue = patientBookingDetails[0].substring(patientBookingDetails[0].indexOf("=")+1);
				patientProfileResponse.setIsCalendlyBooking(isCalendlyBookingValue.equals("true") ? Boolean.TRUE : Boolean.FALSE);
				String calendlyBookingDateWithTime = patientBookingDetails[1].substring(patientBookingDetails[1].indexOf("=")+1);
				patientProfileResponse.setLastCalendlyBookingDate(calendlyBookingDateWithTime);
				patientProfileResponse.setCalendlyBookingTimeStart(patientBookingDetails[2].substring(patientBookingDetails[2].indexOf("=")+1));
				patientProfileResponse.setCalendlyBookingTimeEnd(patientBookingDetails[3].substring(patientBookingDetails[3].indexOf("=")+1));
				patientProfileResponse.setValidateStartTime(patientBookingDetails[4].substring(patientBookingDetails[4].indexOf("=")+1));
				patientProfileResponse.setCalendlyBookingTherapistName(patientBookingDetails[5].substring(patientBookingDetails[5].indexOf("=")+1));
				patientProfileResponse.setCalendlyBookingNote(patientBookingDetails[6].substring(patientBookingDetails[6].indexOf("=")+1));
				patientProfileResponse.setStatus(patientBookingDetails[10].substring(patientBookingDetails[10].indexOf("=")+1));
		    }
		 }catch(Exception exception) {
			 LOGGER.error(ResponseMessage.ERROR_PATIENT_PROFILE_DETAIL_FROM_DOCTOR_SERVICE + exception.getMessage());
				
		 }
		}catch(Exception exception) {
			LOGGER.error(ResponseMessage.ERROR_PATIENT_PROFILE_DETAIL + exception.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ResponseStatus.FAILURE.value(),
							ResponseMessage.ERROR_PATIENT_PROFILE_DETAIL + exception.getMessage(),
							DateUtil.getCurrentDateTime(), Collections.emptyList(), null));
		}
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(new BaseResponse(HttpStatus.OK.value(), ResponseStatus.SUCCESS.value(),
						ResponseMessage.GET_PATIENT_PROFILE_DETAIL, DateUtil.getCurrentDateTime(), Collections.emptyList(),
						patientProfileResponse));
	}

	@GetMapping("/fetchpatientdetailspdf/{userid}/{wellnessid}")
	public ResponseEntity<PatientDetailResponseForFile> fetchPatientDetailsForPdf(@PathVariable("userid") Long userId,
			@PathVariable("wellnessid") Long wellnessId) {
		LOGGER.info("Inside fetchPatientDetailsForPdf method, userid : " + userId);
		PatientDetailResponseForFile patientDetailResponseForFile = new PatientDetailResponseForFile();
		PatientDetailResponse detailResponse = null;
		User user = null;
		try {
			detailResponse = this.userService.fetchPatientDetailsForPDF(userId);
			patientDetailResponseForFile.setUserId(detailResponse.getUserId());
			patientDetailResponseForFile.setProfileDetails(detailResponse.getProfileDetails());
			patientDetailResponseForFile.setWellnessCheckDetails(detailResponse.getWellnessCheckDetails());
			user = this.userService.getUserById(userId);
			patientDetailResponseForFile.setAccessCode(user.getAccessCode());
			patientDetailResponseForFile.setDob(user.getDob());
			patientDetailResponseForFile.setEmail(user.getEmail());
			patientDetailResponseForFile.setEthnicity(user.getEthnicity());
			patientDetailResponseForFile.setGender(user.getGender());
			patientDetailResponseForFile.setName(user.getFirstName()+" "+user.getSurName());
			patientDetailResponseForFile.setTherapistId(user.getTherapistId());
			patientDetailResponseForFile.setTherapistPreference(user.getTherapistPreference());
			
		} catch (Exception exception) {
			LOGGER.error(ResponseMessage.SOMETHING_WENT_WRONG + exception.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(null);
		}

		return ResponseEntity.status(HttpStatus.OK).body(patientDetailResponseForFile);
	}
	
	@PostMapping("/editTherapistName")
	public String editTherapistName(@RequestBody EditTherapistNameRequest editTherapistNameRequest) {
		LOGGER.info("EditTherapistNameRequest controller ::: "+editTherapistNameRequest);
		 try {
			 String editTherapistName = this.userService.editTherapistName(editTherapistNameRequest);
			 LOGGER.info(editTherapistName);
			 return editTherapistName;
		 }catch(Exception exception) {
			 LOGGER.info(ResponseMessage.EDIT_THERAPIST_NAME_ERROR+exception.getMessage());
		 }
		 return "";
	}
	
	@PostMapping("/filterUser")
	public ResponseEntity<BaseResponse> filterUser(@RequestBody FilterUserRequest filterUserRequest) {
		LOGGER.info("filterUser, service patient controller class ::: "+filterUserRequest);
		List<User> filterUsers = new ArrayList<>();
		try {
			filterUsers = this.userService.filterUsers(filterUserRequest);
			LOGGER.info("filterUserResponse filter users from patient service ::: "+filterUsers);
			if(filterUsers.isEmpty()) {
				return ResponseEntity.status(HttpStatus.OK).body(
						new BaseResponse(HttpStatus.OK.value(),ResponseStatus.SUCCESS.value(),
								ResponseMessage.FILTER_USERS_NOT_FOUND,DateUtil.getCurrentDateTime(),
							Collections.emptyList(), null));
			}
		}catch(Exception exception) {
			LOGGER.info(ResponseMessage.EXCEPTION_FILTER_USERS+" "+exception.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),ResponseStatus.FAILURE.value(),
						ResponseMessage.EXCEPTION_FILTER_USERS+exception.getMessage(),DateUtil.getCurrentDateTime(),
					Collections.emptyList(), null));
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(
				new BaseResponse(HttpStatus.OK.value(),ResponseStatus.SUCCESS.value(),
						ResponseMessage.GET_ALL_FILTER_USERS,DateUtil.getCurrentDateTime(),
					Collections.emptyList(), filterUsers));
	}
	
	@GetMapping("/fetchWellnessCheckDetails/{userId}")
	public ResponseEntity<BaseResponse> fetchWellnessCheckDetails(@PathVariable("userId") Long userId) {
		LOGGER.info("Inside fetchWellnessCheckDetails method, userId ::: "+userId);
		WellnessCheckDetailResponse wellnessCheckDetailResponse = null;
		try {
			
			wellnessCheckDetailResponse = this.userService.fetchWellnessCheckDetails(userId);
			return ResponseEntity.status(HttpStatus.OK).body(
					new BaseResponse(HttpStatus.OK.value(),ResponseStatus.SUCCESS.value(),
							ResponseMessage.FETCH_WELLNESS_CHECK_DETAILS,DateUtil.getCurrentDateTime(),
						Collections.emptyList(), wellnessCheckDetailResponse));
			
		}catch(Exception exception) {
			LOGGER.info(ResponseMessage.FETCH_WELLNESS_CHECK_DETAILS_ERROR+" "+exception.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),ResponseStatus.FAILURE.value(),
						ResponseMessage.FETCH_WELLNESS_CHECK_DETAILS_ERROR+exception.getMessage(),DateUtil.getCurrentDateTime(),
					Collections.emptyList(), null));
		}
		
		
	}
	
	@GetMapping("/fetchProfileDetails/{userId}")
	public ResponseEntity<BaseResponse> fetchProfileDetails(@PathVariable("userId") Long userId) {
		LOGGER.info("Inside fetchWellnessCheckDetails method, userId ::: "+userId);
		List<ProfileDetailResponse> profileDetailResponse = null;
		try {
			
			profileDetailResponse = this.userService.fetchProfileDetails(userId);
			return ResponseEntity.status(HttpStatus.OK).body(
					new BaseResponse(HttpStatus.OK.value(),ResponseStatus.SUCCESS.value(),
							ResponseMessage.FETCH_PROFILE_DETAILS,DateUtil.getCurrentDateTime(),
						Collections.emptyList(), profileDetailResponse));
			
		}catch(Exception exception) {
			LOGGER.info(ResponseMessage.FETCH_PROFILE_ERROR+" "+exception.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),ResponseStatus.FAILURE.value(),
						ResponseMessage.FETCH_PROFILE_ERROR+exception.getMessage(),DateUtil.getCurrentDateTime(),
					Collections.emptyList(), null));
		}
		
		
	}
	
	@GetMapping("/getTherapistIdByEmail/{email}")
	public Long getTherapistIdByEmail(@PathVariable("email") String email) {
		LOGGER.info("Inside getTherapistIdByEmail method, email ::: "+email);
		Long therapistId = null;
		try {
			therapistId = this.userService.getTherapistIdByEmail(email);
			
		}catch(Exception exception) {
			LOGGER.info(ResponseMessage.GET_THERAPISTID_ERROR+" "+exception.getMessage());
		}
		
		return therapistId;
	}
	
	@GetMapping("/getTherapistBookingDetails/{userId}")
	public ResponseEntity<BaseResponse> getTherapistBookingDetails(@PathVariable("userId") Long userId) {
		LOGGER.info("getTherapistBookingDetails method userId : " + userId);
	    Object therapistBookingDetails = null;
		try {
			User user = this.userService.getUserById(userId);
			therapistBookingDetails = this.patientBookingService.getPatientBookingDetail(user.getTherapistId(), userId);
		
		
	}catch(Exception exception) {
		LOGGER.error(ResponseMessage.ERROR_PATIENT_BOOKING_DETAIL_FROM_DOCTOR_SERVICE + exception.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ResponseStatus.FAILURE.value(),
						ResponseMessage.ERROR_PATIENT_BOOKING_DETAIL_FROM_DOCTOR_SERVICE + exception.getMessage(),
						DateUtil.getCurrentDateTime(), Collections.emptyList(), null));
	}
	
	return ResponseEntity.status(HttpStatus.OK)
			.body(new BaseResponse(HttpStatus.OK.value(), ResponseStatus.SUCCESS.value(),
					ResponseMessage.GET_PATIENT_BOOKING_DETAIL, DateUtil.getCurrentDateTime(), Collections.emptyList(),
					therapistBookingDetails));
	}
	
	@GetMapping("/getAllTherapistBookingDetails/{userId}")
	public ResponseEntity<BaseResponse> getAllTherapistBookingDetails(@PathVariable("userId") Long userId) {
		LOGGER.info("getAllTherapistBookingDetails method userId : " + userId);
	    Object therapistBookingDetails = null;
		try {
			
			therapistBookingDetails = this.patientBookingService.getAllPatientBookingDetail(userId);
	
		}catch(Exception exception) {
		LOGGER.error(ResponseMessage.ERROR_PATIENT_BOOKING_DETAIL_FROM_DOCTOR_SERVICE + exception.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ResponseStatus.FAILURE.value(),
						ResponseMessage.ERROR_PATIENT_BOOKING_DETAIL_FROM_DOCTOR_SERVICE + exception.getMessage(),
						DateUtil.getCurrentDateTime(), Collections.emptyList(), null));
	}
	
	return ResponseEntity.status(HttpStatus.OK)
			.body(new BaseResponse(HttpStatus.OK.value(), ResponseStatus.SUCCESS.value(),
					ResponseMessage.GET_PATIENT_BOOKING_DETAIL, DateUtil.getCurrentDateTime(), Collections.emptyList(),
					therapistBookingDetails));
	}
	
	@GetMapping("/getAnalyticsUserRequestData/{userId}")
	public ResponseEntity<AnalyticsUserRequestData> getAnalyticsUserRequestData(@PathVariable("userId") Long userId) {
		AnalyticsUserRequestData analyticsUserRequestData = null;
		try {
			
			analyticsUserRequestData = this.userService.getAnalyticsUserRequestData(userId);
			
		}catch(Exception exception) {
			LOGGER.error(ResponseMessage.ERROR_GET_ANALYTICS_USER_REQUEST_DATA + exception.getMessage());
			return null;
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(analyticsUserRequestData);
	}
}
