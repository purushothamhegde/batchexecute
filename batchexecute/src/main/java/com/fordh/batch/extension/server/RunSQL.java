package com.fordh.batch.extension.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class RunSQL {

	public static Logger logger = Logger.getLogger(RunSQL.class.getName());
    public static String fileToRead = "/apps/fordh_config/fordh_config.properties";
	//public static String fileToRead = "/home/10293762/fordh_config.properties";
	

	private String sDB2Url = "";
	private String sDB2Port = "";
	private String sDB2Schema = "";
	private String sDB2username = "";
	private String sDB2password = "";

	private String JDBCurl ="";


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

	    sDB2Url = prop.getProperty("DB2_SERVER_URL");
	    sDB2Port = prop.getProperty("DB2_SERVER_PORT");
	    sDB2Schema = prop.getProperty("DB2_SCHEMA");
	    sDB2username = prop.getProperty("DB2_USER_ID");
	    sDB2password = prop.getProperty("DB2_PASSWORD");	
	    JDBCurl = "jdbc:db2://"+ sDB2Url +":" + sDB2Port +"/" + sDB2Schema + ":user=" +sDB2username + ";password=" +sDB2password + ";";
	    //logger.info("DB2 Connection JDBC ::" + JDBCurl);
	
	}


	public ResultSet ExecuteQuery( String sQuery) throws SQLException, ClassNotFoundException {
		GetRequiredProperties();
		Class.forName("com.ibm.db2.jcc.DB2Driver");
		String sResponse="";
		Connection con = java.sql.DriverManager.getConnection(JDBCurl);
		Statement stm=null;
	    ResultSet resultSet=null;
	    stm = con.createStatement(); 
	    resultSet = stm.executeQuery(sQuery);
	    return resultSet;
	    
	}



}

