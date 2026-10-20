package com.circlesHealth.admin.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.circlesHealth.admin.dto.AdminDto;
import com.circlesHealth.admin.entity.User;
import com.circlesHealth.admin.entity.UserSession;
import com.circlesHealth.admin.repo.UserRepository;
import com.circlesHealth.admin.repo.UserSessionRepository;
import com.circlesHealth.admin.response.CreateEditUserResponse;
import com.circlesHealth.admin.service.AdminService;
import com.circlesHealth.admin.utils.DateUtil;
import com.circlesHealth.admin.utils.StringUtils;
import com.circlesHealth.admin.utils.constants.DatabaseConstants;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {
	
	 @Autowired
	 UserRepository userRepository;
	 
	 @Autowired
	 PasswordEncoder passwordEncoder;

	 @Autowired
	 UserSessionRepository userSessionRepository;
	 
	@Override
	public User AddAdmin(AdminDto adminDto) {
		User user = new User();
		user.setFirstName(adminDto.getFirstName());
		user.setLastName(adminDto.getLastName());
		user.setGender(adminDto.getGender());
		user.setEthnicity(adminDto.getEthnicity());
		user.setEmail(adminDto.getEmail());
		user.setDateOfBirth(adminDto.getDateOfBirth());
		user.setAccessCode(adminDto.getAccessCode());
		user.setUserUniqueId(StringUtils.getUniqueId());
		user.setUserType(DatabaseConstants.ADMIN_USER_TYPE);
		user.setCreatedBy(DatabaseConstants.SUPER_ADMIN_USER_TYPE);
		user.setCreatedDate(DateUtil.getCurrentDateTime());
		user.setPassword(passwordEncoder.encode(adminDto.getAccessCode()));
		User savedUser = this.userRepository.save(user);
		return savedUser;
	}

	/*
	 * @Override public User editAdmin(AdminDto adminDto) {
	 * 
	 * User user = this.userRepository.findByAdminId(adminDto.getAdminId());
	 * user.setAccessCode(adminDto.getAccessCode());
	 * user.setEmail(adminDto.getEmail());
	 * user.setModifiedBy(DatabaseConstants.ADMIN_USER_TYPE);
	 * user.setModifiedDate(DateUtil.getCurrentDateTime());
	 * user.setPassword(passwordEncoder.encode(adminDto.getAccessCode())); User
	 * savedAdmin = this.userRepository.save(user); return savedAdmin; }
	 */
	@Override
	public User getAdmin(Long userId) {
		return this.userRepository.getByUserId(userId);
	}

	@Override
	public String deleteAdmin(Long userId) {
		List<UserSession> userSessions =  this.userSessionRepository.findByUserId(userId);
		if(!userSessions.isEmpty()) {
			for(UserSession userSession : userSessions) {
				this.userSessionRepository.deleteById(userSession.getId());
			}
		}
		this.userRepository.deleteById(userId);
		return "Admin deleted successfully......";
	}

	@Override
	public List<User> getAdmins() {
			return this.userRepository.findAll();
	}

	@Override
	public CreateEditUserResponse editUser(User user) {
		User adminTobeEdited = this.userRepository.getByUserId(user.getUserId());
		adminTobeEdited.setDateOfBirth(user.getDateOfBirth());
		adminTobeEdited.setEthnicity(user.getEthnicity());
		adminTobeEdited.setGender(user.getGender());
		adminTobeEdited.setFirstName(user.getFirstName());
		adminTobeEdited.setLastName(user.getLastName());
		adminTobeEdited.setEmail(user.getEmail());
		adminTobeEdited.setModifiedDate(LocalDateTime.now());
		adminTobeEdited.setPassword(passwordEncoder.encode(user.getAccessCode()));
		User editedUser = (User) this.userRepository.save(adminTobeEdited);
		CreateEditUserResponse createUserResponse = this.userToCreateUserResponse(editedUser);
	    
		return createUserResponse;
	}

	public CreateEditUserResponse userToCreateUserResponse(User user) {
		CreateEditUserResponse createAdminResponse = new CreateEditUserResponse();
		createAdminResponse.setUserId(user.getUserId());
		createAdminResponse.setDateOfBirth(user.getDateOfBirth());
		createAdminResponse.setEmail(user.getEmail());
		createAdminResponse.setEthnicity(user.getEthnicity());
		createAdminResponse.setFirstName(user.getFirstName());
		createAdminResponse.setLastName(user.getLastName());
		createAdminResponse.setGender(user.getGender());
		createAdminResponse.setAccessCode(user.getAccessCode());
		createAdminResponse.setUserType(user.getUserType());
		createAdminResponse.setCreatedBy(user.getCreatedBy());
		
		return createAdminResponse;
	}

	@Override
	public User getByUserIdAndEmail(Long userId, String email) {
		return this.userRepository.getByUserIdAndEmail(userId, email);
	}

	@Override
	public String checkEmailAvailabilty(String email) {
		User user = this.userRepository.getByEmail(email);
		if(user != null) {
			return "Email address already exist.";
		} else {
			return "Email address does not exist.";
		}
	}
		

}



