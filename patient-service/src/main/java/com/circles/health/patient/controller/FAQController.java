package com.circles.health.patient.controller;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.circles.health.patient.entity.FAQ;
import com.circles.health.patient.service.FAQService;
import com.circles.health.patient.utils.DateUtil;
import com.circles.health.patient.utils.constants.EndPoints;
import com.circles.health.patient.utils.constants.ResponseMessage;
import com.circles.health.patient.utils.enums.ResponseStatus;
import com.circles.health.patient.wrapper.BaseResponse;

@RestController
@RequestMapping(EndPoints.BASE_PATH + "/faq")
public class FAQController {

	private static final Logger LOGGER = LoggerFactory.getLogger(FAQController.class);
	
	@Autowired
	private FAQService faqService;
	
	@GetMapping("/faqs")
	public ResponseEntity<BaseResponse> getAllFAQ() {
		LOGGER.info("Inside getAllFAQ method.");
		try {
			
			List<FAQ> faqs = this.faqService.getAllFAQ();
			
			return ResponseEntity.status(HttpStatus.OK).body(
					new BaseResponse(HttpStatus.OK.value(),ResponseStatus.SUCCESS.value(),
							ResponseMessage.GET_ALL_FAQS,DateUtil.getCurrentDateTime(),
						Collections.emptyList(), faqs));
			
		}catch(Exception exception) {
			LOGGER.info(ResponseMessage.EXCEPTION_ALL_FAQS+exception.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),ResponseStatus.FAILURE.value(),
						ResponseMessage.EXCEPTION_ALL_FAQS+exception.getMessage(),DateUtil.getCurrentDateTime(),
					Collections.emptyList(), null));
		}
	}
}
