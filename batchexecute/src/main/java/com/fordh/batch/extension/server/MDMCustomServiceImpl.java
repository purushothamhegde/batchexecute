package com.fordh.batch.extension.server;


import com.fordh.batch.extension.client.Model.BatchGroupResponse;
import com.fordh.batch.extension.client.Model.DIHJobResponse;
import com.fordh.batch.extension.client.Model.JobList;
import com.fordh.batch.extension.client.Model.Jobs;
import com.fordh.batch.extension.client.Model.MDMJobResponse;
import com.fordh.batch.extension.client.Model.MDMJobResponseList;
import com.fordh.batch.extension.client.Model.PCJobResponse;
import com.fordh.batch.extension.client.service.IMDMCustomBatchService;
import com.fordh.batch.extension.client.service.IMDMCustomServiceClient;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.thirdparty.json.JSONObject;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;


import javax.xml.soap.*;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MDMCustomServiceImpl extends RemoteServiceServlet implements IMDMCustomBatchService{
	public static Logger logger = Logger.getLogger(MDMCustomServiceImpl.class.getName());
	
	
	private JobList myjoblist = new JobList();
	private List<Jobs> myJoblist ;
	private BatchGroupResponse myBatchGroupStatus;

		
	
	
	@Override
	public BatchGroupResponse ExecuteBatchGroup(String sBatchGroup) throws RequestException {
		// TODO Auto-generated method stub
		myBatchGroupStatus= new BatchGroupResponse();
		ResultSet myrs ;
		String Res="";
		String sQuery ="";
		sQuery = " SELECT SEQUENCE_1,JOB_TYPE,JOB_NAME FROM C_L_JOB_DETAIL WHERE JOB_ID="+ sBatchGroup  +" ORDER BY SEQUENCE_1 ";
		
		logger.info("Job List Query::" + sQuery  );
		
		RunSQL mysql = new RunSQL();
	    try {
			myrs = mysql.ExecuteQuery(sQuery);

			try{
				while (myrs.next())
				{
					logger.info("Job Type::" + myrs.getString(2));
					
					if (myrs.getString(2).equals("MDM")) {
						RunMDMJobs mymdmjob = new RunMDMJobs();
						MDMJobResponseList mymdmJobGroupResponse = new MDMJobResponseList();
						Res= Res +  myrs.getString(2) + "Job Status :" + mymdmjob.ExecuteBatchGroup(myrs.getString(3)) + "\n";
						logger.info(Res);
						// Now let us find the status using the C_REPOS Tables.
						String rowidBatchGroup="";
						int intstartpos=0;
						int intendpos=0;

						
						intstartpos=Res.indexOf("<rowidBatchGroupLog>")+20;
						intendpos=Res.indexOf("</rowidBatchGroupLog>");
						rowidBatchGroup= Res.substring(intstartpos, intendpos);
						logger.info("Batch Group Id ::" + rowidBatchGroup);
						
						sQuery="SELECT C_REPOS_JOB_GROUP_CONTROL.ROWID_JOB_GROUP ,C_REPOS_JOB_GROUP.JOB_GROUP_NAME ,C_REPOS_JOB_CONTROL.TABLE_DISPLAY_NAME, C_REPOS_OBJ_FUNCTION_TYPE.OBJECT_FUNCTION_TYPE_DESC, ";
						sQuery=sQuery + " C_REPOS_JOB_CONTROL.STATUS_MESSAGE,C_REPOS_JOB_METRIC_TYPE.METRIC_TYPE_CODE,METRIC_TYPE_DESC,METRIC_VALUE ";
						sQuery=sQuery + " FROM C_REPOS_JOB_METRIC ,C_REPOS_JOB_METRIC_TYPE,C_REPOS_OBJ_FUNCTION_TYPE,C_REPOS_JOB_CONTROL,C_REPOS_JOB_GROUP_CONTROL,C_REPOS_JOB_GROUP ";
						sQuery=sQuery + " WHERE " ;
						sQuery=sQuery + " C_REPOS_JOB_METRIC_TYPE.METRIC_TYPE_CODE=C_REPOS_JOB_METRIC.METRIC_TYPE_CODE AND " ;
						sQuery=sQuery + " C_REPOS_JOB_METRIC.ROWID_JOB=C_REPOS_JOB_CONTROL.ROWID_JOB AND METRIC_VALUE >0 AND " ;
						sQuery=sQuery + " C_REPOS_JOB_CONTROL.OBJECT_FUNCTION_TYPE_CODE=C_REPOS_OBJ_FUNCTION_TYPE.OBJECT_FUNCTION_TYPE_CODE AND " ;
						sQuery=sQuery + " C_REPOS_JOB_CONTROL.ROWID_JOB_GROUP_CONTROL=C_REPOS_JOB_GROUP_CONTROL.ROWID_JOB_GROUP_CONTROL AND " ;
						sQuery=sQuery + " C_REPOS_JOB_GROUP_CONTROL.ROWID_JOB_GROUP =C_REPOS_JOB_GROUP.ROWID_JOB_GROUP AND " ;
						sQuery=sQuery + " C_REPOS_JOB_CONTROL.ROWID_JOB_GROUP_CONTROL='" +  rowidBatchGroup + "' ORDER BY C_REPOS_OBJ_FUNCTION_TYPE.OBJECT_FUNCTION_TYPE_DESC,C_REPOS_JOB_CONTROL.TABLE_DISPLAY_NAME";
						
						List <MDMJobResponse> mymdmjoblist ;
						
						mymdmjoblist= new ArrayList<MDMJobResponse>();
						
						ResultSet myrs2 ;
						myrs2= mysql.ExecuteQuery(sQuery);
						while (myrs2.next()) {
							MDMJobResponse mymdmjobres = new MDMJobResponse();
							mymdmjobres.setJobGroupName(myrs2.getString(2));
							mymdmjobres.setTableDisplayName(myrs2.getString(3));
							mymdmjobres.setJobStage(myrs2.getString(4));
							mymdmjobres.setJobStageStatus(myrs2.getString(5));
							mymdmjobres.setJobStageMetricName(myrs2.getString(7));
							mymdmjobres.setJobStageMetricValue(myrs2.getString(8));
							mymdmjoblist.add(mymdmjobres);
							
						}
						myBatchGroupStatus.setSequence(myrs.getInt(1));
						mymdmJobGroupResponse.setMdmresponselist(mymdmjoblist);
						myBatchGroupStatus.setMymdmjobGroupResponse(mymdmJobGroupResponse);
							
					}
					
					else if(myrs.getString(2).equals("PC")) {
						RunPCJobs mypcjob = new RunPCJobs();
						try {
							Res= mypcjob.RunPCJob(myrs.getString(3));
							logger.info(Res);
							
						
							PCJobResponse myPCJobStatus= new PCJobResponse();
							myPCJobStatus.setPcjobresponse(Res);
							myBatchGroupStatus.setSequence(myrs.getInt(1));
							myBatchGroupStatus.setMypcJobResponse(myPCJobStatus);
							
							
							
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
					
					else if(myrs.getString(2).trim().equals("DIH")) {
						RunDIHJobs mydihjob = new RunDIHJobs();
						try {
							logger.info("DIH Call Started::" );
							
							Res= mydihjob.RunDIHJob(myrs.getString(3),"publication");
							logger.info("DIH Soap Call Response::" + Res);

							// The Res is the SOAP Response, We need to pick the Event Id from it and then do a REST API call to get the status
						
							//         <eventId>265570</eventId>

							if (Res.contains(" <status>SUCCESS</status>")) 
							{
								logger.info("Triggered the DIH Job , Now finidng out the Status");
								Integer intstartpos=Res.indexOf("<eventId>")+9 ;
								Integer intendpos=Res.indexOf("</eventId>");
								String sEventId="";
								sEventId= Res.substring(intstartpos, intendpos);
								
								// We now have the Event Id So next step is to find the Status
								
								String sRes=mydihjob.GetEventStatus(sEventId);
								
								logger.info("Event Status Response::" + sRes);
								
								DIHJobResponse myDIHJobStatus= new DIHJobResponse();
								
								ObjectMapper mapper = new ObjectMapper();
							    DIHJobResponse p = mapper.readValue(sRes,  DIHJobResponse.class);
							        		
							    p.getEventStatus();	
								myDIHJobStatus.setDIHResponse(sRes);
								myBatchGroupStatus.setSequence(myrs.getInt(1));
								myBatchGroupStatus.setMydihJobResponse(myDIHJobStatus);
							}
							else
							{
								//Something went wrong let us inform the user about it. 
								
								logger.info("Unable to Trigger the DIH Job !!");
								
							}
							
							
						} 
						
						
						catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
			
			}
		
				
			}
			finally {
		
				myrs.close();
			}

	    } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		return myBatchGroupStatus;
	}



	@Override
	public JobList GetJobliist() {
		// TODO Auto-generated method stub
		logger.info("Inside Method :ViewJobList");

		ResultSet myrs ;
		myJoblist= new ArrayList<Jobs>();
		
		String sQuery ="";
		sQuery =" SELECT DATA_CHARTER,JOB_ID,JOB_NAME FROM C_L_JOB ORDER BY INTEGER (JOB_ID)";
		
		logger.info("Job List Query::" + sQuery  );
				
		try {
			
			RunSQL mysql = new RunSQL();
		    myrs = mysql.ExecuteQuery(sQuery);
		    
		    
			try{
				while (myrs.next())
				{
					Jobs jobrecord = new Jobs();

					jobrecord.setDataCharter(myrs.getString(1));
					jobrecord.setJobId(myrs.getInt(2));
					jobrecord.setJobName(myrs.getString(3));
					
					myJoblist.add(jobrecord);
					
			}
				myjoblist.setJobList(myJoblist);		
				
			}
			finally {
		
				myrs.close();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return myjoblist ;	
		

	}
		
}
