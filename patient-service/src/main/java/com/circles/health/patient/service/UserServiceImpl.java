package com.circles.health.patient.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.circles.health.patient.entity.ProfileAnswer;
import com.circles.health.patient.entity.ProfileQuestion;
import com.circles.health.patient.entity.User;
import com.circles.health.patient.entity.VideoContent;
import com.circles.health.patient.entity.WellnessCheckAnswer;
import com.circles.health.patient.entity.WellnessCheckOption;
import com.circles.health.patient.entity.WellnessCheckQuestion;
import com.circles.health.patient.repository.PatientContentRepository;
import com.circles.health.patient.repository.ProfileAnswerRepository;
import com.circles.health.patient.repository.ProfileQuestionRepository;
import com.circles.health.patient.repository.UserRepository;
import com.circles.health.patient.repository.WellnessCheckAnswerRepository;
import com.circles.health.patient.repository.WellnessCheckOptionRepository;
import com.circles.health.patient.repository.WellnessCheckQuestionRepository;
import com.circles.health.patient.request.EditTherapistNameRequest;
import com.circles.health.patient.request.FilterUserRequest;
import com.circles.health.patient.response.AnalyticsUserRequestData;
import com.circles.health.patient.response.CreateEditUserResponse;
import com.circles.health.patient.response.Ethnicity;
import com.circles.health.patient.response.Gender;
import com.circles.health.patient.response.PatientDetailResponse;
import com.circles.health.patient.response.PatientProfileDetails;
import com.circles.health.patient.response.PatientWellnessCheckDetails;
import com.circles.health.patient.response.ProfileDetailResponse;
import com.circles.health.patient.response.TherapistPreference;
import com.circles.health.patient.response.UserFormData;
import com.circles.health.patient.response.WellnessCheckDetailResponse;
import com.circles.health.patient.response.WellnessCheckDetails;
import com.circles.health.patient.utils.RestTemplateUtil;
import com.circles.health.patient.utils.StringUtils;
import com.circles.health.patient.utils.constants.DatabaseConstants;

@Service
@Transactional
public class UserServiceImpl implements UserService{

	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
	private WellnessCheckAnswerRepository wellnessCheckAnswerRepository;
	@Autowired
	private WellnessCheckQuestionRepository wellnessCheckQuestionRepository;
	
	@Autowired
	private WellnessCheckOptionRepository wellnessCheckOptionRepository;
	
	@Autowired
	private ProfileAnswerRepository profileAnswerRepository;
	@Autowired
	private ProfileQuestionRepository profileQuestionRepository;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PatientContentRepository patientContentRepository;
	
	@Value("${doctor.service.base.api.url}")
    String doctorServiceUrl;
	
	@Autowired
    RestTemplate restTemplate;
	
	@Override
	public CreateEditUserResponse createUser(User user) {
		//String encodedPassword = this.bCryptPasswordEncoder.encode(user.getPassword());
		//user.setPassword(encodedPassword);
		user.setUserType(DatabaseConstants.PATIENT_USER_TYPE);
		user.setCreatedDate(LocalDateTime.now());
		user.setUserUniqueId(StringUtils.getUniqueId());
		user.setWellnessCheck("No");
		if(user != null && user.getTherapistId() != null) {
			try {
				Object obj = sendGetTherapistRequestToTherapistData(user.getTherapistId());
				user.setTherapistPreference(obj.toString());
			} catch (Exception e) {
				LOGGER.info("Exception occur while getting therapist detail from doctor service in createUser : "+e.getMessage());
				e.printStackTrace();
			}
		}
		User savedUser = this.userRepository.save(user);
		this.creaVideoContent(savedUser);
		CreateEditUserResponse createUserResponse = this.userToCreateUserResponse(savedUser);
		return createUserResponse;
	}

	@Override
	public User getUserByEmailAndDOB(String email, LocalDate dob) {
		return this.userRepository.getByEmailAndDOB(email, dob);
	}

	@Override
	public List<User> getUsers() {
		LOGGER.info("Inside getUsers method for get users list.");
		return this.userRepository.findAll();
	}

	@Override
	public CreateEditUserResponse editUser(User user) {
		User userTobeEdited = this.userRepository.getById(user.getUserId());
		userTobeEdited.setAccessCode(user.getAccessCode());
		userTobeEdited.setDob(user.getDob());
		userTobeEdited.setEthnicity(user.getEthnicity());
		userTobeEdited.setGender(user.getGender());
		userTobeEdited.setFirstName(user.getFirstName());
		userTobeEdited.setSurName(user.getSurName());
		userTobeEdited.setTherapistId(user.getTherapistId());
		userTobeEdited.setEmail(user.getEmail());
		userTobeEdited.setModifiedDate(LocalDateTime.now());
		if(user != null && user.getTherapistId() != null) {
			try {
				Object obj = sendGetTherapistRequestToTherapistData(user.getTherapistId());
				userTobeEdited.setTherapistPreference(obj.toString());
			} catch (Exception e) {
				LOGGER.info("Exception occur while getting therapist detail from doctor service in editUser : "+e.getMessage());
				e.printStackTrace();
			}
		}
		User editedUser = (User) this.userRepository.save(userTobeEdited);
		CreateEditUserResponse createUserResponse = this.userToCreateUserResponse(editedUser);
	    
		return createUserResponse;
	}

	@Override
	public String deleteUser(Long id) {
		this.userRepository.deleteById(id);
		return "User deleted successfully......";
	}

	@Override
	public String checkAccessCodeAvailabilty(String accessCode) {
		User user = this.userRepository.getByAccessCode(accessCode);
		LOGGER.info("Check User null condition is : {}", user != null);
		if(user != null) {
			LOGGER.info("user null condition is  true.");
			return "Access code already exist.";
		} else {
			LOGGER.info("user null condition is  false.");
			return "Access code does not exist.";
		}
		
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

	@Override
	public User getUserById(Long userId) {
		return this.userRepository.getByUserId(userId);
	}

	@Override
	public UserFormData addUserFormData() {
		LOGGER.info("Inside addUserFormData method.");
		List<Gender> genders = new ArrayList<>();
		genders.add(new Gender("name", "male"));
		genders.add(new Gender("name", "female"));
		genders.add(new Gender("name", "other"));
		
		
		List<Ethnicity> ethnicitys  = new ArrayList<>();
		ethnicitys.add(new Ethnicity("White", "White"));
		ethnicitys.add(new Ethnicity("Mixed or Multiple ethnic groups", "Mixed or Multiple ethnic groups"));
		ethnicitys.add(new Ethnicity("Asian or Asian British", "Asian or Asian British"));
		ethnicitys.add(new Ethnicity("Black, African, Caribbean or Black British", "Black, African, Caribbean or Black British"));
		ethnicitys.add(new Ethnicity("Other Ethnic Group", "Other Ethnic Group"));
		
		List<TherapistPreference> therapistPreferences = new ArrayList<>();
		
		try {
			Object object =  sendGetRequestToTherapistData();
			String jsonColon = object.toString().replace("=", ":");
			JSONArray jsonarray = new JSONArray(jsonColon);
			for (int i = 0; i < jsonarray.length(); i++) {
				TherapistPreference therapistPreference = new TherapistPreference();
			    JSONObject jsonobject = jsonarray.getJSONObject(i);
			    Long therapistId = jsonobject.getLong("therapistId");
			    therapistPreference.setValue(therapistId);
			    LOGGER.info("therapistId from therapist table : "+therapistId);
			    String therapistName = jsonobject.getString("therapistName");
			    therapistPreference.setName(therapistName);
			    LOGGER.info("therapistName from therapist table : "+therapistName);
			    therapistPreferences.add(therapistPreference);
			}
			
		} catch (Exception e) {
			LOGGER.info("Exception occur while getting therapist detail from doctor service : "+e.getMessage());
			e.printStackTrace();
		}
		
		UserFormData formData = new UserFormData();
		formData.setEithnicity(ethnicitys);
		formData.setGender(genders);
		formData.setTherapistPreference(therapistPreferences);
		return formData;
	}

	private Object sendGetRequestToTherapistData()
			throws Exception {

		Object o = RestTemplateUtil.getMethod(doctorServiceUrl + "/doctor/getTherapistsName", restTemplate);

		return o;
	}
	
	@Override
	public User getByUserIdAndAccessCode(Long userId, String accessCode) {
		return this.userRepository.getByUserIdAndAccessCode(userId, accessCode);
	}

	@Override
	public User getByUserIdAndEmail(Long userId, String email) {
		return this.userRepository.getByUserIdAndEmail(userId, email);
	}
	
	public CreateEditUserResponse userToCreateUserResponse(User user) {
		CreateEditUserResponse createUserResponse = new CreateEditUserResponse();
		createUserResponse.setUserId(user.getUserId());
		createUserResponse.setAccessCode(user.getAccessCode());
		createUserResponse.setDob(user.getDob());
		createUserResponse.setEmail(user.getEmail());
		createUserResponse.setEthnicity(user.getEthnicity());
		createUserResponse.setFirstName(user.getFirstName());
		createUserResponse.setSurName(user.getSurName());
		createUserResponse.setGender(user.getGender());
		createUserResponse.setTherapistPreference(user.getTherapistPreference());
		createUserResponse.setTherapistId(user.getTherapistId());
		return createUserResponse;
	}
	
	public void creaVideoContent(User user) {
		VideoContent videoContent = new VideoContent();
		videoContent.setUserId(user.getUserId());
		videoContent.setVideoLink1("https://www.youtube.com/watch?v=aBlqPJ7R8dg");
		videoContent.setVideoLink2("https://www.youtube.com/watch?v=HLKq8f7Hq94");
		videoContent.setVideoCompleted(false);
		videoContent.setVideoDurationInSeccond(0L);
		videoContent.setVideoViewed(false);
		this.patientContentRepository.save(videoContent);
		
	}

	@Override
	public PatientDetailResponse fetchPatientDetails(Long userId, Long wellnessId) {
		WellnessCheckAnswer answer = null;
		PatientDetailResponse patientDetailResponse = null;
		PatientWellnessCheckDetails patientWellnessCheckDetails = null;
		try {
			if (wellnessId == 0) {
				answer = wellnessCheckAnswerRepository.findTopByUserIdOrderByWellnessAnswerIdDesc(userId);
			} else {
				answer = wellnessCheckAnswerRepository.findByUserIdAndWellnessAnswerId(userId, wellnessId);
			}

			if (answer != null) {
				patientDetailResponse = new PatientDetailResponse();
				patientDetailResponse.setUserId(userId);
				patientWellnessCheckDetails = new PatientWellnessCheckDetails();
				patientWellnessCheckDetails.setGad7Score(answer.getGad7Score());
				patientWellnessCheckDetails.setPhq9Score(answer.getPhq9Score());
				patientWellnessCheckDetails.setTotalWellnessScore(answer.getResponseScore());
				patientWellnessCheckDetails.setWellnessCheckDate(answer.getWellnessCheckDate());
				patientWellnessCheckDetails.setWellnessCheckId(answer.getWellnessAnswerId());

				if (answer.getWellnessResponse() != null && !answer.getWellnessResponse().isEmpty()) {
					String[] commaSeperatedStr = answer.getWellnessResponse().split(",");
					List<WellnessCheckDetails> wcDetailList = new ArrayList<>();
					for (String str : commaSeperatedStr) {
						String[] colonSeperatedStr = str.split(":");
						if (colonSeperatedStr != null) {
							WellnessCheckDetails wellnessCheckDetails = new WellnessCheckDetails();
							WellnessCheckQuestion question = wellnessCheckQuestionRepository
									.findById(Integer.parseInt(colonSeperatedStr[0])).get();
							if (question != null) {
								wellnessCheckDetails.setQuestionId(question.getWellnessCheckQuestionId());
								wellnessCheckDetails.setQuestion(question.getWellnessCheckQuestion());
								wellnessCheckDetails.setQuestionSequence(question.getWellnessCheckQuestionSequence());
								wellnessCheckDetails.setCategory(question.getWellnessCheckQuestionCategory());
							}


				Optional<WellnessCheckOption> option  = wellnessCheckOptionRepository
				.findById(Long.parseLong(colonSeperatedStr[1]));
				if (option.isPresent() ) {
				WellnessCheckOption welloption= option.get();
				wellnessCheckDetails.setOptionId(welloption.getWellnessOptionId());
				wellnessCheckDetails.setOption(welloption.getOptionCaption());
				wellnessCheckDetails.setOptionSequence(welloption.getOptionSequence());
				wellnessCheckDetails.setScore(welloption.getCategory().equalsIgnoreCase("Required") ? welloption.getOptionScore() : -1);
				}
				
				else {
				wellnessCheckDetails.setOptionId(0L);
				wellnessCheckDetails.setOption("NA");
				wellnessCheckDetails.setOptionSequence(0);
				wellnessCheckDetails.setScore(0);
				
				
				}

							wcDetailList.add(wellnessCheckDetails);
						}
					}

					patientWellnessCheckDetails.setWellnessCheckDetials(wcDetailList);
				}

				patientDetailResponse.setWellnessCheckDetails(patientWellnessCheckDetails);

				List<ProfileAnswer> profileAnswerList = profileAnswerRepository
						.findByWellnessCheckId(answer.getWellnessAnswerId());
				if (profileAnswerList != null && !profileAnswerList.isEmpty()) {

					List<PatientProfileDetails> patientProfileDetails = new ArrayList<PatientProfileDetails>();
					for (ProfileAnswer profileAnswer : profileAnswerList) {

						PatientProfileDetails profileDetails = new PatientProfileDetails();
						profileDetails.setProfileId(profileAnswer.getProfileAnswerId());
						profileDetails.setQuestionId(profileAnswer.getProfileQuestionId());
						ProfileQuestion proQuestion = profileQuestionRepository
								.findById(Long.valueOf(profileAnswer.getProfileQuestionId())).get();
						profileDetails.setQuestion(proQuestion.getProfileQuestion());
						profileDetails.setQuestionSequence(proQuestion.getProfileQuestionSequence());
						profileDetails.setProfileFillingDate(profileAnswer.getProfileFillingDate());
						profileDetails.setResponse(profileAnswer.getProfileAnswer());
						patientProfileDetails.add(profileDetails);
					}

					patientDetailResponse.setProfileDetails(patientProfileDetails);
					
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return patientDetailResponse;
	}

	@Override
	public List<PatientWellnessCheckDetails> getWellnessCheckDates(Long userId) {
		List<PatientWellnessCheckDetails> wellnessCheckDetails = null;

		try {

			List<WellnessCheckAnswer> wellnessCheckAnswers = wellnessCheckAnswerRepository
					.findByUserIdOrderByWellnessAnswerIdDesc(userId);

			if (wellnessCheckAnswers != null && wellnessCheckAnswers.size() > 0) {
				wellnessCheckDetails = new ArrayList<PatientWellnessCheckDetails>();
				int count = 0;
				for (WellnessCheckAnswer answer : wellnessCheckAnswers) {
					PatientWellnessCheckDetails patientWellnessCheckDetails = new PatientWellnessCheckDetails();
					if (count == 0) {
						patientWellnessCheckDetails.setActiveDate(true);

						count++;
					} else {
						patientWellnessCheckDetails.setActiveDate(false);

					}
					patientWellnessCheckDetails.setWellnessCheckId(answer.getWellnessAnswerId());
					patientWellnessCheckDetails.setWellnessCheckDate(answer.getWellnessCheckDate());

					wellnessCheckDetails.add(patientWellnessCheckDetails);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return wellnessCheckDetails;
	}

	@Override
	public Object getBookingUrl(Long userId) throws Exception {
		
		User user = this.userRepository.getById(userId);
		Object obj = null;
		if(user != null && user.getTherapistId() != null) {
			obj = sendGetUrlRequestToTherapistData(user.getTherapistId());
		}
		
		return obj;
	}
	
	private Object sendGetUrlRequestToTherapistData(Long therapistId)
			throws Exception {

		Object o = RestTemplateUtil.getMethod(doctorServiceUrl + "/doctor/getCalendlyUrl/"+therapistId, restTemplate);

		return o;
	}
	
	private Object sendGetTherapistRequestToTherapistData(Long therapistId)
			throws Exception {

		Object o = RestTemplateUtil.getMethod(doctorServiceUrl + "/doctor/getTherapistNameById/"+therapistId, restTemplate);

		return o;
	}

	@Override
	public Boolean isPatientProfileCompleted(Long userId) {
        Boolean isPatientProfileDone = Boolean.FALSE;
		
		ProfileAnswer profileAnswer = this.profileAnswerRepository.findTopByUserIdOrderByProfileAnswerIdDesc(userId);
		if(profileAnswer != null) {
			isPatientProfileDone = Boolean.TRUE;
		}
		
		return isPatientProfileDone;
	}

	@Override
	public String editTherapistName(EditTherapistNameRequest editTherapistNameRequest) {
		LOGGER.info("EditTherapistNameRequest service ::: "+editTherapistNameRequest);
		List<User> usersToBeEdited = this.userRepository.getByTherapistId(editTherapistNameRequest.getTherapistId());
		List<User> savedUsers = new ArrayList<>();
		if(!usersToBeEdited.isEmpty()) {
			for(User userToBeEdited : usersToBeEdited) {
				userToBeEdited.setTherapistPreference(editTherapistNameRequest.getTherapistName());
				User savedUser = this.userRepository.save(userToBeEdited);
				savedUsers.add(savedUser);
			}
		}
		
		
		if(!savedUsers.isEmpty()) {
			return "Therapists edited successfully.";
		} else {
			return "Therapists not edited successfully.";
		}
		
	}

	@Override
	public List<User> filterUsers(FilterUserRequest filterUserRequest) {
		LOGGER.info("filterUsers, patient service in service class ::: "+filterUserRequest);
		List<User> users = new ArrayList<>();
		if(filterUserRequest.getFirstName() != null && filterUserRequest.getSurName() != null && filterUserRequest.getEmail() != null) {
			
			users = this.userRepository.findByFirstNameStartsWithAndSurNameStartsWithAndEmailStartsWith(filterUserRequest.getFirstName(), 
					filterUserRequest.getSurName(), filterUserRequest.getEmail());
		
		} else if(filterUserRequest.getFirstName() != null && filterUserRequest.getSurName() != null) {
			
			users = this.userRepository.findByFirstNameStartsWithAndSurNameStartsWith(filterUserRequest.getFirstName(), filterUserRequest.getSurName());
			
		} else if(filterUserRequest.getFirstName() != null && filterUserRequest.getEmail() != null) {
			
			users = this.userRepository.findByFirstNameStartsWithAndEmailStartsWith(filterUserRequest.getFirstName(), filterUserRequest.getEmail());
			
		} else if(filterUserRequest.getSurName() != null && filterUserRequest.getEmail() != null) {
			
			users = this.userRepository.findBySurNameStartsWithAndEmailStartsWith(filterUserRequest.getSurName(), filterUserRequest.getEmail());
			
		} else if(filterUserRequest.getFirstName() != null) {
			
			users = this.userRepository.findByFirstNameStartsWith(filterUserRequest.getFirstName());
			
		} else if(filterUserRequest.getSurName() != null) {
			
			users = this.userRepository.findBySurNameStartsWith(filterUserRequest.getSurName());
			
		} else if(filterUserRequest.getEmail() != null) {
			
			users = this.userRepository.findByEmailStartsWith(filterUserRequest.getEmail());
		}
		 
		
		return users;
	}

	@Override
	public WellnessCheckDetailResponse fetchWellnessCheckDetails(Long userId) {
		
		WellnessCheckDetailResponse wellnessCheckDetailResponse = null;
		List<PatientWellnessCheckDetails> patientWellnessCheckDetails = new ArrayList<>();
		List<WellnessCheckAnswer> wellnessAnswers = null;
		String wellnessCheckNumber = "Wellness Check-";
		int number = 1;
		
		try {
			wellnessCheckDetailResponse = new WellnessCheckDetailResponse();
			wellnessAnswers = this.wellnessCheckAnswerRepository.findByUserId(userId);
			for(WellnessCheckAnswer answer : wellnessAnswers) {
				if (answer != null) {
					PatientWellnessCheckDetails patientWellnessCheckDetail = new PatientWellnessCheckDetails();
					wellnessCheckDetailResponse = new WellnessCheckDetailResponse();
					wellnessCheckDetailResponse.setUserId(userId);
					patientWellnessCheckDetail = new PatientWellnessCheckDetails();
					patientWellnessCheckDetail.setGad7Score(answer.getGad7Score());
					patientWellnessCheckDetail.setPhq9Score(answer.getPhq9Score());
					patientWellnessCheckDetail.setTotalWellnessScore(answer.getResponseScore());
					patientWellnessCheckDetail.setWellnessCheckDate(answer.getWellnessCheckDate());
					patientWellnessCheckDetail.setWellnessCheckId(answer.getWellnessAnswerId());
					patientWellnessCheckDetail.setWellnessCheckNumber(wellnessCheckNumber+number++);
					if (answer.getWellnessResponse() != null && !answer.getWellnessResponse().isEmpty()) {
						String[] commaSeperatedStr = answer.getWellnessResponse().split(",");
						List<WellnessCheckDetails> wcDetailList = new ArrayList<>();
						for (String str : commaSeperatedStr) {
							String[] colonSeperatedStr = str.split(":");
							if (colonSeperatedStr != null) {
								WellnessCheckDetails wellnessCheckDetails = new WellnessCheckDetails();
								WellnessCheckQuestion question = wellnessCheckQuestionRepository
										.findById(Integer.parseInt(colonSeperatedStr[0])).get();
								if (question != null) {
									wellnessCheckDetails.setQuestionId(question.getWellnessCheckQuestionId());
									wellnessCheckDetails.setQuestion(question.getWellnessCheckQuestion());
									wellnessCheckDetails.setQuestionSequence(question.getWellnessCheckQuestionSequence());
									wellnessCheckDetails.setCategory(question.getWellnessCheckQuestionCategory());
								}
								Optional<WellnessCheckOption> option  = wellnessCheckOptionRepository
										.findById(Long.parseLong(colonSeperatedStr[1]));
										if (option.isPresent() ) {
										WellnessCheckOption welloption= option.get();
										wellnessCheckDetails.setOptionId(welloption.getWellnessOptionId());
										wellnessCheckDetails.setOption(welloption.getOptionCaption());
										wellnessCheckDetails.setOptionSequence(welloption.getOptionSequence());
										wellnessCheckDetails.setScore(welloption.getCategory().equalsIgnoreCase("Required") ? welloption.getOptionScore() : -1);
										}
										
										else {
										wellnessCheckDetails.setOptionId(0L);
										wellnessCheckDetails.setOption("NA");
										wellnessCheckDetails.setOptionSequence(0);
										wellnessCheckDetails.setScore(0);
										
										
										}
								wcDetailList.add(wellnessCheckDetails);
							}
							
						}
						
						patientWellnessCheckDetail.setWellnessCheckDetials(wcDetailList);
						patientWellnessCheckDetails.add(patientWellnessCheckDetail);
					}
					
				}
				
			}
			
			wellnessCheckDetailResponse.setWellnessCheckDetails(patientWellnessCheckDetails);
		}catch(Exception exception) {
			LOGGER.info("Exception occur while getting fetchWellnessCheckDetail : "+exception.getMessage());
		}
		
		
		
		
		return wellnessCheckDetailResponse;
	}

	@Override
	public List<ProfileDetailResponse> fetchProfileDetails(Long userId) {
		List<ProfileDetailResponse> listProfileDetailResponse = null;
		List<WellnessCheckAnswer> wellnessAnswers = null;
		String profileQuestionNumber = "Profiling-";
		int number = 1;
		try {
			listProfileDetailResponse = new ArrayList<>();

			wellnessAnswers = this.wellnessCheckAnswerRepository.findByUserId(userId);
			for(WellnessCheckAnswer answer : wellnessAnswers) {
				
			System.out.println("wellness answer id is ::: "+answer.getWellnessAnswerId());
			List<ProfileAnswer> profileAnswerList = profileAnswerRepository
					.findByWellnessCheckId(answer.getWellnessAnswerId());
			
			if (profileAnswerList != null && !profileAnswerList.isEmpty()) {
				ProfileDetailResponse profileDetailResponse = new ProfileDetailResponse();
				profileDetailResponse.setProfileQuestionNumber(profileQuestionNumber+(number++));
				List<PatientProfileDetails> patientProfileDetails = new ArrayList<PatientProfileDetails>();
				for (ProfileAnswer profileAnswer : profileAnswerList) {
					
					PatientProfileDetails profileDetails = new PatientProfileDetails();
					profileDetails.setProfileId(profileAnswer.getProfileAnswerId());
					profileDetails.setQuestionId(profileAnswer.getProfileQuestionId());
					ProfileQuestion proQuestion = profileQuestionRepository
							.findById(Long.valueOf(profileAnswer.getProfileQuestionId())).get();
					profileDetails.setQuestion(proQuestion.getProfileQuestion());
					profileDetails.setQuestionSequence(proQuestion.getProfileQuestionSequence());
					profileDetails.setProfileFillingDate(profileAnswer.getProfileFillingDate());
					profileDetails.setResponse(profileAnswer.getProfileAnswer());
					patientProfileDetails.add(profileDetails);
					
				}
				profileDetailResponse.setProfileDetails(patientProfileDetails);
				profileDetailResponse.setUserId(userId);
				listProfileDetailResponse.add(profileDetailResponse);
			}
			
		 }
		}catch(Exception exception) {
			LOGGER.info("Exception occur while getting fetchProfileDetails : "+exception.getMessage());
		}
		
		return listProfileDetailResponse;
	}

	@Override
	public PatientDetailResponse fetchPatientDetailsForPDF(Long userId) {
		LOGGER.info("Inside fetchPatientDetailsForPDF method in UserServiceImpl class.");
		ProfileAnswer profileAnswerTop = null;
		PatientDetailResponse patientDetailResponse = null;
		WellnessCheckAnswer answer = null;
		PatientWellnessCheckDetails patientWellnessCheckDetails = null;
		try {
			
			profileAnswerTop = this.profileAnswerRepository.findTopByUserIdOrderByProfileAnswerIdDesc(userId);
			if(profileAnswerTop != null) {
				patientDetailResponse = new PatientDetailResponse();
				patientDetailResponse.setUserId(userId);
				answer = wellnessCheckAnswerRepository.findByUserIdAndWellnessAnswerId(userId, profileAnswerTop.getWellnessCheckId());
			
				patientWellnessCheckDetails = new PatientWellnessCheckDetails();
				patientWellnessCheckDetails.setGad7Score(answer.getGad7Score());
				patientWellnessCheckDetails.setPhq9Score(answer.getPhq9Score());
				patientWellnessCheckDetails.setTotalWellnessScore(answer.getResponseScore());
				patientWellnessCheckDetails.setWellnessCheckDate(answer.getWellnessCheckDate());
				patientWellnessCheckDetails.setWellnessCheckId(answer.getWellnessAnswerId());

				if (answer.getWellnessResponse() != null && !answer.getWellnessResponse().isEmpty()) {
					String[] commaSeperatedStr = answer.getWellnessResponse().split(",");
					List<WellnessCheckDetails> wcDetailList = new ArrayList<>();
					for (String str : commaSeperatedStr) {
						String[] colonSeperatedStr = str.split(":");
						if (colonSeperatedStr != null) {
							WellnessCheckDetails wellnessCheckDetails = new WellnessCheckDetails();
							WellnessCheckQuestion question = wellnessCheckQuestionRepository
									.findById(Integer.parseInt(colonSeperatedStr[0])).get();
							if (question != null) {
								wellnessCheckDetails.setQuestionId(question.getWellnessCheckQuestionId());
								wellnessCheckDetails.setQuestion(question.getWellnessCheckQuestion());
								wellnessCheckDetails.setQuestionSequence(question.getWellnessCheckQuestionSequence());
								wellnessCheckDetails.setCategory(question.getWellnessCheckQuestionCategory());
							}


				Optional<WellnessCheckOption> option  = wellnessCheckOptionRepository
				.findById(Long.parseLong(colonSeperatedStr[1]));
				if (option.isPresent() ) {
				WellnessCheckOption welloption= option.get();
				wellnessCheckDetails.setOptionId(welloption.getWellnessOptionId());
				wellnessCheckDetails.setOption(welloption.getOptionCaption());
				wellnessCheckDetails.setOptionSequence(welloption.getOptionSequence());
				wellnessCheckDetails.setScore(welloption.getCategory().equalsIgnoreCase("Required") ? welloption.getOptionScore() : -1);
				}
				
				else {
				wellnessCheckDetails.setOptionId(0L);
				wellnessCheckDetails.setOption("NA");
				wellnessCheckDetails.setOptionSequence(0);
				wellnessCheckDetails.setScore(0);
				
				
				}

							wcDetailList.add(wellnessCheckDetails);
						}
					}
					patientWellnessCheckDetails.setWellnessCheckDetials(wcDetailList);
				}
			
				patientDetailResponse.setWellnessCheckDetails(patientWellnessCheckDetails);	
				
				List<ProfileAnswer> profileAnswerList = profileAnswerRepository
						.findByWellnessCheckId(answer.getWellnessAnswerId());
				if (profileAnswerList != null && !profileAnswerList.isEmpty()) {

					List<PatientProfileDetails> patientProfileDetails = new ArrayList<PatientProfileDetails>();
					for (ProfileAnswer profileAnswer : profileAnswerList) {

						PatientProfileDetails profileDetails = new PatientProfileDetails();
						profileDetails.setProfileId(profileAnswer.getProfileAnswerId());
						profileDetails.setQuestionId(profileAnswer.getProfileQuestionId());
						ProfileQuestion proQuestion = profileQuestionRepository
								.findById(Long.valueOf(profileAnswer.getProfileQuestionId())).get();
						profileDetails.setQuestion(proQuestion.getProfileQuestion());
						profileDetails.setQuestionSequence(proQuestion.getProfileQuestionSequence());
						profileDetails.setProfileFillingDate(profileAnswer.getProfileFillingDate());
						profileDetails.setResponse(profileAnswer.getProfileAnswer());
						patientProfileDetails.add(profileDetails);
					}

					patientDetailResponse.setProfileDetails(patientProfileDetails);
			}
		  }
		}catch(Exception exception) {
			LOGGER.info("Exception occur while getting fetchPatientDetailsForPDF : "+exception.getMessage());
		}
		
		return patientDetailResponse;
	}

	@Override
	public Long getTherapistIdByEmail(String email) {
		
        return this.userRepository.getTherapistIdByEmail(email);
	}

	@Override
	public User updateUser(User user) {
		LOGGER.info("Inside updateUser method of UserServiceImpl class ::: "+user);
		return this.userRepository.save(user);
	}

	@Override
	public AnalyticsUserRequestData getAnalyticsUserRequestData(Long userId) {
		AnalyticsUserRequestData analyticsUserRequestData = null;
		try {
			
			Optional<User> optionalUser = this.userRepository.findById(userId);
			if(optionalUser.isPresent()) {
				analyticsUserRequestData = new AnalyticsUserRequestData();
				User user = optionalUser.get();
				analyticsUserRequestData.setDob(user.getDob());
				analyticsUserRequestData.setEthnicity(user.getEthnicity());
				analyticsUserRequestData.setGender(user.getGender());
			}
			
		}catch(Exception exception) {
			LOGGER.info("Some exception occur inside getAnalyticsUserRequestData method of UserServiceImpl ::: "+exception.getMessage());
		}
		return analyticsUserRequestData;
	}
	
	
}
