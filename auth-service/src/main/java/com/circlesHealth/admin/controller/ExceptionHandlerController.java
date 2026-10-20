package com.circlesHealth.admin.controller;

import com.circlesHealth.admin.utils.DateUtil;
import com.circlesHealth.admin.utils.constants.ResponseMessage;
import com.circlesHealth.admin.utils.enums.ResponseStatus;
import com.circlesHealth.admin.wrapper.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Handle all Exceptions in for All controllers
 */
@RestControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {
	
	private static final Logger LOG = LoggerFactory.getLogger(ExceptionHandlerController.class);

	@Bean
	public ErrorAttributes errorAttributes() {
		// Hide exception field in the return object
		return new DefaultErrorAttributes() {

			@Override
			public Map<String, Object> getErrorAttributes(WebRequest requestAttributes, boolean includeStackTrace) {
				Map<String, Object> errorAttributes = super.getErrorAttributes(requestAttributes, includeStackTrace);
				errorAttributes.remove("exception");
				return errorAttributes;
			}
		};
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<String> errorMessages = new ArrayList<>();
		ex.getBindingResult().getFieldErrors().forEach(
				fieldError -> errorMessages.add(fieldError.getField() + ": " + fieldError.getDefaultMessage()));
		return ResponseEntity.badRequest()
				.body(new BaseResponse(HttpStatus.BAD_REQUEST.value(), ResponseStatus.FAILURE.value(),
						ResponseMessage.VALIDATION_ERROR, DateUtil.getCurrentDateTime(), errorMessages, null));
	}

	@Override
	public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		return ResponseEntity.badRequest()
				.body(new BaseResponse(HttpStatus.BAD_REQUEST.value(), ResponseStatus.FAILURE.value(), 
						ResponseMessage.INVALID_DATA,
						DateUtil.getCurrentDateTime(), Collections.emptyList(), null));
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<BaseResponse> handleConstraintViolationException(ConstraintViolationException ex,
			WebRequest request) {
		return ResponseEntity.badRequest().body(new BaseResponse(HttpStatus.BAD_REQUEST.value(),
				ResponseStatus.FAILURE.value(), ResponseMessage.VALIDATION_ERROR, DateUtil.getCurrentDateTime(),
				ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.toList()),
				null));
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<BaseResponse> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new BaseResponse(HttpStatus.NOT_FOUND.value(), ResponseStatus.FAILURE.value(), ex.getMessage(),
						DateUtil.getCurrentDateTime(), Collections.emptyList(), null));
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<BaseResponse> handleBadCredentialException(BadCredentialsException ex, WebRequest request) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(new BaseResponse(HttpStatus.UNAUTHORIZED.value(), ResponseStatus.FAILURE.value(),
						ResponseMessage.INCORRECT_USERNAME_ACCESS_CODE, DateUtil.getCurrentDateTime(),
						Collections.emptyList(), null));
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<BaseResponse> handleAccessDeniedException(AccessDeniedException ex, WebRequest req) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN)
				.body(new BaseResponse(HttpStatus.FORBIDDEN.value(), ResponseStatus.FAILURE.value(),
						ResponseMessage.ACCESS_DENIED, DateUtil.getCurrentDateTime(), Collections.emptyList(), null));
	}

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<BaseResponse> handleMaxUploadSizeExceededException(
			MaxUploadSizeExceededException maxUploadException, WebRequest request) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
				.body(new BaseResponse(HttpStatus.BAD_REQUEST.value(), ResponseStatus.FAILURE.value(),
						ResponseMessage.MAX_IMAGE_UPLOAD_EXCEEDS, DateUtil.getCurrentDateTime(),
						Collections.emptyList(), null));
	}

	@ExceptionHandler(HttpServerErrorException.class)
	public ResponseEntity<BaseResponse> handleHttpServerErrorException(HttpServerErrorException ex, WebRequest req) {
		String httpMessage = ex.getMessage() == null ? ResponseMessage.SOMETHING_WENT_WRONG
				: ex.getMessage().replace(String.valueOf(ex.getStatusCode().value()), "").trim();
		return ResponseEntity.status(ex.getStatusCode())
				.body(new BaseResponse(ex.getStatusCode().value(), ResponseStatus.FAILURE.value(), httpMessage,
						DateUtil.getCurrentDateTime(), Collections.emptyList(), null));
	}

	@ExceptionHandler(InsufficientAuthenticationException.class)
	public ResponseEntity<BaseResponse> handleInsufficientAuthenticationException(
			InsufficientAuthenticationException ex, WebRequest req) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN.value())
				.body(new BaseResponse(HttpStatus.FORBIDDEN.value(), ResponseStatus.FAILURE.value(),
						ResponseMessage.INSUFFICIENT_AUTHENTICATION, DateUtil.getCurrentDateTime(),
						Collections.emptyList(), null));
	}

	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
				.body(new BaseResponse(HttpStatus.BAD_REQUEST.value(), ResponseStatus.FAILURE.value(),
						ResponseMessage.REQUEST_METHOD_NOT_SUPPORTED, DateUtil.getCurrentDateTime(),
						Collections.emptyList(), null));
	}

	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
				.body(new BaseResponse(HttpStatus.BAD_REQUEST.value(), ResponseStatus.FAILURE.value(),
						ResponseMessage.API_NOT_FOUND, DateUtil.getCurrentDateTime(), Collections.emptyList(), null));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<BaseResponse> handleAllException(Exception ex, WebRequest req) {
		LOG.error("Handled Internal Error Exception", ex);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.body(new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ResponseStatus.FAILURE.value(),
						ResponseMessage.SOMETHING_WENT_WRONG, DateUtil.getCurrentDateTime(), Collections.emptyList(),
						null));
	}

	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<BaseResponse> handleNullPointerException(Exception ex, WebRequest req) {
		LOG.error("Handled Null pointer exception Exception", ex);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
				.body(new BaseResponse(HttpStatus.BAD_REQUEST.value(), ResponseStatus.FAILURE.value(),
						ex.getLocalizedMessage(), DateUtil.getCurrentDateTime(), Collections.emptyList(),
						null));
	}
	
}
