package com.sweetNet.until;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class PropUtil {
	
	  static InputStream input = null;
	  
	  public static Properties getProperty(String fileName)
	  {
	    Properties prop = new Properties();
	    ClassLoader loader = Thread.currentThread().getContextClassLoader();
	    try
	    {
	    	
//            InputStream is = new BufferedInputStream(new FileInputStream(getPath() + fileName));
//            BufferedReader bf = new BufferedReader(new  InputStreamReader(is,"UTF-8"));
//            prop.load(bf);
//	    	
//	    	bf.close();
//	    	is.close();
//	    	
//	    	
//	      input = null;
//	      input = loader.getResourceAsStream(fileName);
//	      prop.load(input);
//	      
//	      input.close();
	    	
	    	prop.load(new FileInputStream(fileName));
	    	
	    }
	    catch (Exception e)
	    {
	      e.printStackTrace();
	    }
	    return prop;
	  }
	  
	    public static String getPath() {
	        return Thread.currentThread().getContextClassLoader().getResource("").getPath();
	    }
	
}
