package com.fordh.batch.extension.client;


import java.util.List;

import com.fordh.batch.extension.client.Model.BatchGroupResponse;
import com.fordh.batch.extension.client.Model.JobList;
import com.fordh.batch.extension.client.Model.Jobs;
import com.fordh.batch.extension.client.Model.MDMJobResponse;
import com.fordh.batch.extension.client.Model.MDMJobResponseList;
import com.fordh.batch.extension.client.Model.PCJobResponse;
import com.fordh.batch.extension.client.service.MDMCustomServiceClientImpl;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Fordh_BatchExecute implements EntryPoint {
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network " + "connection and try again.";

	private MDMCustomServiceClientImpl serviceImpl;
	
	private HTML myhtml ;
	private VerticalPanel vPanel = new VerticalPanel();
	private TextBox txtBatchGroup ;
	private Label lblResult;
	private ListBox cmbhllist;
	private HorizontalPanel hPanel1 = new HorizontalPanel();
	
	public void onModuleLoad() {
		
	serviceImpl = new MDMCustomServiceClientImpl(GWT.getModuleBaseURL()+ "mdmcustomservice", this);

		
		this.cmbhllist= new ListBox();
		this.hPanel1.add(cmbhllist);

		serviceImpl.GetJobList();
		this.vPanel.add(hPanel1);
		
		Button btnExecuetBatchGroup = new Button("Run Batch Group");
		this.vPanel.add(btnExecuetBatchGroup);
		btnExecuetBatchGroup.addClickHandler(new btnExecuetBatchGroupClickHandler());
		
		this.myhtml= new HTML();
		this.vPanel.add(myhtml);
		RootPanel.get().add(vPanel);
		
		RootPanel.get().add(vPanel);
	}
	
	
	private class btnExecuetBatchGroupClickHandler implements ClickHandler{

		@Override
		public void onClick(ClickEvent event) {
			
			
			String sBatchGroup = cmbhllist.getSelectedValue();
			serviceImpl.ExecuteBatchGroup(sBatchGroup);
			
		}
	}

	public void PopulateJobs(JobList mylist) {
		
		List <Jobs> myList = mylist.getJobList();
		
		for (int i=0; i<= myList.size()-1; i++)
		{
			Jobs myoption= myList.get(i);
			this.cmbhllist.addItem(myoption.getJobName(),myoption.getJobId().toString());
			
		}
		
	}
	

	
	public void ShowBatchStatus(BatchGroupResponse response) {
		
		MDMJobResponseList mylist = response.getMymdmjobGroupResponse();
		
		List<MDMJobResponse> mymdmList= mylist.getMdmresponselist();
		
		String content="<table id='results'><tr><th>Job Group Name</th><th>Table Name</th><th>Job Type</th><th>Status</th><th>Status Type</th><th>Records Processed</th></tr>";
		String s="";
		for (int i=0; i<= mymdmList.size()-1; i++)
		{
			MDMJobResponse mdmjob= mymdmList.get(i);
			if ( Integer.parseInt( mdmjob.getJobStageMetricValue()) >0) 
			{
				content=content +"<tr>";
				content=content + "<td>" +  mdmjob.getJobGroupName() + "</td>" ;
				content=content + "<td>" +  mdmjob.getTableDisplayName()+ "</td>" ;
				
				s= mdmjob.getJobStage();
				s= s.substring(s.indexOf("|")+1, s.length() );
				content=content + "<td>" +  s + "</td>" ;
				
				content=content + "<td>" +  mdmjob.getJobStageStatus() + "</td>" ;
				
				s= mdmjob.getJobStageMetricName();
				s= s.substring(s.indexOf("|")+1, s.length() );
				content=content + "<td>" +  s + "</td>" ;
				
				content=content + "<td>" +  mdmjob.getJobStageMetricValue() + "</td>" ;
				content=content +"</tr>";
			}
		}
		
		content=content + "</table>";
//		PCJobResponse mypcresponse = response.getMypcJobResponse();
//		content=content + "<table id='results'><tr><th>PC Job Status</th></tr>";
//		content=content + "<tr><td>" + mypcresponse.getPcjobresponse() +"</td></tr>";
//		content=content + "</table>";
		this.myhtml.setHTML(content);		
		
	}

}