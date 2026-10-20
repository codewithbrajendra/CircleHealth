package com.circlesHealth.admin.service;

import java.util.List;

import com.circlesHealth.admin.dto.AdminDto;
import com.circlesHealth.admin.entity.User;
import com.circlesHealth.admin.response.CreateEditUserResponse;
public interface AdminService {
	User AddAdmin(AdminDto adminDto);

	
	String deleteAdmin(Long userId);

	
	List<User> getAdmins();

	User getAdmin(Long userId);

	CreateEditUserResponse editUser(User user);

	User getByUserIdAndEmail(Long userId, String email);
	
	String checkEmailAvailabilty(String email);
}
