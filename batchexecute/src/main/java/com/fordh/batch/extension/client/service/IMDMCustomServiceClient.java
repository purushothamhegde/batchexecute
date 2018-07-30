package com.fordh.batch.extension.client.service;

import com.fordh.batch.extension.client.Model.JobList;

public interface IMDMCustomServiceClient {

	
	void GetJobList();
	
	void ExecuteBatchGroup(String sBatchGroup);
	
}
