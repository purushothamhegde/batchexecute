package com.fordh.batch.extension.server;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class RunMDMJobs {

	private String sifUrl = "";
	private String orsId = "";
	private String username = "";
	private String password = "";
    
	public static Logger logger = Logger.getLogger(RunMDMJobs.class.getName());
	public static String fileToRead = "/apps/fordh_config/fordh_config.properties";
	//public static String fileToRead = "/home/10293762/fordh_config.properties";
	
	private void GetRequiredProperties() {


		
		Properties prop = new Properties();
		
        try {
            
            File file = new File(fileToRead);
            
            if (file.exists()) {
                logger.info("Config file exists");
            } else {
                logger.error("Exception :: GetRequiredProperties :: Config file not found");
                throw new RuntimeException("Exception :: GetRequiredProperties :: Config file not found");
            }
            
            prop.load(new FileInputStream(file));

        } catch (Exception e) {

            logger.error("Exception :: GetRequiredProperties :: " + e.getMessage(), e);

            throw new RuntimeException("Exception :: GetRequiredProperties :: " + e.getMessage());
        }

	    sifUrl = prop.getProperty("MDM_SIF_URL");
	    orsId = prop.getProperty("MDM_ORS_ID");
	    username = prop.getProperty("MDM_USER_NAME");
	    password = prop.getProperty("MDM_PASSWORD");
	    
	    logger.info("SIF URL ::" + sifUrl);
	    logger.info("ORS ID ::" + orsId );
	    logger.info("User Id ::" + username);
	    logger.info("Password ::" + password);
	    
	
	}
	

	
	
	public String FetchBaseObjects() throws IOException {
		
		GetRequiredProperties();
		String strtext = CallService(sifUrl, orsId, username, password,"ExecuteFetchBaseObjects",  "BASE_OBJECT");
		return strtext ;
		
	}

	public String ExecuteBatchGroup(String BatchGroupName) {
		
		GetRequiredProperties();
		String strtext="";
		try {
			strtext = CallService(sifUrl, orsId, username, password,"ExecuteBatchGroup",  BatchGroupName);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return strtext ;
		
	}
	
	
	private String CallService(String sUrl,String OrsId, String UserId, String Password, String ServiceName , String ServiceParam) throws IOException {
		
		String responseString = "";
		String outputString = "";
		URL url = new URL(sUrl);
		
		URLConnection connection = url.openConnection();
		HttpURLConnection httpConn = (HttpURLConnection)connection;
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		String xmlInput ="";
		
		logger.info("Inside CallService::" );
	    
		
		if (ServiceName.equals("ExecuteFetchBaseObjects")){
			
			xmlInput=ExecuteFetchBaseObjects(sUrl, OrsId, UserId, Password,ServiceName,  ServiceParam);
			
		}
		else if (ServiceName.equals("ExecuteBatchGroup")) {
			
			xmlInput=ExecuteBatchGroup(sUrl, OrsId, UserId, Password,ServiceName,  ServiceParam);
			
		}
		
		
		
		byte[] buffer = new byte[xmlInput.length()];
		buffer = xmlInput.getBytes();
		bout.write(buffer);
		byte[] b = bout.toByteArray();
		
		
		String SOAPAction ="a";
		
		logger.info("SOAP Message ::" + xmlInput );
		
		
		httpConn.setRequestProperty("Content-Length",
		String.valueOf(b.length));
		httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
		httpConn.setRequestProperty("SOAPAction", SOAPAction);
		httpConn.setRequestMethod("POST");
		httpConn.setDoOutput(true);
		httpConn.setDoInput(true);
		OutputStream out = httpConn.getOutputStream();
		
		out.write(b);
		out.close();
		
		
		InputStreamReader isr = new InputStreamReader(httpConn.getInputStream());
		BufferedReader in = new BufferedReader(isr);
//Write the SOAP message response to a String.
		while ((responseString = in.readLine()) != null) {
		outputString = outputString + responseString;
		}
		logger.info("SOAP Raw Response ::" + outputString );
		
		return outputString;
		
	}
	
private String ExecuteFetchBaseObjects(String sUrl,String OrsId, String UserId, String Password, String ServiceName , String ServiceParam)  {
		
		String strmlPayload ="";
		strmlPayload=" <soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:siperian.api\">\n" ;
		strmlPayload = strmlPayload + "<soapenv:Header/>";
	    strmlPayload = strmlPayload + "<soapenv:Body>";
	    strmlPayload = strmlPayload + "<urn:listSiperianObjects>";
	    strmlPayload = strmlPayload + "<urn:username>" + UserId + "</urn:username><urn:password><urn:password>" + Password + "</urn:password><urn:encrypted>false</urn:encrypted></urn:password> <urn:userResourcesOnly>TRUE</urn:userResourcesOnly>";
	    strmlPayload = strmlPayload + "<urn:orsId>" + OrsId + "</urn:orsId><urn:objectType>" + ServiceParam + "</urn:objectType>";
	    strmlPayload = strmlPayload + "</urn:listSiperianObjects></soapenv:Body></soapenv:Envelope>";
	    
		return strmlPayload;
		
	}
	

private String ExecuteBatchGroup(String sUrl,String OrsId, String UserId, String Password, String ServiceName , String ServiceParam)  {
	
	String strmlPayload ="";
	strmlPayload=" <soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:siperian.api\">\n" ;
	strmlPayload = strmlPayload + "<soapenv:Header/>";
    strmlPayload = strmlPayload + "<soapenv:Body>";
    strmlPayload = strmlPayload + "<urn:executeBatchGroup>";
    strmlPayload = strmlPayload + "<urn:username>" + UserId + "</urn:username><urn:password><urn:password>" + Password + "</urn:password><urn:encrypted>false</urn:encrypted></urn:password>";
    strmlPayload = strmlPayload + "<urn:orsId>" + OrsId + "</urn:orsId><urn:batchGroupUid>" + ServiceParam + "</urn:batchGroupUid> <urn:resume>No</urn:resume>";
    strmlPayload = strmlPayload + "</urn:executeBatchGroup></soapenv:Body></soapenv:Envelope>";
    
	return strmlPayload;
	
}









/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 * 
	 * @param html the html string to escape
	 * @return the escaped string
	 */
	
	
	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
	}

	
}
