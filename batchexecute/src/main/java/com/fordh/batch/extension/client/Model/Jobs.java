package com.fordh.batch.extension.client.Model;

import java.io.Serializable;

public class Jobs implements Serializable {

	Integer JobId=0;
	String JobName="";
	String DataCharter="";
	
	
	public Integer getJobId() {
		return JobId;
	}
	public void setJobId(Integer jobId) {
		JobId = jobId;
	}
	public String getJobName() {
		return JobName;
	}
	public void setJobName(String jobName) {
		JobName = jobName;
	}
	public String getDataCharter() {
		return DataCharter;
	}
	public void setDataCharter(String dataCharter) {
		DataCharter = dataCharter;
	}
	
			
	
	
}
