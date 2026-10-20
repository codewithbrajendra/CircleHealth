package com.circlesHealth.admin.controller;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.circlesHealth.admin.dto.AdminDto;
import com.circlesHealth.admin.dto.RequestParamDto;
import com.circlesHealth.admin.entity.User;
import com.circlesHealth.admin.response.CreateEditUserResponse;
import com.circlesHealth.admin.service.AdminService;
import com.circlesHealth.admin.utils.DateUtil;
import com.circlesHealth.admin.utils.constants.EndPoints;
import com.circlesHealth.admin.utils.constants.ResponseMessage;
import com.circlesHealth.admin.utils.enums.ResponseStatus;
import com.circlesHealth.admin.wrapper.BaseResponse;
import com.circlesHealth.admin.wrapper.BaseResponseIsValid;

@RestController
@RequestMapping(EndPoints.BASE_PATH + "/admin")
public class AdminController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);
	
	@Autowired
	AdminService adminService;
	
	 @PostMapping("/addAdmin")
		public ResponseEntity<BaseResponse> createUser(@RequestBody AdminDto adminDto,@RequestHeader(name = "Authorization") String token,
	            @RequestHeader(name = "userUniqueId") String userUniqueId) {
			User addAdmin = null;
			
			try {
			
				addAdmin = this.adminService.AddAdmin(adminDto);
				return ResponseEntity.status(HttpStatus.OK).body(
		                new BaseResponse(HttpStatus.OK.value(),
		                        ResponseStatus.SUCCESS.value(), ResponseMessage.ADMIN_ADDED_SUCCESS,
		                        DateUtil.getCurrentDateTime(), Collections.emptyList(), addAdmin));
				
			}catch (Exception exception) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
						new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),ResponseStatus.FAILURE.value(),
							ResponseMessage.ADMIN_ADDED_ERROR+exception.getMessage(),DateUtil.getCurrentDateTime(),
						Collections.emptyList(), null));
			}
			
		}
	 
	 
	 @PostMapping("/editUser")
		public ResponseEntity<BaseResponse> editUser(@RequestBody User user) {
			CreateEditUserResponse createUserResponse = null;
			try {
			
				createUserResponse = this.adminService.editUser(user);
			
			}catch(Exception exception) {
				LOGGER.error(ResponseMessage.ADMIN_EDITED_ERROR+exception.getMessage());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
							new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),ResponseStatus.FAILURE.value(),
								ResponseMessage.ADMIN_EDITED_ERROR+exception.getMessage(),DateUtil.getCurrentDateTime(),
							Collections.emptyList(), null));
			}
			
			return ResponseEntity.status(HttpStatus.OK).body(
					new BaseResponse(HttpStatus.OK.value(),ResponseStatus.SUCCESS.value(),
							ResponseMessage.ADMIN_EDITED_SUCCESS,DateUtil.getCurrentDateTime(),
						Collections.emptyList(), createUserResponse));
		}
	    
	 @GetMapping("/deleteAdmin/{id}")
		public ResponseEntity<BaseResponse> deleteUser(@PathVariable("id") Long id) {
			LOGGER.info("Admin User id "+id+" is to be delete.");
			String deletedUserMsg ="";
			try {
			
				 deletedUserMsg = this.adminService.deleteAdmin(id);
			
			}catch(Exception exception) {
				LOGGER.error(ResponseMessage.ADMIN_DELETED_ERROR+exception.getMessage());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
						new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),ResponseStatus.FAILURE.value(),
					ResponseMessage.USER_DELETED_ERROR+exception.getMessage(),DateUtil.getCurrentDateTime(),
						Collections.emptyList(), null));
			}
			
			return ResponseEntity.status(HttpStatus.OK).body(
					new BaseResponse(HttpStatus.OK.value(),ResponseStatus.SUCCESS.value(),
							ResponseMessage.USER_DELETED_SUCCESS,DateUtil.getCurrentDateTime(),
						Collections.emptyList(), deletedUserMsg));
		}
		
	    @GetMapping("/getAdmins")
		public ResponseEntity<BaseResponse> users() {
			LOGGER.info("Inside admin method for get users list.");
			try {
				List<User> users = this.adminService.getAdmins();
				List<User> filterUser = users.stream().filter(u ->
				u.getUserType().equals("ADMIN")).collect(Collectors.toList());
			
				if(users != null && !users.isEmpty()) {
				return ResponseEntity.status(HttpStatus.OK).body(
						new BaseResponse(HttpStatus.OK.value(),ResponseStatus.SUCCESS.value(),
								ResponseMessage.GET_ALL_ADMIN,DateUtil.getCurrentDateTime(),
							Collections.emptyList(), filterUser));
			} else {
				return ResponseEntity.status(HttpStatus.OK).body(
						new BaseResponse(HttpStatus.OK.value(),ResponseStatus.SUCCESS.value(),
								ResponseMessage.ADMIN_NOT_FOUND,DateUtil.getCurrentDateTime(),
							Collections.emptyList(), null));
			}
			}catch(Exception exception) {
				LOGGER.error(ResponseMessage.EXCEPTION_ALL_ADMIN+exception.getMessage());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
						new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),ResponseStatus.FAILURE.value(),
							ResponseMessage.EXCEPTION_ALL_ADMIN+exception.getMessage(),DateUtil.getCurrentDateTime(),
						Collections.emptyList(), null));
			}
			
		}
		
		@GetMapping("/getAdmin/{userId}")
		public ResponseEntity<BaseResponse> getUser(@PathVariable("userId") Long userId) {
			LOGGER.info("UserId from request ::: "+userId);
			try {
				User user = this.adminService.getAdmin(userId);
			if(user == null) { 
				return ResponseEntity.status(HttpStatus.OK).body(
						new BaseResponse(HttpStatus.OK.value(),ResponseStatus.SUCCESS.value(),
								ResponseMessage.ADMIN_NOT_EXIST,DateUtil.getCurrentDateTime(),
							Collections.emptyList(), null));
		    } else { 
		    	return ResponseEntity.status(HttpStatus.OK).body(
						new BaseResponse(HttpStatus.OK.value(),ResponseStatus.SUCCESS.value(),
								ResponseMessage.ADMIN_GET_SUCCESSFULL,DateUtil.getCurrentDateTime(),
							Collections.emptyList(), user));
		    }
			}catch(Exception exception) {
				LOGGER.error(ResponseMessage.GET_ADMIN_ERROR+exception.getMessage());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
						new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),ResponseStatus.FAILURE.value(),
							ResponseMessage.GET_ADMIN_ERROR+exception.getMessage(),DateUtil.getCurrentDateTime(),
						Collections.emptyList(), null)); 
			}
		}	
		
		@PostMapping("/checkAdminEmailAvailabilty")
		public ResponseEntity<BaseResponseIsValid> checkAdminEmailAvailabilty(@RequestBody RequestParamDto requestParamDto) {
			LOGGER.info("Email from request ::: "+requestParamDto.getEmail());
			  try {
				  if(requestParamDto.getEmail() == null || requestParamDto.getEmail().equals("")) {
					  return ResponseEntity.status(HttpStatus.OK).body(
								new BaseResponseIsValid(HttpStatus.OK.value(),ResponseStatus.SUCCESS.value(),Boolean.FALSE,
									ResponseMessage.EMAIL_ADDRESS_NOT_BLANK,DateUtil.getCurrentDateTime(),
										Collections.emptyList(), null));
				  }
				  User emailUserExist = this.adminService.getByUserIdAndEmail(requestParamDto.getUserId(), requestParamDto.getEmail());
				  if(emailUserExist != null) {
					  return ResponseEntity.status(HttpStatus.OK).body(
								new BaseResponseIsValid(HttpStatus.OK.value(),ResponseStatus.SUCCESS.value(),Boolean.TRUE,
									ResponseMessage.EMAIL_ADDRESS_NOT_EXIST,DateUtil.getCurrentDateTime(),
										Collections.emptyList(), null));
				  }
				  
				  String resultAccessCode = this.adminService.checkEmailAvailabilty(requestParamDto.getEmail());
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
}
