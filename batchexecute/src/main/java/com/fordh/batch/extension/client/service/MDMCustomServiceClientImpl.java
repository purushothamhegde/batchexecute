package com.fordh.batch.extension.client.service;


import com.fordh.batch.extension.client.Fordh_BatchExecute;
import com.fordh.batch.extension.client.Model.BatchGroupResponse;
import com.fordh.batch.extension.client.Model.JobList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;


public class MDMCustomServiceClientImpl implements IMDMCustomServiceClient {
	private IMDMCustomBatchServiceAsync service;
	
	private Fordh_BatchExecute maingui;
	

	
	
	public  MDMCustomServiceClientImpl(String url,Fordh_BatchExecute uicomp) {
		System.out.println("Posting to the following url " + url);
		this.service= GWT.create(IMDMCustomBatchService.class);
		
		ServiceDefTarget endpoint = (ServiceDefTarget) this.service;
		endpoint.setServiceEntryPoint(url);
		this.maingui=uicomp;
	}
	

	
	
	private class DefaultCallback implements AsyncCallback {

		@Override
		public void onFailure(Throwable caught) {
			System.out.println("Error!!!");
			
		}

		@Override
		public void onSuccess(Object result) {
			if (result instanceof JobList) {
				JobList mylist= (JobList)result;
				maingui.PopulateJobs(mylist);
				
			}
			
			else if (result instanceof BatchGroupResponse) {

				BatchGroupResponse myresponse = (BatchGroupResponse) result;
				maingui.ShowBatchStatus(myresponse);
				
			}
		}
	}

	@Override
	public void GetJobList() {
		service.GetJobliist(new DefaultCallback());
	}




	@Override
	public void ExecuteBatchGroup(String sBatchGroup) {
		// TODO Auto-generated method stub
		service.ExecuteBatchGroup(sBatchGroup, new DefaultCallback());
	}





}
