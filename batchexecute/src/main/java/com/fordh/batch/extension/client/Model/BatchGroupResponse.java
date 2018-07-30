package com.fordh.batch.extension.client.Model;

import java.io.Serializable;

public class BatchGroupResponse implements Serializable {

	
	private MDMJobResponseList mymdmjobGroupResponse;
	private PCJobResponse mypcJobResponse;
	private DIHJobResponse mydihJobResponse;
	private Integer Sequence;
	
	
	public DIHJobResponse getMydihJobResponse() {
		return mydihJobResponse;
	}
	public void setMydihJobResponse(DIHJobResponse mydihJobResponse) {
		this.mydihJobResponse = mydihJobResponse;
	}

	
	
	public MDMJobResponseList getMymdmjobGroupResponse() {
		return mymdmjobGroupResponse;
	}
	public void setMymdmjobGroupResponse(MDMJobResponseList mymdmjobGroupResponse) {
		this.mymdmjobGroupResponse = mymdmjobGroupResponse;
	}
	public PCJobResponse getMypcJobResponse() {
		return mypcJobResponse;
	}
	public void setMypcJobResponse(PCJobResponse mypcJobResponse) {
		this.mypcJobResponse = mypcJobResponse;
	}
	public Integer getSequence() {
		return Sequence;
	}
	public void setSequence(Integer sequence) {
		Sequence = sequence;
	}
	
	
}
