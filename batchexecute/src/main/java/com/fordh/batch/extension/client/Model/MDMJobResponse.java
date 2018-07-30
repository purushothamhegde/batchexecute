package com.fordh.batch.extension.client.Model;

import java.io.Serializable;

public class MDMJobResponse implements Serializable{

	private String JobGroupName="";
	private String TableDisplayName="";
	private String JobStage="";
	private String JobStageStatus="";
	private String JobStageMetricName="";
	private String JobStageMetricValue="";

	
	public String getJobGroupName() {
		return JobGroupName;
	}
	public void setJobGroupName(String jobGroupName) {
		JobGroupName = jobGroupName;
	}
	public String getTableDisplayName() {
		return TableDisplayName;
	}
	public void setTableDisplayName(String tableDisplayName) {
		TableDisplayName = tableDisplayName;
	}
	public String getJobStage() {
		return JobStage;
	}
	public void setJobStage(String jobStage) {
		JobStage = jobStage;
	}
	public String getJobStageStatus() {
		return JobStageStatus;
	}
	public void setJobStageStatus(String jobStageStatus) {
		JobStageStatus = jobStageStatus;
	}
	public String getJobStageMetricName() {
		return JobStageMetricName;
	}
	public void setJobStageMetricName(String jobStageMetricName) {
		JobStageMetricName = jobStageMetricName;
	}
	public String getJobStageMetricValue() {
		return JobStageMetricValue;
	}
	public void setJobStageMetricValue(String jobStageMetricValue) {
		JobStageMetricValue = jobStageMetricValue;
	}
	
	
}
