package com.circles.health.patient.response;

import java.util.List;

public class WellnessQuestion {
	
	
	private Long userId;
	private List<WellnessInfo> wellnessInfo;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public List<WellnessInfo> getWellnessInfo() {
		return wellnessInfo;
	}
	public void setWellnessInfo(List<WellnessInfo> wellnessInfo) {
		this.wellnessInfo = wellnessInfo;
	}


}
