package com.circles.health.patient.controller;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.circles.health.patient.entity.BlogAnalyticsData;
import com.circles.health.patient.entity.BlogArticle;
import com.circles.health.patient.request.BlogAnalyticsRequest;
import com.circles.health.patient.service.BlogArticleService;
import com.circles.health.patient.utils.DateUtil;
import com.circles.health.patient.utils.constants.EndPoints;
import com.circles.health.patient.utils.constants.ResponseMessage;
import com.circles.health.patient.utils.enums.ResponseStatus;
import com.circles.health.patient.wrapper.BaseResponse;

@RestController
@RequestMapping(EndPoints.BASE_PATH)
public class BlogController {

	private static final Logger LOGGER = LoggerFactory.getLogger(BlogController.class);
	
	@Autowired
	private BlogArticleService blogArticleService;
	
	@GetMapping("/blogs")
	public ResponseEntity<BaseResponse> getAllBlog() {
		LOGGER.info("Inside getAllBlog method of BlogController.");
		List<BlogArticle> blogs = null;
		try {
			
			blogs = this.blogArticleService.getBlogs();
			return ResponseEntity.status(HttpStatus.OK).body(
					new BaseResponse(HttpStatus.OK.value(),ResponseStatus.SUCCESS.value(),
							ResponseMessage.GET_ALL_BLOGS,DateUtil.getCurrentDateTime(),
						Collections.emptyList(), blogs));
			
		}catch(Exception exception) {
			LOGGER.info(ResponseMessage.EXCEPTION_ALL_BLOGS+exception.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),ResponseStatus.FAILURE.value(),
						ResponseMessage.EXCEPTION_ALL_BLOGS+exception.getMessage(),DateUtil.getCurrentDateTime(),
					Collections.emptyList(), null));
		}
	}
	
	@PostMapping("/setBlogForAnalytics")
	public ResponseEntity<BaseResponse> setBlogForAnalytics(@RequestBody BlogAnalyticsRequest blogAnalyticsRequest) {
		LOGGER.info("Inside setBlogForAnalytics method of BlogController.");
		BlogAnalyticsData blogAnalyticsdata = null;
		try {
			
			blogAnalyticsdata = this.blogArticleService.setBlogForAnalytics(blogAnalyticsRequest);
			return ResponseEntity.status(HttpStatus.OK).body(
					new BaseResponse(HttpStatus.OK.value(),ResponseStatus.SUCCESS.value(),
							ResponseMessage.SET_BLOG_FOR_ANALYTICS,DateUtil.getCurrentDateTime(),
						Collections.emptyList(), blogAnalyticsdata));
			
		}catch(Exception exception) {
			LOGGER.info(ResponseMessage.EXCEPTION_BLOG_FOR_ANALYTICS+exception.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),ResponseStatus.FAILURE.value(),
						ResponseMessage.EXCEPTION_BLOG_FOR_ANALYTICS+exception.getMessage(),DateUtil.getCurrentDateTime(),
					Collections.emptyList(), null));
		}
	}
}
