package com.dlw.action;

import java.util.Base64;

public class getData {
	public static String getSFXMLData(String template_Name, String channel_Name) {
		//SF call to get the data
		
	    String xmlDatavanSF = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
	    		+ "<Jobdata template=\""+template_Name+"\">\r\n"
	    		+ "  <to>Tove</to>\r\n"
	    		+ "  <channel>\""+channel_Name+"\"</channel>\r\n"
	    		+ "  <from>Jani</from>\r\n"
	    		+ "  <heading>Reminder</heading>\r\n"
	    		+ "  <body>Don't forget me this weekend!</body>\r\n"
	    		+ "</Jobdata>";
	    String sfDataToBase64 = Base64.getEncoder().encodeToString(xmlDatavanSF.getBytes());
	    System.out.println(sfDataToBase64);
	    return sfDataToBase64;
	}
}
