package com.circles.health.patient.service;

import java.util.List;

import com.circles.health.patient.entity.BlogAnalyticsData;
import com.circles.health.patient.entity.BlogArticle;
import com.circles.health.patient.request.BlogAnalyticsRequest;

public interface BlogArticleService {

	List<BlogArticle> getBlogs();
	
	BlogAnalyticsData setBlogForAnalytics(BlogAnalyticsRequest blogAnalyticsRequest);
}
