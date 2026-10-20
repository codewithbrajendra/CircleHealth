package com.circles.health.patient.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.circles.health.patient.entity.FAQ;
import com.circles.health.patient.repository.FAQRepository;

@Service
public class FAQServiceImpl implements FAQService {

	@Autowired
	private FAQRepository faqRepository;
	
	@Override
	public List<FAQ> getAllFAQ() {
		
		return this.faqRepository.findAll();
	}

}
