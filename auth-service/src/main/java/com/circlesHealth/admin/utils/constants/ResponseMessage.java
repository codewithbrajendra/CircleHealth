package com.circlesHealth.admin.utils.constants;


import com.circlesHealth.admin.utils.fileconstant.FileSize;

public class ResponseMessage {

    public static final String DATA_FOUND = "Data found";
    public static final String ACCESS_DENIED = "Access Denied";
    public static final String DATA_NOT_FOUND = "Data not found";
    public static final String DATA_DELETED = "Data deleted successfully";
    public static final String INSUFFICIENT_AUTHENTICATION = "Insufficient Authentication";
    public static final String SOMETHING_WENT_WRONG = "Something went wrong";
    public static final String REQUEST_METHOD_NOT_SUPPORTED = "Request method not supported";
    public static final String API_NOT_FOUND = "Api not found, please check url";
    public static final String VALIDATION_ERROR = "Validation error";
    public static final String EMAIL_SENT = "Email sent";
    public static final String LOGOUT_SUCCESS = "Logged out successfully";
    public static final String CREATED = "Created";
    public static final String INVALID_TOKEN = "Invalid token";
    public static final String USER_NOT_EXISTS = "User does not exists";
    public static final String USER_ALREADY_EXISTS = "User already exists with this email";
    public static final String SESSION_DOES_NOT_EXIST = "Session does not exists";
    public static final String PASSWORD_RESET_SUCCESS = "Password reset successfully";
    public static final String LOGIN_SUCCESS = "Logged in successfully";
    public static final String DATA_UPDATED = "Data updated successfully.";
    public static final String LOGIN_FAILED = "Login failed";
    public static final String INCORRECT_USERNAME_ACCESS_CODE = "Username and Access code does not match";
    public static final String INCORRECT_ACCESS_CODE = "Access code does not found";

    public static final String MAX_IMAGE_UPLOAD_EXCEEDS = "Image size cannot be greater than " + FileSize.MAX_IMAGE_SIZE + "MB";
    public static final String LINK_EXPIRED = "Link has been expired or invalid";
    public static final String SESSION_EXPIRED = "Session expired. Please login again.";
    public static final String TOKEN_REFRESHED_SUCCESS = "Access token refreshed successfully.";
    public static final String PHONE_NUMBER_ALREADY_TAKEN = "This phone number already belongs to another account";
    public static final String INVALID_ROLE = "Invalid role sent";
    public static final String SEND_OTP_SUCCESS = "Otp successfully sent to the user";
    public static final String OTP_VALIDATE_SUCCESS = "Otp validated successfully";
    public static final String OTP_VALIDATE_FAILURE = "Invalid otp sent";
    public static final String EMAIL_CANNOT_BE_EMPTY = "Email cannot be empty";
    public static final String USER_IS_NOT_ACTIVE = "User is not active";
    public static final String USERID_PASSWORD_NOTMATCH = "User ID and Password does not match";
    public static final String PROFILE_UPDATED = "Profile updated successfully.";
    public static final String CURRENT_PASSWORD_NOT_MATCH = "Current Password not matched";
    public static final String NEW_PASSWORD_RE_ENTER_PASSWORD_NOT_MATCH = "New Password and re-entered Password are not matched";
    public static final String VALID_TOKEN = "Valid token";
    public static final String INVALID_CONTENT_TYPE = "Invalid content type. Allow only jpeg, jpg and png type of images";
    public static final String PAYMENT_TOKEN_REQUIRED = "Card token not found. It is required for payment.";
    public static final String CARD_DETAILS_MISSING = "Card details are missing.";
    public static final String INVALID_DATA = "Provided JSON, data or message is not valid.";
    
    public static final String INVALID_OLD_ACCESS_CODE = "Old Access Code is incorrect.";
    public static final String ACCESS_CODE_CHANGED_SUCCESSFULLY = "Access Code changed successfully.";

    public static final String INVALID_EMAIL_ADDRESS = "Invalid email address.";
    public static final String ACCESS_CODE_SEND_SUCCESSFULLY = "Access Code send successfully.";
    public static final String ACCESS_CODE_SEND_ERROR = "Some exception occurred while send access code to mail.";
    
    public static final String USER_CREATED_ERROR = "Error occur while creating user.";
    public static final String USER_CREATED_SUCCESS = "User created successfully.";
    
    public static final String USER_EDITED_ERROR = "Error occur while editing user.";
    public static final String USER_EDITED_SUCCESS = "User edited successfully.";
    
    public static final String USER_DELETED_SUCCESS = "User deleted successfully.";
    public static final String USER_DELETED_ERROR = "Error occur while deleting user.";
    
    public static final String GET_PATIENT_PROFILE_DETAIL = "Patient profile detail get successfully.";
    public static final String ERROR_PATIENT_PROFILE_DETAIL = "Some error occured while getting patient profile detail.";
    
    public static final String GET_ANALYTICS_DATA = "analytics data fetched successfully.";
    public static final String ERROR_ANALYTICS_DATA = "Some error occured while fetching analytics data.";
    
    public static final String ADMIN_ADDED_ERROR = "Error occur while adding admin.";
    public static final String ADMIN_ADDED_SUCCESS = "Admin Added successfully.";
    
    public static final String ADMIN_EDITED_ERROR = "Error occur while editing Admin.";
    public static final String ADMIN_EDITED_SUCCESS = "Admin edited successfully.";

    public static final String GET_ADMIN_ERROR="";
    public static final String GET_ALL_ADMIN = "Get all admin.";
    public static final String ADMIN_NOT_FOUND = "Admin not found.";


    public static final String EXCEPTION_ALL_ADMIN = "Some exception occur while getting admin list.";
    public static final String ADMIN_GET_SUCCESSFULL = "Admin get successfully by admin id.";
    public static final String ADMIN_NOT_EXIST = "Admin does not exit";

    public static final String ADMIN_DELETED_ERROR="Admin deleted successfully.";

    public static final String EMAIL_ADDRESS_EXIST = "Email address already exist.";
    public static final String EMAIL_ADDRESS_NOT_EXIST = "Email address does not exist.";
    public static final String EMAIL_ADDRESS_ERROR = "Some exception occurred while check email availabilty.";
    public static final String EMAIL_ADDRESS_NOT_BLANK = "Email address cannot be null or blank.";
    
    private ResponseMessage() {
    }
}
