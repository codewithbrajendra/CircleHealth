package com.circles.health.patient.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.circles.health.patient.entity.BlogAnalyticsData;
import com.circles.health.patient.entity.BlogArticle;
import com.circles.health.patient.repository.BlogAnalyticsDataRepo;
import com.circles.health.patient.repository.BlogArticleRepository;
import com.circles.health.patient.request.BlogAnalyticsRequest;
import com.circles.health.patient.utils.DateUtil;

@Service
public class BlogArticleServiceImpl implements BlogArticleService {

	private static final Logger LOGGER = LoggerFactory.getLogger(BlogArticleServiceImpl.class);
	
	@Autowired
	private BlogArticleRepository blogArticleRepository;
	
	@Autowired
	private BlogAnalyticsDataRepo blogAnalyticsDataRepo;
	
	@Override
	public List<BlogArticle> getBlogs() {
		LOGGER.info("Inside getBlogs method of  BlogArticleService.");
		return this.blogArticleRepository.findAll();
	}

	@Override
	public BlogAnalyticsData setBlogForAnalytics(BlogAnalyticsRequest blogAnalyticsRequest) {
		LOGGER.info("Inside setBlogForAnalytics method of  BlogArticleService.");
		BlogAnalyticsData blogAnalyticsData = new BlogAnalyticsData();
		blogAnalyticsData.setBlogNo(blogAnalyticsRequest.getBlogNo());
		blogAnalyticsData.setUserId(blogAnalyticsRequest.getUserId());
		blogAnalyticsData.setBlogClickingTime(DateUtil.getCurrentDateTime());
		return this.blogAnalyticsDataRepo.save(blogAnalyticsData);
	}

}
