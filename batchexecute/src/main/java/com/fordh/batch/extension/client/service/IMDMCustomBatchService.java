package com.fordh.batch.extension.client.service;

import com.fordh.batch.extension.client.Model.BatchGroupResponse;
import com.fordh.batch.extension.client.Model.JobList;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("mdmcustomservice")
public interface IMDMCustomBatchService extends RemoteService {

	BatchGroupResponse ExecuteBatchGroup(String sBatchGroup) throws RequestException;
	JobList GetJobliist();
	
	
	
}
