package com.circlesHealth.admin.service.impl;

import com.circlesHealth.admin.dto.LoginDto;
import com.circlesHealth.admin.dto.UserDto;
import com.circlesHealth.admin.dto.mappers.UserMapper;
import com.circlesHealth.admin.entity.Permission;
import com.circlesHealth.admin.form.PatientLoginForm;
import com.circlesHealth.admin.repo.UserRepository;
import com.circlesHealth.admin.utils.constants.DatabaseConstants;
import com.circlesHealth.admin.utils.constants.ResponseMessage;
import com.circlesHealth.admin.entity.User;
import com.circlesHealth.admin.entity.UserSession;
import com.circlesHealth.admin.form.AdminLoginForm;
import com.circlesHealth.admin.repo.UserSessionRepository;
import com.circlesHealth.admin.response.AnalyticsUserRequestData;
import com.circlesHealth.admin.security.AppUserDetailsService;
import com.circlesHealth.admin.security.JwtProvider;
import com.circlesHealth.admin.service.AuthService;
import com.circlesHealth.admin.utils.RestTemplateUtil;
import com.circlesHealth.admin.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private UserSessionRepository userSessionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AppUserDetailsService userDetailsService;

    @Value("${security.otp.expiration}")
    private long otpValidityInMilliseconds;
    @Value("${security.jwt.refresh.token.expiration}")
    private long refreshTokenValidity;

    @Value("${password}")
    private String password;
    
    @Value("${patient.service.base.api.url}")
    private String patientServiceUrl;

    @Autowired
    private RestTemplate restTemplate;
    
    @Override
    public Optional<LoginDto> adminLogin(AdminLoginForm adminLoginForm) {

        LOGGER.info("Admin user attempting to sign in");

        Optional<User> user;
        if (StringUtils.isNotEmpty(adminLoginForm.getEmail())) {
            user = userRepository.findByEmailAndAccessCodeAndUserType(adminLoginForm.getEmail(),
                    adminLoginForm.getAccessCode(), Arrays.asList(DatabaseConstants.ADMIN_USER_TYPE,DatabaseConstants.SUPER_ADMIN_USER_TYPE));
        } else {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, ResponseMessage.INSUFFICIENT_AUTHENTICATION);
        }

        if (user.isEmpty()) {
            return Optional.empty();
        }

        checkUserActive(user.get());

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        user.get().getUserUniqueId(), adminLoginForm.getAccessCode());
        authenticationManager.authenticate(authenticationToken);

        return getLoginUser(user.get());
    }

    @Override
    public Optional<LoginDto> patientLogin(PatientLoginForm patientLoginForm) {

        LOGGER.info("Patient user attempting to sign in");

        Optional<User> user;
        if (StringUtils.isNotEmpty(patientLoginForm.getAccessCode())) {
            user = userRepository.findByAccessCodeAndUserType(patientLoginForm.getAccessCode()
                    , DatabaseConstants.PATIENT_USER_TYPE);
        } else {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST,
                    ResponseMessage.INSUFFICIENT_AUTHENTICATION);
        }

        if (user.isEmpty()) {
            return Optional.empty();
        }

        checkUserActive(user.get());

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        user.get().getUserUniqueId(), patientLoginForm.getAccessCode());
        authenticationManager.authenticate(authenticationToken);

        Optional<LoginDto> loginDtoOption = getLoginUser(user.get());
        LoginDto loginDto = loginDtoOption.get();
        UserDto userDto = loginDto.getUserDto();
        userDto.setUserId(user.get().getPatientId());
        loginDto.setUserDto(userDto);
        
        return loginDtoOption.of(loginDto);
    }

    private Optional<LoginDto> getLoginUser(User user) {

        List<Permission> permissionList = userDetailsService.getUserPermissions(user);

        String token = jwtProvider.createToken(user.getUserUniqueId(), permissionList);

        String refreshToken = jwtProvider.createRefreshToken(user.getUserUniqueId(),
                permissionList);

        String sessionUniqueId = createUserSession(user, token, refreshToken).
                getSessionUniqueId();

        return Optional.of(new LoginDto(token, refreshToken, sessionUniqueId,
                UserMapper.INSTANCE.getEntityToDto(user)));
    }

    private UserSession createUserSession(User user, String token, String refreshToken) {
        UserSession userSession = new UserSession();
        userSession.setSessionUniqueId(StringUtils.getUniqueId());
        userSession.setActive(true);
        userSession.setUserId(user.getUserId());
        userSession.setToken(token);
        userSession.setRefreshToken(refreshToken);
        userSession.setExpiresAt(LocalDateTime.now().plusSeconds(refreshTokenValidity / 1000));
        return userSessionRepository.save(userSession);
    }

    private void checkUserActive(User user) {
        if (Boolean.FALSE.equals(user.getActive())) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, ResponseMessage.USER_IS_NOT_ACTIVE);
        }
    }
    
    @Override
    public void deactivateUserSession(String token, String uniqueSessionId) {
        token = jwtProvider.getBearerToken(token).orElseThrow(() ->
                new HttpServerErrorException(HttpStatus.BAD_REQUEST, ResponseMessage.INVALID_TOKEN));
        UserSession userSession = userSessionRepository.findByTokenAndSessionUniqueId(token, uniqueSessionId)
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.SESSION_DOES_NOT_EXIST));
        userSession.setActive(false);
        userSessionRepository.save(userSession);
    }

	@Override
	public AnalyticsUserRequestData getAnalyticsUserRequestData(Long userId) {
		LOGGER.info("Inside getAnalyticsUserRequestData method, userId ::: "+userId);
		AnalyticsUserRequestData analyticsUserRequestData = null;
		try {
			
			analyticsUserRequestData = RestTemplateUtil.getAnalyticsUserRequestData(patientServiceUrl + "/getAnalyticsUserRequestData/" + userId, restTemplate);
			
		}catch(Exception exception) {
			LOGGER.info("Some exception occur inside getAnalyticsUserRequestData of AuthServiceImpl ::: "+exception.getMessage());
		}
		
		
		return analyticsUserRequestData;
	}
    
    
}