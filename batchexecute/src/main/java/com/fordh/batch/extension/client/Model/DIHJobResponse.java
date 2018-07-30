package com.fordh.batch.extension.client.Model;

import java.io.Serializable;

public class DIHJobResponse implements Serializable {

	private String DIHResponse ="";

	public String getDIHResponse() {
		return DIHResponse;
	}

	public void setDIHResponse(String dIHResponse) {
		DIHResponse = dIHResponse;
	}
	
	
	
	
	 private String responseCode;

	  public String getResponseCode() { return this.responseCode; }

	  public void setResponseCode(String responseCode) { this.responseCode = responseCode; }

	  private int eventId;

	  public int getEventId() { return this.eventId; }

	  public void setEventId(int eventId) { this.eventId = eventId; }

	  private String eventType;

	  public String getEventType() { return this.eventType; }

	  public void setEventType(String eventType) { this.eventType = eventType; }

	  private String topicName;

	  public String getTopicName() { return this.topicName; }

	  public void setTopicName(String topicName) { this.topicName = topicName; }

	  private String publicationName;

	  public String getPublicationName() { return this.publicationName; }

	  public void setPublicationName(String publicationName) { this.publicationName = publicationName; }

	  private String applicationName;

	  public String getApplicationName() { return this.applicationName; }

	  public void setApplicationName(String applicationName) { this.applicationName = applicationName; }

	  private String eventStatus;

	  public String getEventStatus() { return this.eventStatus; }

	  public void setEventStatus(String eventStatus) { this.eventStatus = eventStatus; }

	  private long eventStartTimeLong;

	  public long getEventStartTimeLong() { return this.eventStartTimeLong; }

	  public void setEventStartTimeLong(long eventStartTimeLong) { this.eventStartTimeLong = eventStartTimeLong; }

	  private long eventEndTimeLong;

	  public long getEventEndTimeLong() { return this.eventEndTimeLong; }

	  public void setEventEndTimeLong(long eventEndTimeLong) { this.eventEndTimeLong = eventEndTimeLong; }

	  private boolean isFinal;

	  public boolean getIsFinal() { return this.isFinal; }

	  public void setIsFinal(boolean isFinal) { this.isFinal = isFinal; }

	  private boolean isError;

	  public boolean getIsError() { return this.isError; }

	  public void setIsError(boolean isError) { this.isError = isError; }
	
}
