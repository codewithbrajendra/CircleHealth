package com.circlesHealth.admin.controller;

import com.circlesHealth.admin.dto.PatientUserDto;
import com.circlesHealth.admin.dto.RequestParamDto;
import com.circlesHealth.admin.email.CircleHealthEmail;
import com.circlesHealth.admin.entity.User;
import com.circlesHealth.admin.form.add.AddUserForm;
import com.circlesHealth.admin.response.CreateUserResponse;
import com.circlesHealth.admin.response.PatientDetailResponse;
import com.circlesHealth.admin.response.PatientDetailResponseForFile;
import com.circlesHealth.admin.security.AppUserDetailsService;
import com.circlesHealth.admin.service.UserService;
import com.circlesHealth.admin.utils.DateUtil;
import com.circlesHealth.admin.utils.constants.EndPoints;
import com.circlesHealth.admin.utils.constants.ResponseMessage;
import com.circlesHealth.admin.utils.enums.ResponseStatus;
import com.circlesHealth.admin.wrapper.BaseResponse;
import com.circlesHealth.admin.wrapper.BaseResponseIsValid;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import io.swagger.annotations.ApiOperation;

import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONObject;
//import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Collections;
import java.util.HashMap;


@RestController
@RequestMapping(EndPoints.BASE_PATH + "/user")
public class UserController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
	
    @Autowired
    AppUserDetailsService appUserDetailsService;

    @Autowired
    UserService userService;
    
    @Autowired
	private CircleHealthEmail circleHealthEmail;

    @PostMapping
    @ApiOperation(value = "Public api to Add a user by admin. Return added user dto.")
    public ResponseEntity<BaseResponse> addUser(
            @RequestBody @Valid AddUserForm addUserForm,
            @RequestHeader(name = "Authorization") String token,
            @RequestHeader(name = "sessionId") String sessionId) throws Exception {
        Object responseObject = userService.addUser(addUserForm, token, sessionId);
        return ResponseEntity.status(HttpStatus.OK).body(
                new BaseResponse(HttpStatus.OK.value(),
                        ResponseStatus.SUCCESS.value(), ResponseMessage.LOGIN_SUCCESS,
                        DateUtil.getCurrentDateTime(), Collections.emptyList(), responseObject));
    }

    @GetMapping("/userDetail")
    @ApiIgnore
    @ApiOperation(value = "Api for fetching user detail by token")
    public ResponseEntity<BaseResponse> getUserDetail(@RequestParam String userUniqueId) {

        UserDetails userDetails = appUserDetailsService.loadUserByUsername(userUniqueId);

        return ResponseEntity.ok(
                new BaseResponse(HttpStatus.OK.value(),
                        ResponseStatus.SUCCESS.value(),
                        ResponseMessage.DATA_FOUND,
                        DateUtil.getCurrentDateTime(),
                        Collections.emptyList(),
                        userDetails));
    }
    
    @PostMapping("/createUser")
	public ResponseEntity<BaseResponse> createUser(@RequestBody PatientUserDto patientDto,@RequestHeader(name = "Authorization") String token,
            @RequestHeader(name = "userUniqueId") String userUniqueId) {
		LOGGER.info("UserController : Patient User "+patientDto.getFirstName()+" is to be create.");
		User savedUser = null;
		
		try {
		
			savedUser = this.userService.saveUser(patientDto);
		
			Object responseObject = this.userService.createUser(patientDto, token, userUniqueId);
			
			String jsonString = responseObject.toString();
			
			HashMap<String, String> holder = new HashMap();
			Long userId = null;
			String[] keyVals = responseObject.toString().split(", ");
		    for(String keyVal:keyVals)
		    {
		      String[] parts = keyVal.split("=",2);
		      holder.put(parts[0],parts[1]);
		      userId = Long.parseLong(holder.get(parts[0]));
		      break;
		    }
		    System.out.println("userId : "+userId);
			
			
			User user = this.userService.savePatientId(savedUser,userId);
			
			return ResponseEntity.status(HttpStatus.OK).body(
	                new BaseResponse(HttpStatus.OK.value(),
	                        ResponseStatus.SUCCESS.value(), ResponseMessage.USER_CREATED_SUCCESS,
	                        DateUtil.getCurrentDateTime(), Collections.emptyList(), responseObject));
			
		}catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),ResponseStatus.FAILURE.value(),
						ResponseMessage.USER_CREATED_ERROR+exception.getMessage(),DateUtil.getCurrentDateTime(),
					Collections.emptyList(), null));
		}
		
	}
    
    @PostMapping("/editUser")
	public ResponseEntity<BaseResponse> editUser(@RequestBody PatientUserDto patientDto,@RequestHeader(name = "Authorization") String token,
            @RequestHeader(name = "userUniqueId") String userUniqueId) {
		LOGGER.info("UserController : Patient Edit User "+patientDto.getFirstName()+" is to be create.");
		User editUser = null;
		
		try {
		
			editUser = this.userService.editUser(patientDto);
		
			Object responseObject = this.userService.editUser(patientDto, token, userUniqueId);
			
			
			return ResponseEntity.status(HttpStatus.OK).body(
	                new BaseResponse(HttpStatus.OK.value(),
	                        ResponseStatus.SUCCESS.value(), ResponseMessage.USER_EDITED_SUCCESS,
	                        DateUtil.getCurrentDateTime(), Collections.emptyList(), responseObject));
			
		}catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),ResponseStatus.FAILURE.value(),
						ResponseMessage.USER_EDITED_ERROR+exception.getMessage(),DateUtil.getCurrentDateTime(),
					Collections.emptyList(), null));
		}
		
	}
    
    @GetMapping("/deleteUser/{userId}")
	public ResponseEntity<BaseResponse> deleteUser(@PathVariable("userId") Long userId, @RequestHeader(name = "Authorization") String token,
            @RequestHeader(name = "userUniqueId") String userUniqueId) {
		//LOGGER.info("UserController : Patient Edit User "+patientDto.getFirstName()+" is to be create.");
		User editUser = null;
		
		try {
		
			 this.userService.deleteByPatientId(userId);
		
			Object responseObject = this.userService.deleteUser(userId, token, userUniqueId);
			
			
			return ResponseEntity.status(HttpStatus.OK).body(
	                new BaseResponse(HttpStatus.OK.value(),
	                        ResponseStatus.SUCCESS.value(), ResponseMessage.USER_DELETED_SUCCESS,
	                        DateUtil.getCurrentDateTime(), Collections.emptyList(), null));
			
		}catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),ResponseStatus.FAILURE.value(),
						ResponseMessage.USER_DELETED_ERROR+exception.getMessage(),DateUtil.getCurrentDateTime(),
					Collections.emptyList(), null));
		}
		
	}
    
    @PostMapping("/changeAccessCode")
    public ResponseEntity<BaseResponseIsValid> changeAccessCode(@RequestBody RequestParamDto requestParamDto) {
    
    	String result = this.userService.changeAccessCode(requestParamDto.getUserId(),requestParamDto.getOldAccessCode(), requestParamDto.getNewAccessCode());
    	
    	if(result.equals(ResponseMessage.ACCESS_CODE_CHANGED_SUCCESSFULLY)) {
    		return ResponseEntity.status(HttpStatus.OK).body(
                    new BaseResponseIsValid(HttpStatus.OK.value(),
                            ResponseStatus.SUCCESS.value(),Boolean.TRUE, ResponseMessage.ACCESS_CODE_CHANGED_SUCCESSFULLY,
                            DateUtil.getCurrentDateTime(), Collections.emptyList(), null));
    	} else if(result.equals(ResponseMessage.INVALID_OLD_ACCESS_CODE)) {
    		return ResponseEntity.status(HttpStatus.OK).body(
                    new BaseResponseIsValid(HttpStatus.OK.value(),
                            ResponseStatus.FAILURE.value(),Boolean.FALSE, ResponseMessage.INVALID_OLD_ACCESS_CODE,
                            DateUtil.getCurrentDateTime(), Collections.emptyList(), null));
    	}
    	
    	return ResponseEntity.status(HttpStatus.OK).body(
                new BaseResponseIsValid(HttpStatus.OK.value(),
                        ResponseStatus.SUCCESS.value(),Boolean.FALSE, "",
                        DateUtil.getCurrentDateTime(), Collections.emptyList(), null));
    }
    
    @PostMapping("/adminForgottenAccessCode")
    public ResponseEntity<BaseResponseIsValid> adminForgottenAccessCode(@RequestBody RequestParamDto requestParamDto) {
    	
    	try {
    	User user = this.userService.findByEmail(requestParamDto.getEmail());
    	
    	if(user == null) {
    		return ResponseEntity.status(HttpStatus.OK).body(
					new BaseResponseIsValid(HttpStatus.OK.value(),ResponseStatus.FAILURE.value(),Boolean.FALSE,
							ResponseMessage.INVALID_EMAIL_ADDRESS,DateUtil.getCurrentDateTime(),
						Collections.emptyList(), null));
    	} else {
    		
    		String result = this.circleHealthEmail.forgottenAccessCode(requestParamDto.getEmail(), user.getAccessCode());
    		return ResponseEntity.status(HttpStatus.OK).body(
					new BaseResponseIsValid(HttpStatus.OK.value(),ResponseStatus.SUCCESS.value(),Boolean.TRUE,
							ResponseMessage.ACCESS_CODE_SEND_SUCCESSFULLY,DateUtil.getCurrentDateTime(),
						Collections.emptyList(), null));
    	}
    	}catch(Exception exception) {
    		
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					new BaseResponseIsValid(HttpStatus.INTERNAL_SERVER_ERROR.value(),ResponseStatus.FAILURE.value(),
						Boolean.FALSE,ResponseMessage.ACCESS_CODE_SEND_ERROR+exception.getMessage(),DateUtil.getCurrentDateTime(),
					Collections.emptyList(), null));
    	}
    }
    
	@GetMapping("/userquestiondetail/{userId}/{wellnessid}")
	public ResponseEntity<BaseResponse> userQuestionDetails(@PathVariable("userId") Long userId,
			@PathVariable(name = "wellnessid") Long wellnessId, @RequestHeader(name = "Authorization") String token,
			@RequestHeader(name = "userUniqueId") String userUniqueId) {

		try {
			Object responseObject = this.userService.getUserQuestionDetails(userId, wellnessId, token,
					userUniqueId);

			return ResponseEntity.status(HttpStatus.OK)
					.body(new BaseResponse(HttpStatus.OK.value(), ResponseStatus.SUCCESS.value(),
							"Data fetch successfully", DateUtil.getCurrentDateTime(), Collections.emptyList(),
							responseObject));

		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ResponseStatus.FAILURE.value(),
							ResponseMessage.SOMETHING_WENT_WRONG + exception.getMessage(),
							DateUtil.getCurrentDateTime(), Collections.emptyList(), null));
		}

	}

	@GetMapping("/getpatientwellnesscheckdate/{userId}")
	public ResponseEntity<BaseResponse> getPatientWellnessCheckDate(@PathVariable("userId") Long userId,
			@RequestHeader(name = "Authorization") String token,
			@RequestHeader(name = "userUniqueId") String userUniqueId) {

		try {
			Object responseObject = this.userService.getPatientWellnessCheckDate(userId, token, userUniqueId);

			return ResponseEntity.status(HttpStatus.OK)
					.body(new BaseResponse(HttpStatus.OK.value(), ResponseStatus.SUCCESS.value(),
							"Data fetched successfully", DateUtil.getCurrentDateTime(), Collections.emptyList(),
							responseObject));

		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ResponseStatus.FAILURE.value(),
							ResponseMessage.SOMETHING_WENT_WRONG + exception.getMessage(),
							DateUtil.getCurrentDateTime(), Collections.emptyList(), null));
		}

    }
	
	@GetMapping("/getPatientProfileDetails/{userId}")
	public ResponseEntity<BaseResponse> getPatientProfileDetails(@PathVariable("userId") Long userId,
			@RequestHeader(name = "Authorization") String token,
			@RequestHeader(name = "userUniqueId") String userUniqueId) {
	
		try {
			Object responseObject = this.userService.getPatientProfileDetails(userId, token, userUniqueId);

			return ResponseEntity.status(HttpStatus.OK)
					.body(new BaseResponse(HttpStatus.OK.value(), ResponseStatus.SUCCESS.value(),
							ResponseMessage.GET_PATIENT_PROFILE_DETAIL, DateUtil.getCurrentDateTime(), Collections.emptyList(),
							responseObject));

		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ResponseStatus.FAILURE.value(),
							ResponseMessage.ERROR_PATIENT_PROFILE_DETAIL + exception.getMessage(),
							DateUtil.getCurrentDateTime(), Collections.emptyList(), null));
		
	}
	}
  
	@GetMapping("/getpatientdetailpdf/{userId}")
	public ResponseEntity<InputStreamResource> getPatientDetailPdf(@PathVariable("userId") Long userId) {
		LOGGER.info("Inside getPatientDetailPdf method in UserController class.");
		LOGGER.info("getPatientDetailPdf userId ::: "+userId);
		HttpHeaders respHeaders = null;
		InputStreamResource isr = null;
		
		try {
			PatientDetailResponseForFile patientDetailResponseForFile = this.userService.getUserQuestionDetailsForPdfGeneration(userId);
			LOGGER.info("patientDetailResponseForFile from patient service ::: "+patientDetailResponseForFile);
			if (patientDetailResponseForFile != null) {
				File file = this.userService.generateQuestionDetailsPdf(patientDetailResponseForFile);
				
				respHeaders = new HttpHeaders();
				MediaType mediaType = MediaType.parseMediaType("application/pdf");
				respHeaders.setContentType(mediaType);
				respHeaders.setContentLength(file.length());
				respHeaders.setContentDispositionFormData("attachment", file.getName());
				isr = new InputStreamResource(new FileInputStream(file));
				file.delete();
			}

		} catch (Exception exception) {
			LOGGER.info("Some exception occur while get Patient Detail PDF ::: "+exception.getMessage());
			exception.printStackTrace();
		}

		return new ResponseEntity<InputStreamResource>(isr, respHeaders, HttpStatus.OK);
    }
	
	@GetMapping("/getPatientDetailCSV/{userId}")
	public ResponseEntity<InputStreamResource> getPatientDetailCSV(@PathVariable("userId") Long userId) {
		LOGGER.info("Inside getPatientDetailCSV method in UserController class.");
		LOGGER.info("getPatientDetailCSV userId ::: "+userId);
		HttpHeaders respHeaders = null;
		InputStreamResource isr = null;
		String filename = "";
		try {
			PatientDetailResponseForFile patientDetailResponseForFile = this.userService.getUserQuestionDetailsForPdfGeneration(userId);
			LOGGER.info("patientDetailResponseForFile from patient service ::: "+patientDetailResponseForFile);
			if (patientDetailResponseForFile != null) {
				ByteArrayInputStream fileData = this.userService.generateQuestionDetailsCSV(patientDetailResponseForFile);
				
				 isr = new InputStreamResource(fileData);
				
				if(patientDetailResponseForFile.getName() != null) {
					filename = patientDetailResponseForFile.getName();
				}
				
				respHeaders = new HttpHeaders();
				MediaType mediaType = MediaType.parseMediaType("application/csv");
				respHeaders.setContentType(mediaType);
				respHeaders.setContentDispositionFormData("attachment" , filename + ".csv");
			}

		} catch (Exception exception) {
			LOGGER.info("Some exception occur while get Patient Detail CSV ::: "+exception.getMessage());
			exception.printStackTrace();
		}

		return new ResponseEntity<InputStreamResource>(isr, respHeaders, HttpStatus.OK);
    }
}