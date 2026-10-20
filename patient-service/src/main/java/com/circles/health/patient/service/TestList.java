package com.circles.health.patient.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.circles.health.patient.response.AnalyticsExitAndBlogPageDetails;

public class TestList {

	public static void main(String[] args) {
		List<AnalyticsExitAndBlogPageDetails> exitPageDetails =new ArrayList<>();
		AnalyticsExitAndBlogPageDetails analyticsExitAndBlogPageDetails1 = new AnalyticsExitAndBlogPageDetails();
		analyticsExitAndBlogPageDetails1.setPageCount("1");
		analyticsExitAndBlogPageDetails1.setPagePath("/page1");
		AnalyticsExitAndBlogPageDetails analyticsExitAndBlogPageDetails2 = new AnalyticsExitAndBlogPageDetails();
		analyticsExitAndBlogPageDetails2.setPageCount("2");
		analyticsExitAndBlogPageDetails1.setPagePath("/page2");
		AnalyticsExitAndBlogPageDetails analyticsExitAndBlogPageDetails3 = new AnalyticsExitAndBlogPageDetails();
		analyticsExitAndBlogPageDetails3.setPageCount("3");
		analyticsExitAndBlogPageDetails3.setPagePath("/page3");
		exitPageDetails = Arrays.asList(analyticsExitAndBlogPageDetails1,analyticsExitAndBlogPageDetails2,analyticsExitAndBlogPageDetails3);
		System.out.println("Before sorting ::: "+exitPageDetails);
		Collections.sort(exitPageDetails, Collections.reverseOrder());
		System.out.println("After sorting ::: "+exitPageDetails);
		
	}

}
