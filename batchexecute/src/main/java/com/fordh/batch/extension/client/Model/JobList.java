package com.fordh.batch.extension.client.Model;

import java.io.Serializable;
import java.util.List;



public class JobList implements Serializable {

	private  List<Jobs> JobList ;

	public List<Jobs> getJobList() {
		return JobList;
	}

	public void setJobList(List<Jobs> jobList) {
		JobList = jobList;
	}
	
	
	
}
