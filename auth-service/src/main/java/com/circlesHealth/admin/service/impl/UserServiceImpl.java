package com.circlesHealth.admin.service.impl;

import com.circlesHealth.admin.dto.PatientUserDto;
import com.circlesHealth.admin.entity.User;
import com.circlesHealth.admin.entity.UserSession;
import com.circlesHealth.admin.form.add.AddUserForm;
import com.circlesHealth.admin.repo.UserRepository;
import com.circlesHealth.admin.repo.UserSessionRepository;
import com.circlesHealth.admin.response.PatientDetailResponse;
import com.circlesHealth.admin.response.PatientDetailResponseForFile;
import com.circlesHealth.admin.response.PatientProfileDetails;
import com.circlesHealth.admin.response.PatientWellnessCheckDetails;
import com.circlesHealth.admin.response.WellnessCheckDetails;
import com.circlesHealth.admin.service.UserService;
import com.circlesHealth.admin.utils.DateUtil;
import com.circlesHealth.admin.utils.JsonUtil;
import com.circlesHealth.admin.utils.RestTemplateUtil;
import com.circlesHealth.admin.utils.StringUtils;
import com.circlesHealth.admin.utils.constants.DatabaseConstants;
import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Table;
import com.lowagie.text.alignment.HorizontalAlignment;
import com.lowagie.text.alignment.VerticalAlignment;
import com.lowagie.text.pdf.PdfWriter;
import com.opencsv.CSVWriter;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    RestTemplate restTemplate;

    @Value("${patient.service.base.api.url}")
    String patientServiceUrl;
    
    @Autowired
    UserRepository userRepository;
    
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserSessionRepository userSessionRepository;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Override
    public Object addUser(AddUserForm addUserForm, String token, String sessionId) throws Exception {
        LOGGER.info("AddUserForm : " + addUserForm.toString());
        return sendUserDataToPatient(addUserForm, token, sessionId);
    }

    private Object sendUserDataToPatient(AddUserForm addUserForm,
                                         String token, String sessionId) throws Exception {

        Object o = RestTemplateUtil.postMethod(patientServiceUrl+"/addUser", token, sessionId, JsonUtil.convertObjectToString(addUserForm), restTemplate);

        return o;
    }

	@Override
	public String changeAccessCode(Long userId, String oldAccessCode, String newAccessCode) {
		
		Optional<User> user = this.userRepository.findByUserId(userId);
		if(user.get() != null) {
			if(!user.get().getAccessCode().equals(oldAccessCode)) {
				return "Old Access Code is incorrect.";
			}
			User userToBeSaved = user.get();
			userToBeSaved.setAccessCode(newAccessCode);
			userToBeSaved.setPassword(passwordEncoder.encode(newAccessCode));
			User changedAccessCodeUser = this.userRepository.save(userToBeSaved);
			if(changedAccessCodeUser != null) {
				return "Access Code changed successfully.";
			  }
			return "Unable to change access code.";
		} else {
			return "UserId is incorrect.";
		}
		
	}

	@Override
	public User findByEmail(String email) {
		
		Optional<User> optionalUser = this.userRepository.findByEmail(email);
		
		if(optionalUser.isEmpty()) {
			return null;
		}
		
		return optionalUser.get();
	}
	
	private Object sendUserDataToPatientData(PatientUserDto patientDto, String token, String userUniqueId)
			throws Exception {

		String objectValue = JsonUtil.convertObjectToString(patientDto);
		Object o = RestTemplateUtil.postMethod(patientServiceUrl + "/createUser", token, userUniqueId,
				JsonUtil.convertObjectToString(patientDto), restTemplate);

		return o;
	}
	
	private Object sendEditDataToPatientData(PatientUserDto patientDto, String token, String userUniqueId)
			throws Exception {

		String objectValue = JsonUtil.convertObjectToString(patientDto);
		Object o = RestTemplateUtil.postMethod(patientServiceUrl + "/editUser", token, userUniqueId,
				JsonUtil.convertObjectToString(patientDto), restTemplate);

		return o;
	}
	
	private Object sendDeleteDataToPatientData(Long patientId, String token, String userUniqueId)
			throws Exception {

		Object o = RestTemplateUtil.getMethod(patientServiceUrl + "/deleteUser/"+patientId, token, userUniqueId, restTemplate);

		return o;
	}
	
	@Override
    public Object createUser(PatientUserDto patientDto, String token, String userUniqueId) throws Exception {
        LOGGER.info("AddUserForm : " + patientDto.toString());
        return sendUserDataToPatientData(patientDto, token, userUniqueId);
    }
	
	@Override
	public User saveUser(PatientUserDto patientDto) {
		User user = new User();
		user.setAccessCode(patientDto.getAccessCode());
		user.setEmail(patientDto.getEmail());
		user.setUserUniqueId(StringUtils.getUniqueId());
		user.setUserType(DatabaseConstants.PATIENT_USER_TYPE);
		user.setCreatedBy(DatabaseConstants.ADMIN_USER_TYPE);
		user.setCreatedDate(DateUtil.getCurrentDateTime());
		user.setPassword(passwordEncoder.encode(patientDto.getAccessCode()));
		
		User savedUser = this.userRepository.save(user);
		
		return savedUser;
	}
	
	@Override
	public User editUser(PatientUserDto patientDto) {
		User user = this.userRepository.findByPatientId(patientDto.getUserId());
		user.setAccessCode(patientDto.getAccessCode());
		user.setEmail(patientDto.getEmail());
		user.setModifiedBy(DatabaseConstants.ADMIN_USER_TYPE);
		user.setModifiedDate(DateUtil.getCurrentDateTime());
		user.setPassword(passwordEncoder.encode(patientDto.getAccessCode()));
		User savedUser = this.userRepository.save(user);
		
		return savedUser;
	}
	
	@Override
    public Object editUser(PatientUserDto patientDto, String token, String userUniqueId) throws Exception {
        LOGGER.info("EditUserForm : " + patientDto.toString());
        return sendEditDataToPatientData(patientDto, token, userUniqueId);
    }
	
	@Override
    public Object deleteUser(Long patientId, String token, String userUniqueId) throws Exception {
        LOGGER.info("DeleteUserForm : " + patientId);
        return sendDeleteDataToPatientData(patientId, token, userUniqueId);
    }

	@Override
	public User savePatientId(User savedUser,Long patientId) {
		
		savedUser.setPatientId(patientId);
		
		return this.userRepository.save(savedUser);
	}

	@Override
	public void deleteByPatientId(Long patientId) {
		User user = this.userRepository.findByPatientId(patientId);
		List<UserSession> userSessions =  this.userSessionRepository.findByUserId(user.getUserId());
		if(!userSessions.isEmpty()) {
			for(UserSession userSession : userSessions) {
				this.userSessionRepository.deleteById(userSession.getId());
			}
		}
		this.userRepository.deleteByPatientId(patientId);
		
	}
	
	
	@Override
	public Object getUserQuestionDetails(Long userId, Long wellnessId, String token, String userUniqueId) {
		LOGGER.info("Dashboard Details For USer : " + userId);
		Object resObject = RestTemplateUtil.getMethod(
				patientServiceUrl + "/fetchpatientdetails/" + userId + "/" + wellnessId, token, userUniqueId,
				restTemplate);
		return resObject;
	}

	@Override
	public Object getPatientWellnessCheckDate(Long userId, String token, String userUniqueId) {
		LOGGER.info("fetch wellness check date for user : " + userId);
		Object resObject = RestTemplateUtil.getMethod(patientServiceUrl + "/getwellnesscheckdates/" + userId, token,
				userUniqueId, restTemplate);
		return resObject;
	}

	@Override
	public Object getPatientProfileDetails(Long userId, String token, String userUniqueId) {
		LOGGER.info("Get Patient Profile Details for admin, userId : " + userId);
		Object resObject = RestTemplateUtil.getMethod(patientServiceUrl + "/getPatientProfileDetails/" + userId, token,
				userUniqueId, restTemplate);
		return resObject;
	}

	@Override
	public PatientDetailResponseForFile getUserQuestionDetailsForPdfGeneration(Long userId) {
         LOGGER.info("Question Details For User : " + userId);
         PatientDetailResponseForFile resp = RestTemplateUtil.getMethodForDownloadPdf(patientServiceUrl 
				+ "/fetchpatientdetailspdf/" + userId + "/0", restTemplate);
		
		return resp;
	}

	@Override
	public File generateQuestionDetailsPdf(PatientDetailResponseForFile patientDetailResponse) {
		LOGGER.info("Inside generateQuestionDetailsPdf method.");
		Document document = null;
		File file = null;
		try {
			file = File.createTempFile(patientDetailResponse.getName()!=null ? patientDetailResponse.getName() : "questiondetail", ".pdf");
			LOGGER.info("file created ::: "+file);
			document = new Document();
			PdfWriter.getInstance(document,new FileOutputStream(file));
			Font font = new Font(Font.HELVETICA, 12, Font.BOLD);
			Table wellnessCheckTable = null, profileDetailTable = null, patientDetailTable = null;
			//code to populate Wellness Check Details in generated pdf. 
			PatientWellnessCheckDetails patientWellnessCheckDetails = patientDetailResponse.getWellnessCheckDetails();
			
			
			if (patientWellnessCheckDetails != null) {
				wellnessCheckTable = new Table(4); 
				wellnessCheckTable.setPadding(5);
				wellnessCheckTable.setSpacing(1);
				wellnessCheckTable.setWidth(100);
				
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				String wellnessCheckDate = patientWellnessCheckDetails.getWellnessCheckDate().format(formatter);
				
				Cell cell = new Cell("Wellness Check Questionnaire ");
				cell.setHeader(true);
				cell.setVerticalAlignment(VerticalAlignment.CENTER);
				cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
				cell.setColspan(4);
				cell.setBackgroundColor(Color.LIGHT_GRAY);
				wellnessCheckTable.addCell(cell);
				
				Cell cell1 = new Cell("Date of Wellness Check = "+wellnessCheckDate+",  Total Score=" + patientWellnessCheckDetails.getTotalWellnessScore() 
					+ "  PHQ9=" + patientWellnessCheckDetails.getPhq9Score() + "  GAD7=" + patientWellnessCheckDetails.getGad7Score());
				cell1.setHeader(true);
				cell1.setVerticalAlignment(VerticalAlignment.CENTER);
				cell1.setHorizontalAlignment(HorizontalAlignment.CENTER);
				cell1.setColspan(4);
				cell1.setBackgroundColor(Color.LIGHT_GRAY);
				wellnessCheckTable.addCell(cell1);
				
				wellnessCheckTable.addCell(new Phrase("Question", font));
				wellnessCheckTable.addCell(new Phrase("Category", font));
				wellnessCheckTable.addCell(new Phrase("Response", font));          
				wellnessCheckTable.addCell(new Phrase("Score", font));
				wellnessCheckTable.endHeaders();
				List<WellnessCheckDetails> wellnessCheckDetailsList = patientWellnessCheckDetails.getWellnessCheckDetials();
				if (wellnessCheckDetailsList != null && !wellnessCheckDetailsList.isEmpty()) {
					for (WellnessCheckDetails details : wellnessCheckDetailsList) {
						wellnessCheckTable.addCell(details.getQuestion().trim());
						wellnessCheckTable.addCell(details.getCategory().trim());
						wellnessCheckTable.addCell(details.getOption().trim());
						wellnessCheckTable.addCell(details.getScore() == -1 ? "" : String.valueOf(details.getScore()));
					}
				}
			}
			
			Paragraph para = new Paragraph("", font);
			
			//code to populate Profile Details in generated pdf.
			List<PatientProfileDetails> patientProfileDetailList = patientDetailResponse.getProfileDetails();
			if (patientProfileDetailList != null && !patientProfileDetailList.isEmpty()) {
				profileDetailTable = new Table(2);
				profileDetailTable.setPadding(5);
				profileDetailTable.setSpacing(1);
				profileDetailTable.setWidth(100);
				
				Cell cell = new Cell("Profile Detail Questionnaire");
				cell.setHeader(true);
				cell.setVerticalAlignment(VerticalAlignment.CENTER);
				cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
				cell.setColspan(2);
				cell.setBackgroundColor(Color.LIGHT_GRAY);
				profileDetailTable.addCell(cell);
				
				profileDetailTable.addCell(new Phrase("Question", font));
				profileDetailTable.addCell(new Phrase("Response", font)); 
				profileDetailTable.endHeaders();
				
				for (PatientProfileDetails patientProfileDetails : patientProfileDetailList) {
					profileDetailTable.addCell(patientProfileDetails.getQuestion().trim());
					profileDetailTable.addCell(patientProfileDetails.getResponse().trim());
				}
				
			}
			
			if(patientDetailResponse.getAccessCode() != null) {
				patientDetailTable = new Table(2);
				patientDetailTable.setPadding(5);
				patientDetailTable.setSpacing(1);
				patientDetailTable.setWidth(100);
				
				Cell cell = new Cell("Patient Detail");
				cell.setHeader(true);
				cell.setVerticalAlignment(VerticalAlignment.CENTER);
				cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
				cell.setColspan(2);
				cell.setBackgroundColor(Color.LIGHT_GRAY);
				patientDetailTable.addCell(cell);
				
				patientDetailTable.addCell(new Phrase("Name", font));
				patientDetailTable.addCell(patientDetailResponse.getName());
				patientDetailTable.addCell(new Phrase("Email", font));
				patientDetailTable.addCell(patientDetailResponse.getEmail());
				patientDetailTable.addCell(new Phrase("Date of Birth", font));
				patientDetailTable.addCell(patientDetailResponse.getDob().toString());
				patientDetailTable.addCell(new Phrase("Gender", font));
				patientDetailTable.addCell(patientDetailResponse.getGender());
				patientDetailTable.addCell(new Phrase("Ethnicity", font));
				patientDetailTable.addCell(patientDetailResponse.getEthnicity());
			}
			
			document.open();

			if(patientDetailTable != null) {
				document.add(patientDetailTable);
				
			}
			document.add(para);
			if(wellnessCheckTable != null) {
				document.add(wellnessCheckTable);
			}

			document.add(para); 

			if(profileDetailTable != null) {
				document.add(profileDetailTable);
			}
			
			document.close();
		} catch (IOException e) {
			LOGGER.info("Some exception occur while generateQuestionDetailsPdf ::: "+e.getMessage());
			e.printStackTrace();
		}
		
		return file;
	}

	@Override
	public ByteArrayInputStream generateQuestionDetailsCSV(PatientDetailResponseForFile patientDetailResponse) {
		LOGGER.info("Inside generateQuestionDetailsPdf method.");
		String[] HEADERS = {"Serial no","Name", "Email", "Date of Birth", "Gender","Ethnicity", "Date of Wellness Check", "Total Score", "PHQ9",
				"GAD7", "Question", "Category", "Response","Score", "Profile Question", "Response"};
		int patientDetailCount = 0;
		int profileDetailCount = -1;
		int serialNumber = 1;
		CSVPrinter csvPrinter =null;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			CSVFormat format = CSVFormat.DEFAULT.withHeader(HEADERS);
			csvPrinter = new CSVPrinter(new PrintWriter(out), format);
			PatientWellnessCheckDetails patientWellnessCheckDetails = patientDetailResponse.getWellnessCheckDetails();
			List<WellnessCheckDetails> wellnessCheckDetailsList = patientWellnessCheckDetails.getWellnessCheckDetials();
			if (wellnessCheckDetailsList != null && !wellnessCheckDetailsList.isEmpty()) {
				for (WellnessCheckDetails details : wellnessCheckDetailsList) {
					List<String> patientWellnessProfileData = new ArrayList<>();
					patientDetailCount++;
					profileDetailCount++;
					patientWellnessProfileData.add(String.valueOf(serialNumber++));
			if(patientDetailCount == 1) {
				patientWellnessProfileData.add(patientDetailResponse.getName());
				patientWellnessProfileData.add(patientDetailResponse.getEmail());
				patientWellnessProfileData.add(patientDetailResponse.getDob().toString());
				patientWellnessProfileData.add(patientDetailResponse.getGender());
				patientWellnessProfileData.add(patientDetailResponse.getEthnicity());
			} else {
				patientWellnessProfileData.add(" ");
				patientWellnessProfileData.add(" ");
				patientWellnessProfileData.add(" ");
				patientWellnessProfileData.add(" ");
				patientWellnessProfileData.add(" ");
			}
			
            if (patientWellnessCheckDetails != null) {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				String wellnessCheckDate = patientWellnessCheckDetails.getWellnessCheckDate().format(formatter);
				
				patientWellnessProfileData.add(wellnessCheckDate);
				patientWellnessProfileData.add(patientWellnessCheckDetails.getTotalWellnessScore().toString());
				patientWellnessProfileData.add(patientWellnessCheckDetails.getPhq9Score().toString());
				patientWellnessProfileData.add(patientWellnessCheckDetails.getGad7Score().toString());
				
				patientWellnessProfileData.add(details.getQuestion().trim());
				patientWellnessProfileData.add(details.getCategory().trim());
				patientWellnessProfileData.add(details.getOption().trim());
				patientWellnessProfileData.add(details.getScore() == -1 ? "" : String.valueOf(details.getScore()));
					}
            
                List<PatientProfileDetails> patientProfileDetailList = patientDetailResponse.getProfileDetails();
		  if(profileDetailCount < 14) {
			patientWellnessProfileData.add(patientProfileDetailList.get(profileDetailCount).getQuestion().trim());
			patientWellnessProfileData.add(patientProfileDetailList.get(profileDetailCount).getResponse().trim());
			} else {
				patientWellnessProfileData.add(" ");
				patientWellnessProfileData.add(" ");
			}
			
			csvPrinter.printRecord(patientWellnessProfileData);
				}
			}
			csvPrinter.flush();
		
	}catch(Exception exception) {
			LOGGER.info("Some exception occur while generateQuestionDetailsCSV ::: "+exception.getMessage());
			exception.printStackTrace();
		}
		
		return new ByteArrayInputStream(out.toByteArray());
	}

}
